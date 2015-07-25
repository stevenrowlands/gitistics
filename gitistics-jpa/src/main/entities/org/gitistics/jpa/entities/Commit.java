package org.gitistics.jpa.entities;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "COMMIT")
public class Commit {

	@Id
	private String commitId;

	private String committerName;

	private String committerEmail;
	
	private String authorName;
	
	private String authorEmail;

	private Date commitDate;

	private int parentCount = 1;
	
	private long linesAdded = 0;
	
	private long linesRemoved = 0;

	private boolean valid = true;

	@ManyToOne
	@JoinColumn(name = "repoId", nullable = false)
	private Repo repo;

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "commit")
	private List<CommitFile> files;
	
	private String message;

	public Commit() {
	}

	public Commit(Repo repo, String commitId) {
		super();
		setRepo(repo);
		setCommitId(commitId);
	}
	
	public String getCommitId() {
		return commitId;
	}

	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}

	public Date getCommitDate() {
		return commitDate;
	}

	public void setCommitDate(Date commitDate) {
		this.commitDate = commitDate;
	}

	public String getCommitterName() {
		return committerName;
	}

	public void setCommitterName(String committerName) {
		this.committerName = committerName;
	}

	public String getCommitterEmail() {
		return committerEmail;
	}

	public void setCommitterEmail(String committerEmail) {
		this.committerEmail = committerEmail;
	}

	public int getParentCount() {
		return parentCount;
	}

	public void setParentCount(int parentCount) {
		this.parentCount = parentCount;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		if (message.length() > 2000) {
			message = message.substring(0, 2000);
		}
		this.message = message;
	}

	public Repo getRepo() {
		return repo;
	}

	public void setRepo(Repo repo) {
		this.repo = repo;
	}

	public List<CommitFile> getFiles() {
		return files;
	}

	public void setFiles(List<CommitFile> files) {
		this.files = files;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getAuthorEmail() {
		return authorEmail;
	}

	public void setAuthorEmail(String authorEmail) {
		this.authorEmail = authorEmail;
	}

	public long getLinesAdded() {
		return linesAdded;
	}

	public void setLinesAdded(long linesAdded) {
		this.linesAdded = linesAdded;
	}

	public long getLinesRemoved() {
		return linesRemoved;
	}

	public void setLinesRemoved(long linesRemoved) {
		this.linesRemoved = linesRemoved;
	}

}
