package org.gitistics.jpa.post;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.inject.Inject;

import org.gitistics.jpa.repository.CommitStreakRepository;
import org.gitistics.jpa.statistics.DataGenerator;
import org.gitistics.jpa.statistics.JdbcRandomStatisticsProvider;
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
public class JPAPostProcessorTest {

	@Inject
	private DataGenerator generator;
	
	@Inject
	private JPAPostProcessor postProcessor;

	@Inject
	private CommitStreakRepository commitStreakRepository;
	
	@Test
	@Transactional
	@Rollback
	public void testSanity() {
		generator.generate("person1", new DateTime(2013, 1, 1, 0, 0), Months.ONE, 12, 10);
		generator.generate("person2", new DateTime(2014, 1, 1, 0, 0), Months.ONE, 12, 10);
		postProcessor.process();
		
		long streak = commitStreakRepository.count();
		assertThat(streak, equalTo(2L));
	}

}
