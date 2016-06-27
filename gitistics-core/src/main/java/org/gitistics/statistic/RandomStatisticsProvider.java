package org.gitistics.statistic;

import java.util.List;

public interface RandomStatisticsProvider {

	public List<Statistic> revertself(String repository);
	
	public List<Statistic> streaker(String repository);
}
