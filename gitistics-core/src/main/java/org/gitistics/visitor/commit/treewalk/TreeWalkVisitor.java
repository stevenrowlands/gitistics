package org.gitistics.visitor.commit.treewalk;

import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;

public interface TreeWalkVisitor {

	public void visit(RevCommit commit, TreeWalk walk);
}
