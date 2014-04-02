package org.gitistics.visitor.commit.filechange;


public interface FileChangeCallback {

	public void filesChanged(FileChanges changes);
}
