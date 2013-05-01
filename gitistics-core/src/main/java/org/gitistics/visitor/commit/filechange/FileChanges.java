package org.gitistics.visitor.commit.filechange;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.revwalk.RevCommit;

public class FileChanges {
	
	private RevCommit commit;

	private List<FileChange> changes = new ArrayList<FileChange>();

	
	public FileChanges(RevCommit commit) {
		this.commit = commit;
	}

	public List<FileChange> getChanges() {
		return changes;
	}

	public void addChange(FileChange change) {
		changes.add(change);
	}

	public RevCommit getCommit() {
		return this.commit;
	}

}
