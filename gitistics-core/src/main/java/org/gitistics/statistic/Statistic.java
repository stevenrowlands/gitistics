package org.gitistics.statistic;


/**
 * General use statistics object
 */
public class Statistic {

	String repository;
	
	String author;
	
	int year;
	
	int month;
	
	long commits;
	
	long linesAdded;
	
	long linesRemoved;

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
}
