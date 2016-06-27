package org.gitistics.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;
import org.gitistics.jpa.entities.Repo;
import org.gitistics.revwalk.CommitRangeFilter;
import org.gitistics.revwalk.RevWalkUtils;
import org.gitistics.visitor.CommitWalker;
import org.gitistics.visitor.commit.CommitCountVisitor;
import org.gitistics.visitor.commit.CommitVisitor;
import org.gitistics.visitor.commit.PostProcessor;
import org.gitistics.visitor.commit.TreeWalkVisitorImpl;
import org.gitistics.visitor.commit.filechange.FileChangeCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class ThreadedRepositoryProcessor implements RepositoryProcessor {

	private static final Logger logger = LoggerFactory.getLogger(ThreadedRepositoryProcessor.class);
	
	private FileChangeCallback [] fileChangeCallbacks;
	
	private int numThreads = Integer.getInteger("gitistics.numthreads", 16);
	

	@Autowired
	private PostProcessor postProcessor;
	
	public void process(Repo repo) {
		try { 
			doProcess(repo);
		} catch (Exception e) {
			throw new RuntimeException("unable to process repository", e);
		}
	}
		
	private void doProcess(Repo repo) throws Exception {
		Repository repository = new org.eclipse.jgit.internal.storage.file.FileRepository(repo.getLocation());
		
		// phase 1 - get repository information
		CommitCountVisitor counter = new CommitCountVisitor();
		new CommitWalker(counter).walk(RevWalkUtils.newRevWalk(repository));
		int count = counter.getCount();
		logger.info("commit count {}", count);
		int blockSize = count / numThreads;
		int position = 1;
		List<FutureTask> fts = new ArrayList<FutureTask>();
		while (position < count) {
			int end = position + blockSize;
			if (end > count) {
				end = count;
			}
			logger.info("chunk {} to  {}", position, end);
			RevWalk walk = RevWalkUtils.newRevWalk(repository);
			walk.setRevFilter(new CommitRangeFilter(position, end));
			CommitVisitor visitor = new TreeWalkVisitorImpl(repository, fileChangeCallbacks);
			FutureTask ft = new FutureTask(new ThreadedWalker(visitor, walk), null);
			new Thread(ft).start();
			fts.add(ft);
			position = end + 1;
		}
		for (FutureTask ft : fts) {
			ft.get();
		}
		postProcessor.process();
	}
	
	@Autowired
	public void setFileChangeCallbacks(FileChangeCallback [] fileChangeCallbacks) {
		this.fileChangeCallbacks = fileChangeCallbacks;
	}
	
}
