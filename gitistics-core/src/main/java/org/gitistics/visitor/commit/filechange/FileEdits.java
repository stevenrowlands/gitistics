package org.gitistics.visitor.commit.filechange;

import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.revwalk.RevCommit;

public class FileEdits {
	
	private RevCommit parent;

	private ChangeType changeType;

	private EditList edits;

	public RevCommit getParent() {
		return parent;
	}

	public void setParent(RevCommit parent) {
		this.parent = parent;
	}

	public ChangeType getChangeType() {
		return changeType;
	}

	public void setChangeType(ChangeType changeType) {
		this.changeType = changeType;
	}

	public EditList getEdits() {
		return edits;
	}

	public void setEdits(EditList edits) {
		this.edits = edits;
	}
}
