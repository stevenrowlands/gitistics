package org.gitistics.visitor.commit;

import org.gitistics.visitor.commit.filechange.FileChangeCallback;
import org.gitistics.visitor.commit.filechange.FileChanges;

public abstract class AbstractCommitVisitor implements CommitVisitor {

	private final FileChangeCallback [] fileChangeCallbacks;
	
	public AbstractCommitVisitor(FileChangeCallback... fileChangeCallbacks) {
		this.fileChangeCallbacks = fileChangeCallbacks;
	}
	
	protected boolean hasCallbacks() {
		return fileChangeCallbacks != null && fileChangeCallbacks.length > 0;
	}
	
	protected void callback(FileChanges changes) {
		if (!hasCallbacks()) return;
		for (FileChangeCallback callback : fileChangeCallbacks) {
			callback.filesChanged(changes);
		}
	}
}
