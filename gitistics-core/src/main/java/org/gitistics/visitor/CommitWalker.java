package org.gitistics.visitor;

import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.gitistics.visitor.commit.CommitVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Goes through a RevWalk passing commits to CommitVisitor's
 */
public class CommitWalker implements Walker {

	private static final Logger log = LoggerFactory.getLogger(CommitWalker.class);
	
	private CommitVisitor [] commitVisitors;
	
	public CommitWalker(CommitVisitor...commitVisitors) {
		this.commitVisitors = commitVisitors;
	}
	
	public void walk(RevWalk walk) {
		for (RevCommit commit : walk) {
			log.debug("visiting commit {}", commit.getName());
			visit(commit);
		}
	}
	
	private void visit(RevCommit commit) {
		for (CommitVisitor commitVisitor : commitVisitors) {
			commitVisitor.visit(commit);
		}
	}
}
