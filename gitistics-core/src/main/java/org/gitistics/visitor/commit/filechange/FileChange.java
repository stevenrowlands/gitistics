package org.gitistics.visitor.commit.filechange;

import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.revwalk.RevCommit;

public class FileChange {
	
	private RevCommit commit;
	
	private RevCommit parent;
	
	private ChangeType changeType;

	private String path;
	
	private EditList edits;
	
	public RevCommit getCommit() {
		return commit;
	}

	public void setCommit(RevCommit commit) {
		this.commit = commit;
	}

	public ChangeType getChangeType() {
		return changeType;
	}

	public void setChangeType(ChangeType changeType) {
		this.changeType = changeType;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public RevCommit getParent() {
		return parent;
	}

	public void setParent(RevCommit parent) {
		this.parent = parent;
	}

	public EditList getEdits() {
		return edits;
	}

	public void setEdits(EditList edits) {
		this.edits = edits;
	}
	
	public LineChanges getLineChanges() {
		return new LineChanges(this);
	}
}
