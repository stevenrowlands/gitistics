package org.gitistics.visitor;

import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.gitistics.visitor.commit.CommitVisitor;

/**
 * Goes through a RevWalk passing commits to CommitVisitor's
 */
public class CommitWalker implements Walker {

	private CommitVisitor [] commitVisitors;
	
	public CommitWalker(CommitVisitor...commitVisitors) {
		this.commitVisitors = commitVisitors;
	}
	
	public void walk(RevWalk walk) {
		for (RevCommit commit : walk) {
			visit(commit);
		}
	}
	
	private void visit(RevCommit commit) {
		for (CommitVisitor commitVisitor : commitVisitors) {
			commitVisitor.visit(commit);
		}
	}
}
