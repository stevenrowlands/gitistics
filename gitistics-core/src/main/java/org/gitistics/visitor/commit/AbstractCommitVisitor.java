package org.gitistics.visitor.commit;

import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.revwalk.RevCommit;
import org.gitistics.visitor.commit.edit.EditListVisitor;
import org.gitistics.visitor.commit.filechange.FileChange;
import org.gitistics.visitor.commit.filechange.FileChangeCallback;

public abstract class AbstractCommitVisitor implements CommitVisitor {

	private FileChangeCallback [] fileChangeCallbacks;
	
	private EditListVisitor [] editVisitors;
	
	protected boolean hasCallbacks() {
		return fileChangeCallbacks != null && fileChangeCallbacks.length > 0;
	}
	
	protected boolean hasEditVisitors() {
		return editVisitors != null && editVisitors.length > 0;
	}
	
	protected void callback(FileChange change) {
		if (!hasCallbacks()) return;
		for (FileChangeCallback callback : fileChangeCallbacks) {
			callback.fileChanged(change);
		}
	}
	
	protected void edit(RevCommit commit, RevCommit parent, EditList edits) {
		if (!hasEditVisitors()) return;
		for (EditListVisitor editVisitor : editVisitors) {
			editVisitor.visit(commit, parent, edits);
		}
	}
	
	public void setEditVisitors(EditListVisitor... editVisitors) {
		this.editVisitors = editVisitors;
	}
	
	public void setFileChangeCallback(FileChangeCallback... fileChangeCallbacks) {
		this.fileChangeCallbacks = fileChangeCallbacks;
	}
}
