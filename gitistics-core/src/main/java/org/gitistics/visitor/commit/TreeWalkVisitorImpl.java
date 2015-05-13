package org.gitistics.visitor.commit;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.gitistics.visitor.commit.filechange.FileChangeCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TreeWalkVisitorImpl implements CommitVisitor {

	private static final Logger logger = LoggerFactory.getLogger(TreeWalkVisitorImpl.class);
	
	private TreeWalkVisitorRoot root;
	
	private TreeWalkVisitorStandard standard;
	
	public TreeWalkVisitorImpl(Repository repository, FileChangeCallback... fileChangeCallbacks) {
		this.root = new TreeWalkVisitorRoot(repository, fileChangeCallbacks);
		this.standard = new TreeWalkVisitorStandard(repository, fileChangeCallbacks);
	}
	
	public void visit(RevCommit commit) {
		try { 
			root.visit(commit);
			standard.visit(commit); 
		} catch (Exception e) {
			logger.error("unable to process commit " + commit.getName(), e);
		}
	}
	
}
