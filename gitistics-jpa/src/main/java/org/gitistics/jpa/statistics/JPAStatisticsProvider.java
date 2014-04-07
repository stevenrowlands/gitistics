package org.gitistics.jpa.statistics;


import static org.gitistics.jpa.entities.QCommit.commit;
import static org.gitistics.jpa.entities.QRepo.repo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.gitistics.statistic.Statistic;
import org.gitistics.statistic.StatisticGroup;
import org.gitistics.statistic.StatisticOrderBy;
import org.gitistics.statistic.StatisticParam;
import org.gitistics.statistic.StatisticsProvider;
import org.springframework.stereotype.Component;

import com.mysema.query.Tuple;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Expression;
import com.mysema.query.types.expr.ComparableExpressionBase;

@Component
public class JPAStatisticsProvider implements StatisticsProvider {

	@PersistenceContext
	private EntityManager em;

	public List<Statistic> statistics(StatisticParam filter) {
		JPAQuery query = new JPAQuery(em)
				.from(commit)
				.innerJoin(commit.repo, repo)
				.where(commit.valid.eq(true));
		
		if (filter.getPageSize() > 0) {
			query.limit(filter.getPageSize());
			query.offset(filter.getPage() * filter.getPageSize());
		}
		
		addGroupBy(query, filter);
		addOrderBy(query, filter);
		
		if (filter.getAuthorName() != null) {
			query = query.where(commit.authorName.eq(filter.getAuthorName()));
		}
		if (filter.getRepositoryName() != null) {
			query = query.where(commit.repo.name.eq(filter.getRepositoryName()));
		}
		if (filter.getDateFrom() != null) {
			query = query.where(commit.commitDate.goe(filter.getDateFrom()));
		}
		if (filter.getDateTo() != null) {
			query = query.where(commit.commitDate.loe(filter.getDateTo()));
		}
		
		List<Expression<?>> select = new ArrayList<Expression<?>>();
		select.addAll(query.getMetadata().getGroupBy());
		select.add(commit.countDistinct());
		select.add(commit.linesAdded.sum());
		select.add(commit.linesRemoved.sum());
		
		List<Tuple> results = query.list(select.toArray(new Expression [select.size()]));

		List<Statistic> statistics = new ArrayList<Statistic>();
		for (Tuple t : results) {
			Statistic s = new Statistic();
			s.setRepository(t.get(repo.name));
			s.setLinesAdded(t.get(commit.linesAdded.sum()));
			s.setLinesRemoved(t.get(commit.linesRemoved.sum()));
			s.setCommits(t.get(commit.countDistinct()));
			s.setAuthor(t.get(commit.authorName));
			if (select.contains(commit.commitDate.year())) {
				s.setYear(t.get(commit.commitDate.year()));
			}
			if (select.contains(commit.commitDate.month())) {
				s.setMonth(t.get(commit.commitDate.month()));
			}
			statistics.add(s);
		}
		return statistics;
	}
	
	private void addGroupBy(JPAQuery query, StatisticParam params) {
		for (StatisticGroup group : params.getGroups()) {
			switch(group) {
				case YEAR:
					query.groupBy(commit.commitDate.year());
					break;
				case MONTH:
					query.groupBy(commit.commitDate.month());
					break;
				case AUTHOR:
					query.groupBy(commit.authorName);
					break;
				case REPOSITORY:
					query.groupBy(repo.name);
					break;
			}
		}
	}
	
	private void addOrderBy(JPAQuery query, StatisticParam params) {
		for (StatisticOrderBy orderBy : params.getOrders()) {
			ComparableExpressionBase<?> e = null;
			switch(orderBy.getOrder()) {
				case YEAR:
					e = commit.commitDate.year();
					break;
				case MONTH:
					e = commit.commitDate.month();
					break;
				case AUTHOR:
					e = commit.authorName;
					break;
				case COMMITS:
					e = commit.countDistinct();
					break;
				case REPOSITORY:
					e = repo.name;
					break;
			}
			switch(orderBy.getDirection()) {
				case ASC:
					query.orderBy(e.asc());
					break;
				case DESC:
					query.orderBy(e.desc());
			}
			
		}
		
	}
	
}
