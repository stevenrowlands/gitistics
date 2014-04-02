package org.gitistics.jpa.commit.filechange;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.revwalk.RevCommit;
import org.gitistics.jpa.entities.Commit;
import org.gitistics.jpa.entities.CommitFile;
import org.gitistics.jpa.entities.Repo;
import org.gitistics.jpa.repository.CommitRepository;
import org.gitistics.visitor.commit.filechange.FileChange;
import org.gitistics.visitor.commit.filechange.FileChangeCallback;
import org.gitistics.visitor.commit.filechange.FileChanges;
import org.gitistics.visitor.commit.filechange.LineChanges;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JPAFileChangeCallback implements FileChangeCallback {

	@Autowired
	private CommitRepository commitRepository;

	private Date getDate(RevCommit commit) {
		return new Date(commit.getCommitTime() * 1000L);
	}

	public void filesChanged(FileChanges changes) {
		RevCommit rc = changes.getCommit();
		Date date = getDate(rc);
		Repo repo = new Repo(changes.getRepository().getDirectory().getAbsolutePath());
		
		Commit commit = new Commit(repo, rc.getId().getName());
		commit.setAuthorEmail(rc.getAuthorIdent().getEmailAddress());
		commit.setAuthorName(rc.getAuthorIdent().getName());
		commit.setCommitterEmail(rc.getCommitterIdent().getEmailAddress());
		commit.setCommitterName(rc.getCommitterIdent().getName());
		commit.setParentCount(rc.getParentCount());
		commit.setCommitDate(date);
		commit.setMessage(rc.getFullMessage());
		commit.setFiles(getFiles(commit, changes));
		
		commitRepository.save(commit);
	}
	
	private List<CommitFile> getFiles(Commit commit, FileChanges changes) {
		List<CommitFile> files = new ArrayList<CommitFile>();
		for (FileChange change : changes.getChanges()) {
			CommitFile file = new CommitFile(commit);
			
			LineChanges lineChanges = new LineChanges(change);
			file.setLinesAdded(lineChanges.getAdded());
			file.setLinesRemoved(lineChanges.getRemoved());
			
			String fileName = change.getPath();
			file.setFileName(fileName);
			files.add(file);
		}
		return files;
	}
}