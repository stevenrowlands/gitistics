package org.gitistics.statistic;

public class StatisticOrderBy {

	private StatisticOrder order;
	
	private StatisticOrderDir direction = StatisticOrderDir.ASC;

	public StatisticOrderBy() {
		
	}
	
	public StatisticOrderBy(StatisticOrder order) {
		this.order = order;
	}
	
	public StatisticOrderBy(StatisticOrder order, StatisticOrderDir direction) {
		this.order = order;
		this.direction = direction;
	}
	
	public StatisticOrder getOrder() {
		return order;
	}

	public void setOrder(StatisticOrder order) {
		this.order = order;
	}

	public StatisticOrderDir getDirection() {
		return direction;
	}

	public void setDirection(StatisticOrderDir direction) {
		this.direction = direction;
	}
}
