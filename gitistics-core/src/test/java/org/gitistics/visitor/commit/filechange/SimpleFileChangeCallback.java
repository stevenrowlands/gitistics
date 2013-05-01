package org.gitistics.visitor.commit.filechange;

import java.util.List;

public class SimpleFileChangeCallback implements FileChangeCallback {

	private FileChanges changes;

	public void filesChanged(FileChanges change) {
		this.changes = change;
	}

	public FileChanges getChanges() {
		return changes;
	}

	public List<FileChange> getFilesChanged() {
		return changes.getChanges();
	}
}
