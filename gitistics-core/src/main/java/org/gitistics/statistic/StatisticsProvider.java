package org.gitistics.statistic;

import java.util.List;

public interface StatisticsProvider {

	public List<Statistic> byYear(StatisticFilter filter);
}
