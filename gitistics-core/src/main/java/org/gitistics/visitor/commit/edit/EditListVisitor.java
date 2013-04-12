package org.gitistics.visitor.commit.edit;

import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.revwalk.RevCommit;

public interface EditListVisitor {

	public void visit(RevCommit c, RevCommit parent, EditList edits);
}
