package org.gitistics.repository;

import org.eclipse.jgit.revwalk.RevWalk;
import org.gitistics.visitor.CommitWalker;
import org.gitistics.visitor.commit.CommitVisitor;

public class ThreadedWalker implements Runnable {

	private CommitVisitor visitor;
	private RevWalk walk;
	
	public ThreadedWalker(CommitVisitor visitor, RevWalk walk) {
		this.visitor = visitor;
		this.walk = walk;
	}
	
	public void run() {
		new CommitWalker(visitor).walk(walk);
	}
}
