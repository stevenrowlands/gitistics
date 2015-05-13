package org.gitistics.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@SuppressWarnings("serial")
@Entity
@Table(name = "CommitFile")
public class CommitFile extends AbstractPersistable<Integer> {

	@ManyToOne
	@JoinColumn(name = "commitId", nullable=false)
	private Commit commit;

	private String fileName;
	
	private String fileType;

	private long linesAdded;

	private long linesRemoved;

	public CommitFile() {
		
	}
	
	public CommitFile(Commit commit) {
		setCommit(commit);
	}
	
	public Commit getCommit() {
		return commit;
	}

	public void setCommit(Commit commit) {
		this.commit = commit;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
}
