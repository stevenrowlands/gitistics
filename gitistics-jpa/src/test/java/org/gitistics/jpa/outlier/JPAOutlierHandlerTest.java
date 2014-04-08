package org.gitistics.jpa.outlier;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.sql.Date;
import java.util.List;

import javax.inject.Inject;

import org.gitistics.jpa.statistics.DataGenerator;
import org.gitistics.jpa.statistics.JPAStatisticsProvider;
import org.gitistics.outlier.Outlier;
import org.gitistics.outlier.OutlierParam;
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
public class JPAOutlierHandlerTest  {
	
	@Inject
	private JPAOutlierHandler outlierHandler;
	
	@Inject
	private DataGenerator generator;
	
	@Test
	@Transactional
	@Rollback
	public void testSimple() {
		generator.generate("person1", new DateTime(2013, 1, 1, 0, 0), Months.ONE, 1, 10);
		generator.generate("person2", new DateTime(2013, 1, 1, 0, 0), Months.ONE, 1, 100000);
		OutlierParam param = new OutlierParam();
		List<Outlier> outliers = outlierHandler.outliers(param);
		assertThat(outliers.get(0).getLinesAdded(), equalTo(100000L));
	}

	
	@Test
	@Transactional
	@Rollback
	public void testDateRange() {
		generator.generate("person1", new DateTime(2013, 1, 1, 0, 0), Months.ONE, 1, 10);
		generator.generate("person2", new DateTime(2014, 1, 1, 0, 0), Months.ONE, 1, 100000);
		OutlierParam param = new OutlierParam();
		param.setDateFrom(new Date(new DateTime(2013, 1, 1, 0, 0).getMillis()));
		param.setDateTo(new Date(new DateTime(2013, 1, 2, 0, 0).getMillis()));
		List<Outlier> outliers = outlierHandler.outliers(param);
		assertThat(outliers.size(), equalTo(1));
		assertThat(outliers.get(0).getLinesAdded(), equalTo(10L));
	}
	
	@Test
	@Transactional
	@Rollback
	public void testAuthor() {
		generator.generate("person1", new DateTime(2013, 1, 1, 0, 0), Months.ONE, 1, 10);
		generator.generate("person2", new DateTime(2014, 1, 1, 0, 0), Months.ONE, 10, 100000);
		OutlierParam param = new OutlierParam();
		param.setAuthorName("person2");
		List<Outlier> outliers = outlierHandler.outliers(param);
		assertThat(outliers.size(), equalTo(10));
		assertThat(outliers.get(0).getLinesAdded(), equalTo(100000L));
	}
}
 

