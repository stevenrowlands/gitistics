package org.gitistics.jpa.entities;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class CommitStreak {

	@Id
	private String startCommitId;

	private Date startDate;

	private Date endDate;

	private int commits;

	private String committerName;

	public String getCommitterName() {
		return committerName;
	}

	public void setCommitterName(String committerName) {
		this.committerName = committerName;
	}

	@ManyToOne
	@JoinColumn(name = "repoId", nullable = false)
	private Repo repo;

	public Repo getRepo() {
		return repo;
	}

	public void setRepo(Repo repo) {
		this.repo = repo;
	}

	public String getStartCommitId() {
		return startCommitId;
	}

	public void setStartCommitId(String startCommitId) {
		this.startCommitId = startCommitId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getCommits() {
		return commits;
	}

	public void setCommits(int commits) {
		this.commits = commits;
	}

}
