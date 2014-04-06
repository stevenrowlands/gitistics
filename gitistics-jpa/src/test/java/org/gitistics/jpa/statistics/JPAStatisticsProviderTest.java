package org.gitistics.jpa.statistics;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.sql.Date;
import java.util.List;

import javax.inject.Inject;

import org.gitistics.statistic.Statistic;
import org.gitistics.statistic.StatisticGroup;
import org.gitistics.statistic.StatisticOrder;
import org.gitistics.statistic.StatisticOrderBy;
import org.gitistics.statistic.StatisticParam;
import org.joda.time.DateTime;
import org.joda.time.Days;
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
	public void testNoParameters() {
		generator.generate("person1", new DateTime(2013, 1, 1, 0, 0), Months.ONE, 24, 10);
		StatisticParam filter = new StatisticParam();
		List<Statistic> statistics = statisticsProvider.statistics(filter);
		assertThat(statistics.size(), equalTo(1));
		assertThat(statistics.get(0).getLinesAdded(), equalTo(240L));
		assertThat(statistics.get(0).getLinesRemoved(), equalTo(240L));
		assertThat(statistics.get(0).getCommits(), equalTo(24L));
	}
	
	

	@Test
	@Transactional
	@Rollback
	public void testByMonth() {
		generator.generate("person1", new DateTime(2013, 1, 1, 0, 0), Days.ONE, 50, 10);
		StatisticParam filter = new StatisticParam();
		filter.addGroup(StatisticGroup.YEAR);
		filter.addGroup(StatisticGroup.MONTH);
		filter.addOrder(new StatisticOrderBy(StatisticOrder.MONTH));;
		List<Statistic> statistics = statisticsProvider.statistics(filter);
		assertThat(statistics.size(), equalTo(2));
		assertThat(statistics.get(0).getLinesAdded(), equalTo(310L));
		assertThat(statistics.get(0).getLinesRemoved(), equalTo(310L));
		assertThat(statistics.get(0).getCommits(), equalTo(31L));
		assertThat(statistics.get(0).getMonth(), equalTo(1));
		assertThat(statistics.get(0).getYear(), equalTo(2013));
	}
	
	@Test
	@Transactional
	@Rollback
	public void testAuthor() {
		generator.generate("person1", new DateTime(2013, 1, 1, 0, 0), Months.ONE, 12, 10);
		generator.generate("person2", new DateTime(2013, 1, 1, 0, 0), Months.ONE, 24, 10);
		StatisticParam filter = new StatisticParam();
		filter.setAuthorName("person1");
		List<Statistic> statistics = statisticsProvider.statistics(filter);
		assertThat(statistics.size(), equalTo(1));
		assertThat(statistics.get(0).getLinesAdded(), equalTo(120L));
		assertThat(statistics.get(0).getLinesRemoved(), equalTo(120L));
		assertThat(statistics.get(0).getCommits(), equalTo(12L));
	}
	
	
	@Test
	@Transactional
	@Rollback
	public void testSingleYear() {
		generator.generate("person1", new DateTime(2013, 1, 1, 0, 0), Months.ONE, 12, 10);
		StatisticParam filter = new StatisticParam();
		filter.addGroup(StatisticGroup.YEAR);
		List<Statistic> statistics = statisticsProvider.statistics(filter);
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
		StatisticParam filter = new StatisticParam();
		filter.addGroup(StatisticGroup.YEAR);
		List<Statistic> statistics = statisticsProvider.statistics(filter);
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
		StatisticParam filter = new StatisticParam();
		filter.addOrder(new StatisticOrderBy(StatisticOrder.YEAR));
		filter.addGroup(StatisticGroup.YEAR);
		filter.setPage(0);
		filter.setPageSize(2);
		List<Statistic> statistics = statisticsProvider.statistics(filter);
		assertThat(statistics.size(), equalTo(2));
		assertThat(statistics.get(0).getYear(), equalTo(2000));
		filter.setPage(1);
		statistics = statisticsProvider.statistics(filter);
		assertThat(statistics.size(), equalTo(2));
		assertThat(statistics.get(0).getYear(), equalTo(2002));
	}
	
	@Test
	@Transactional
	@Rollback
	public void testDateFilter() {
		DateTime start = new DateTime(2000, 1, 1, 0, 0);
		generator.generate("person1", start, Years.ONE, 10, 10);
		
		StatisticParam filter = new StatisticParam();
		filter.addGroup(StatisticGroup.YEAR);
		filter.addOrder(new StatisticOrderBy(StatisticOrder.YEAR));
		filter.setDateFrom(new Date(start.plusYears(1).getMillis()));
		List<Statistic> statistics = statisticsProvider.statistics(filter);
		assertThat(statistics.size(), equalTo(9));
		assertThat(statistics.get(0).getYear(), equalTo(2001));
		
		filter.setDateTo(new Date(new DateTime(2002,1,1,0,0).getMillis()));
		statistics = statisticsProvider.statistics(filter);
		assertThat(statistics.size(), equalTo(2));
		
		filter.setDateFrom(null);
		statistics = statisticsProvider.statistics(filter);
		assertThat(statistics.size(), equalTo(3));
		assertThat(statistics.get(0).getYear(), equalTo(2000));
		assertThat(statistics.get(2).getYear(), equalTo(2002));
	}
}
 

