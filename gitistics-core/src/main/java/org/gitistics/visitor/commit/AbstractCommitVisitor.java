package org.gitistics.visitor.commit;

import org.gitistics.visitor.commit.filechange.FileChangeCallback;
import org.gitistics.visitor.commit.filechange.FileChanges;

public abstract class AbstractCommitVisitor implements CommitVisitor {

	private FileChangeCallback [] fileChangeCallbacks;
	
	protected boolean hasCallbacks() {
		return fileChangeCallbacks != null && fileChangeCallbacks.length > 0;
	}
	
	protected void callback(FileChanges changes) {
		if (!hasCallbacks()) return;
		for (FileChangeCallback callback : fileChangeCallbacks) {
			callback.filesChanged(changes);
		}
	}
	
	public void setFileChangeCallback(FileChangeCallback... fileChangeCallbacks) {
		this.fileChangeCallbacks = fileChangeCallbacks;
	}
}
