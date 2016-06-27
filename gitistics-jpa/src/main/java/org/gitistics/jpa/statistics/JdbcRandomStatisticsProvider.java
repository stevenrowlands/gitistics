package org.gitistics.jpa.statistics;

import static org.gitistics.jpa.entities.QCommitStreak.commitStreak;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.gitistics.jpa.entities.Repo;
import org.gitistics.jpa.repository.RepoRepository;
import org.gitistics.statistic.RandomStatisticsProvider;
import org.gitistics.statistic.Statistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.mysema.query.Tuple;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Expression;

@Component
public class JdbcRandomStatisticsProvider implements RandomStatisticsProvider {

	@Autowired
	private JdbcTemplate template;

	@Autowired
	private RepoRepository repoRepository;

	@PersistenceContext
	private EntityManager em;

	public List<Statistic> revertself(String repoName) {

		Repo repository = repoRepository.findByName(repoName);
		List<Map<String, Object>> rows = template.queryForList(
				"select year(a.commitdate) as COMMITYEAR, a.committername as COMMITTERNAME, count(*) AS CNT from commit a, commit b where a.revert = b.commitId and a.committername = b.committername and a.repoid = ? group by year(a.commitdate), a.committername order by year(a.commitdate) DESC, count(*) DESC",
				repository.getLocation());

		List<Statistic> statistics = new ArrayList<Statistic>();
		for (Map<String, Object> result : rows) {
			Statistic s = new Statistic();
			s.setAuthor((String) result.get("COMMITTERNAME"));
			s.setYear((Integer) result.get("COMMITYEAR"));
			s.setCommits((Long) result.get("CNT"));
			statistics.add(s);
		}
		return statistics;
	}

	public List<Statistic> streaker(String repository) {
		JPAQuery query = new JPAQuery(em).from(commitStreak);
		query.where(commitStreak.repo.name.eq(repository)
				.and(commitStreak.endDate.ne(commitStreak.startDate)));

		
		List<Expression<?>> select = new ArrayList<Expression<?>>();
		select.add(commitStreak.committerName);
		select.add(commitStreak.startDate);
		select.add(commitStreak.endDate);
		select.add(commitStreak.commits);
		query.orderBy(commitStreak.commits.desc());
		query.limit(100);

		List<Tuple> results = query.list(select.toArray(new Expression[select.size()]));

		List<Statistic> statistics = new ArrayList<Statistic>();
		for (Tuple t : results) {
			Statistic s = new Statistic();
			s.setCommits(t.get(commitStreak.commits));
			s.setAuthor(t.get(commitStreak.committerName));
			s.setStartDate(t.get(commitStreak.startDate));
			s.setEndDate(t.get(commitStreak.endDate));
			statistics.add(s);
		}
		return statistics;

	}

}
