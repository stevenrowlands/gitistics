package org.gitistics.visitor.commit.filechange;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.revwalk.RevCommit;

public class FileChange {
	
	private RevCommit commit;

	private String path;
	
	private List<FileEdits> edits = new ArrayList<FileEdits>();
	
	public RevCommit getCommit() {
		return commit;
	}

	public void setCommit(RevCommit commit) {
		this.commit = commit;
	}

	public String getExtension() {
		if (path.contains(".")) {
			return path.substring(path.lastIndexOf(".") + 1);
		}
		return "";
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public LineChanges getLineChanges() {
		return new LineChanges(this);
	}

	public List<FileEdits> getEdits() {
		return edits;
	}

	public FileEdits getEdit(int index) {
		return edits.get(index);
	}

	public void setEdits(List<FileEdits> edits) {
		this.edits = edits;
	}

	public void addEdit(FileEdits edit) {
		edits.add(edit);
	}
}
