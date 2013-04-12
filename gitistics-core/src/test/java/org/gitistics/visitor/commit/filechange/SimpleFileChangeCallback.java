package org.gitistics.visitor.commit.filechange;

import java.util.ArrayList;
import java.util.List;

public class SimpleFileChangeCallback implements FileChangeCallback {
	
	private List<FileChange> filesChanged = new ArrayList<FileChange>();
	
	public void fileChanged(FileChange change) {
		filesChanged.add(change);
	}

	public List<FileChange> getFilesChanged() {
		return filesChanged;
	}
}
