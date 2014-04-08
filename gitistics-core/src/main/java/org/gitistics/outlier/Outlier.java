package org.gitistics.outlier;

import java.sql.Date;


public class Outlier {

	private String commitId;
	
	private String authorName;

	private Date commitDate;
	
	private long linesAdded;
	
	private long linesRemoved;
	
	private boolean valid;

	public String getCommitId() {
		return commitId;
	}

	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
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

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public Date getCommitDate() {
		return commitDate;
	}

	public void setCommitDate(Date commitDate) {
		this.commitDate = commitDate;
	}

}
