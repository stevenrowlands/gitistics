package org.gitistics.repository;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepository;
import org.gitistics.jpa.entities.Repo;
import org.gitistics.revwalk.RevWalkUtils;
import org.gitistics.visitor.commit.CommitVisitor;
import org.gitistics.visitor.commit.TreeWalkVisitorImpl;
import org.gitistics.visitor.commit.filechange.FileChangeCallback;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.PeriodFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Single threaded dumb processor for a repository. Useful for testing
 */
@Component
public class SimpleRepositoryProcessor implements RepositoryProcessor{

	private static final Logger logger = LoggerFactory.getLogger(SimpleRepositoryProcessor.class);
	
	private FileChangeCallback [] fileChangeCallbacks;
	
	public void process(Repo repo) {
		try { 
			doProcess(repo);
		} catch (Exception e) {
			throw new RuntimeException("unable to process repository", e);
		}
	}
		
	private void doProcess(Repo repo) throws Exception {
		Repository repository = new FileRepository(repo.getLocation());
		DateTime start = new DateTime();
		RevWalk walk = RevWalkUtils.newRevWalk(repository);
		CommitVisitor visitor = new TreeWalkVisitorImpl(repository, fileChangeCallbacks);
		for (RevCommit commit : walk) {
			logger.info("visiting commit {}", commit.getName());
			visitor.visit(commit);
		}
		DateTime end = new DateTime();
		logger.info("completed processing in {}", PeriodFormat.getDefault().print((new Duration(start.getMillis(), end.getMillis()).toPeriod())));
	}
	
	@Autowired
	public void setFileChangeCallbacks(FileChangeCallback [] fileChangeCallbacks) {
		this.fileChangeCallbacks = fileChangeCallbacks;
	}
}
