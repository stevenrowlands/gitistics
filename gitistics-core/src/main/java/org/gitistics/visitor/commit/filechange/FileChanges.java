package org.gitistics.visitor.commit.filechange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

public class FileChanges {

	private Repository repository;
	
	private RevCommit commit;

	private Map<String, FileChange> changes = new HashMap<String, FileChange>();

	public FileChanges(Repository repository, RevCommit commit) {
		this.repository = repository;
		this.commit = commit;
	}

	public List<FileChange> getChanges() {
		return new ArrayList<FileChange>(changes.values());
	}

	public FileChange getChange(String id) {
		return changes.get(id);
	}

	public FileChange getChange(int index) {
		return getChanges().get(index);
	}

	public void addChange(String id, FileChange change) {
		changes.put(id, change);
	}

	public RevCommit getCommit() {
		return this.commit;
	}

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}
	
	

}
