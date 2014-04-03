package org.gitistics.jpa.statistics;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.sql.Date;
import java.util.List;

import javax.inject.Inject;

import org.gitistics.statistic.Statistic;
import org.gitistics.statistic.StatisticFilter;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.Years;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:repository-context.xml")
public class JPAStatisticsProviderTest  {
	
	@Inject
	private DataGenerator generator;
	
	@Inject
	private JPAStatisticsProvider statisticsProvider;
	
	@Test
	@Transactional
	@Rollback
	public void testSingleYear() {
		generator.generate("person1", new DateTime(2013, 1, 1, 0, 0), Months.ONE, 12, 10);
		List<Statistic> statistics = statisticsProvider.byYear(new StatisticFilter());
		assertThat(statistics.size(), equalTo(1));
		assertThat(statistics.get(0).getLinesAdded(), equalTo(120L));
		assertThat(statistics.get(0).getLinesRemoved(), equalTo(120L));
		assertThat(statistics.get(0).getCommits(), equalTo(12L));
	}

	@Test
	@Transactional
	@Rollback
	public void testMultipleYear() {
		generator.generate("person1", new DateTime(2013, 1, 1, 0, 0), Years.ONE, 2, 10);
		List<Statistic> statistics = statisticsProvider.byYear(new StatisticFilter());
		assertThat(statistics.size(), equalTo(2));
		assertThat(statistics.get(0).getLinesAdded() + statistics.get(1).getLinesAdded(), equalTo(20L));
		assertThat(statistics.get(0).getLinesRemoved() + statistics.get(1).getLinesRemoved(), equalTo(20L));
		assertThat(statistics.get(0).getCommits() + statistics.get(1).getCommits(), equalTo(2L));
	}
	
	@Test
	@Transactional
	@Rollback
	public void testPaging() {
		generator.generate("person1", new DateTime(2000, 1, 1, 0, 0), Years.ONE, 10, 10);
		StatisticFilter filter = new StatisticFilter();
		filter.setPage(0);
		filter.setPageSize(2);
		List<Statistic> statistics = statisticsProvider.byYear(filter);
		assertThat(statistics.size(), equalTo(2));
		assertThat(statistics.get(0).getYear(), equalTo(2000));
		filter.setPage(1);
		statistics = statisticsProvider.byYear(filter);
		assertThat(statistics.size(), equalTo(2));
		assertThat(statistics.get(0).getYear(), equalTo(2002));
	}
	
	@Test
	@Transactional
	@Rollback
	public void testDateFilter() {
		DateTime start = new DateTime(2000, 1, 1, 0, 0);
		generator.generate("person1", start, Years.ONE, 10, 10);
		
		StatisticFilter filter = new StatisticFilter();
		filter.setDateFrom(new Date(start.plusYears(1).getMillis()));
		List<Statistic> statistics = statisticsProvider.byYear(filter);
		assertThat(statistics.size(), equalTo(9));
		assertThat(statistics.get(0).getYear(), equalTo(2001));
		
		filter.setDateTo(new Date(new DateTime(2002,1,1,0,0).getMillis()));
		statistics = statisticsProvider.byYear(filter);
		assertThat(statistics.size(), equalTo(2));
		
		filter.setDateFrom(null);
		statistics = statisticsProvider.byYear(filter);
		assertThat(statistics.size(), equalTo(3));
		assertThat(statistics.get(0).getYear(), equalTo(2000));
		assertThat(statistics.get(2).getYear(), equalTo(2002));
	}
}
 

