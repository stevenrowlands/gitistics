package org.gitistics.jpa.statistics;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.inject.Inject;

import org.gitistics.statistic.Statistic;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:repository-context.xml")
public class JdbcRandomStatisticsProviderTest {

	@Inject
	private DataGenerator generator;
	
	@Inject
	private JdbcRandomStatisticsProvider statisticsProvider;
	
	@Test
	@Transactional
	@Rollback
	public void testSingleYearRevert() {
		generator.generate("person1", new DateTime(2013, 1, 1, 0, 0), Months.ONE, 12, 10);
		List<Statistic> statistics = statisticsProvider.revertself("repo1");
		assertThat(statistics.size(), equalTo(1));
		assertThat(statistics.get(0).getCommits(), equalTo(11L));
	}

}
