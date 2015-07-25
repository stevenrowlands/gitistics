package org.gitistics.statistic;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * General use statistics filter
 */
public class StatisticParam {

	int page = 0;

	int pageSize = 50;

	private Date dateFrom;

	private Date dateTo;
	
	private String authorName;
	
	private String repositoryName;
	
	private String message;
	
	private String fileName;
	
	private int parentCount;
	
	private List<StatisticGroup> groups = new ArrayList<StatisticGroup>();
	
	private List<StatisticOrderBy> orders = new ArrayList<StatisticOrderBy>();

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}

	public List<StatisticGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<StatisticGroup> groups) {
		this.groups = groups;
	}
	
	public void addGroup(StatisticGroup group) {
		this.groups.add(group);
	}

	public List<StatisticOrderBy> getOrders() {
		return orders;
	}

	public void setOrders(List<StatisticOrderBy> orders) {
		this.orders = orders;
	}
	
	public void addOrder(StatisticOrderBy order) {
		this.orders.add(order);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getParentCount() {
		return parentCount;
	}

	public void setParentCount(int parentCount) {
		this.parentCount = parentCount;
	}

}
