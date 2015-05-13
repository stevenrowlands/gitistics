package org.gitistics.visitor.commit;

import org.eclipse.jgit.revwalk.RevCommit;

public class CommitCountVisitor implements CommitVisitor {
	

	private int count;
	
	public void visit(RevCommit commit) {
		count++;
	}
	
	public int getCount() {
		return count;
	}
	
}
