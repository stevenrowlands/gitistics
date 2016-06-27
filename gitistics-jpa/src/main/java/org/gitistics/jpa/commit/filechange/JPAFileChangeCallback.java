package org.gitistics.jpa.commit.filechange;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	private static final Pattern REVERT = Pattern.compile("This reverts commit ([a-f0-9]+)");

	@Autowired
	private CommitRepository commitRepository;

	private Date getDate(RevCommit commit) {
		return new Date(commit.getCommitTime() * 1000L);
	}

	public void filesChanged(FileChanges changes) {
		RevCommit rc = changes.getCommit();
		Date date = getDate(rc);
		Repo repo = new Repo(changes.getRepository().getDirectory().getAbsolutePath());
		String fullMessage = rc.getFullMessage();
		Matcher matcher = REVERT.matcher(fullMessage);
		String reverts = "";
		if (matcher.find()) {
			reverts = matcher.group(1);
		}
		Commit commit = new Commit(repo, rc.getId().getName());
		commit.setAuthorEmail(rc.getAuthorIdent().getEmailAddress());
		commit.setAuthorName(rc.getAuthorIdent().getName());
		commit.setCommitterEmail(rc.getCommitterIdent().getEmailAddress());
		commit.setCommitterName(rc.getCommitterIdent().getName());
		commit.setParentCount(rc.getParentCount());
		commit.setCommitDate(date);
		commit.setMessage(rc.getFullMessage());
		commit.setFiles(getFiles(commit, changes));
		commit.setRevert(reverts);
		for (CommitFile cf : commit.getFiles()) {
			commit.setLinesAdded(commit.getLinesAdded() + cf.getLinesAdded());
			commit.setLinesRemoved(commit.getLinesRemoved() + cf.getLinesRemoved());
		}
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
			file.setFileType(getType(fileName));
			files.add(file);
		}
		return files;
	}

	private String getType(String name) {
		if (name.contains(".")) {
			String type = name.substring(name.lastIndexOf(".") + 1);
			if (type.contains("/") || type.contains("\\") || type.contains("#")) {
				return "";
			}
			return type;
		}
		return "";
	}
}