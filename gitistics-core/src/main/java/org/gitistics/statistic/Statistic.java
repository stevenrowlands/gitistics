package org.gitistics.statistic;

import java.sql.Date;

/**
 * General use statistics object
 */
public class Statistic {

	String repository;
	
	String author;

	String authorEmail;
	
	int year;
	
	int month;
	
	long commits;
	
	long filesChanged;
	
	long linesAdded;
	
	long linesRemoved;
	
	String fileName;
	
	String fileType;

	Date startDate;
	
	Date endDate;

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

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public long getCommits() {
		return commits;
	}

	public void setCommits(long commits) {
		this.commits = commits;
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

	public String getAuthorEmail() {
		return authorEmail;
	}

	public void setAuthorEmail(String authorEmail) {
		this.authorEmail = authorEmail;
	}

	public long getFilesChanged() {
		return filesChanged;
	}

	public void setFilesChanged(long filesChanged) {
		this.filesChanged = filesChanged;
	}
}
