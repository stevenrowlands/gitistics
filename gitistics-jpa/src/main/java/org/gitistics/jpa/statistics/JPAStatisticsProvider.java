package org.gitistics.jpa.statistics;


import static org.gitistics.jpa.entities.QRepo.repo;
import static org.gitistics.jpa.entities.QCommit.commit;
import static org.gitistics.jpa.entities.QCommitFile.commitFile;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.gitistics.jpa.entities.QRepo;
import org.gitistics.statistic.Statistic;
import org.gitistics.statistic.StatisticFilter;
import org.gitistics.statistic.StatisticsProvider;
import org.springframework.stereotype.Component;

import com.mysema.query.Tuple;
import com.mysema.query.jpa.impl.JPAQuery;

@Component
public class JPAStatisticsProvider implements StatisticsProvider {

	@PersistenceContext
	private EntityManager em;

	public List<Statistic> byYear(StatisticFilter filter) {
		JPAQuery query = new JPAQuery(em)
				.from(commitFile)
				.innerJoin(commitFile.commit, commit)
				.innerJoin(commit.repo, repo)
				.groupBy(repo.name, commit.commitDate.year())
				.where(commit.valid.eq(true))
				.orderBy(commit.commitDate.year().asc())
				.limit(filter.getPageSize()).offset(filter.getPage() * filter.getPageSize());
		
		if (filter.getDateFrom() != null) {
			query = query.where(commit.commitDate.goe(filter.getDateFrom()));
		}
		if (filter.getDateTo() != null) {
			query = query.where(commit.commitDate.loe(filter.getDateTo()));
		}

		List<Tuple> results = query.list(repo.name, commit.commitDate.year(),
				commit.count(), commitFile.linesAdded.sum(),
				commitFile.linesRemoved.sum());

		List<Statistic> statistics = new ArrayList<Statistic>();
		for (Tuple t : results) {
			Statistic s = new Statistic();
			s.setRepository(t.get(repo.name));
			s.setLinesAdded(t.get(commitFile.linesAdded.sum()));
			s.setLinesRemoved(t.get(commitFile.linesRemoved.sum()));
			s.setCommits(t.get(commit.count()));
			s.setYear(t.get(commit.commitDate.year()));
			statistics.add(s);
		}
		return statistics;
	}
}
