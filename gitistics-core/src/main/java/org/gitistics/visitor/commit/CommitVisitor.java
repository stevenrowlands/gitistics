package org.gitistics.visitor.commit;

import org.eclipse.jgit.revwalk.RevCommit;

public interface CommitVisitor {

	public void visit(RevCommit commit);
	
}
