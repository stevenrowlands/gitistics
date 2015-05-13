package org.gitistics.jpa.statistics;


import static org.gitistics.jpa.entities.QCommit.commit;
import static org.gitistics.jpa.entities.QCommitFile.commitFile;
import static org.gitistics.jpa.entities.QRepo.repo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.gitistics.statistic.Statistic;
import org.gitistics.statistic.StatisticGroup;
import org.gitistics.statistic.StatisticOrder;
import org.gitistics.statistic.StatisticOrderBy;
import org.gitistics.statistic.StatisticParam;
import org.gitistics.statistic.StatisticsProvider;
import org.springframework.stereotype.Component;

import com.mysema.query.Tuple;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Expression;
import com.mysema.query.types.expr.ComparableExpressionBase;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.expr.StringExpressions;

@Component
public class JPAStatisticsProvider implements StatisticsProvider {

	@PersistenceContext
	private EntityManager em;

	public List<Statistic> statistics(StatisticParam filter) {
		JPAQuery query = new JPAQuery(em)
				.from(commit)
				.where(commit.valid.eq(true));
		
		if (includeFiles(filter)) {
			query = query.innerJoin(commit.files, commitFile);
		}
		
		if (filter.getPageSize() > 0) {
			query.limit(filter.getPageSize());
			query.offset(filter.getPage() * filter.getPageSize());
		}
		
		addGroupBy(query, filter);
		addOrderBy(query, filter);
		

		List<Expression<?>> select = new ArrayList<Expression<?>>();

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
		if (filter.getMessage() != null) {
			query = query.where(commit.message.containsIgnoreCase(filter.getMessage()));
		}
		if (filter.getFileName() != null) {
			query = query.where(commitFile.fileName.like(filter.getFileName()));
		}
		
		select.addAll(query.getMetadata().getGroupBy());
		select.add(commit.commitId.count());
		if (includeFiles(filter)) {
			select.add(commit.commitId.countDistinct());
		}
		select.add(linesAdded(filter));
		select.add(linesRemoved(filter));
		
		List<Tuple> results = query.list(select.toArray(new Expression [select.size()]));

		List<Statistic> statistics = new ArrayList<Statistic>();
		for (Tuple t : results) {
			Statistic s = new Statistic();
			s.setRepository(t.get(commit.repo.name));
			s.setLinesAdded(t.get(linesAdded(filter)));
			s.setLinesRemoved(t.get(linesRemoved(filter)));
			if (includeFiles(filter)) {;
				s.setCommits(t.get(commit.commitId.countDistinct()));
				s.setFilesChanged(t.get(commit.commitId.count()));
			} else {
				s.setCommits(t.get(commit.commitId.count()));
			}
			s.setAuthor(t.get(commit.authorName));
			s.setAuthorEmail(t.get(commit.authorEmail));
			if (select.contains(commit.commitDate.year())) {
				s.setYear(t.get(commit.commitDate.year()));
			}
			if (select.contains(commit.commitDate.month())) {
				s.setMonth(t.get(commit.commitDate.month()));
			}
			if (select.contains(commitFile.fileType)) {
				s.setFileType(t.get(commitFile.fileType));
			}
			statistics.add(s);
		}
		return statistics;
	}
	
	private NumberExpression<Long> linesAdded(StatisticParam params) {
		if (includeFiles(params)) {
			return commitFile.linesAdded.sum();
		}
		return commit.linesAdded.sum();
	}
	
	private NumberExpression<Long> linesRemoved(StatisticParam params) {
		if (includeFiles(params)) {
			return commitFile.linesRemoved.sum();
		}
		return commit.linesRemoved.sum();
	}
	
	private boolean includeFiles(StatisticParam params) {
		if (params.getGroups().contains(StatisticGroup.FILE_TYPE)) {
			return true;
		}
		for (StatisticOrderBy order : params.getOrders()) {
			if (order.getOrder().equals(StatisticOrder.FILE_TYPE)) {
				return true;
			}
		}
		if (params.getFileName() != null) {
			return true;
		}
		return false;
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
				case FILE_TYPE:
					query.groupBy(commitFile.fileType);
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
					e = commit.count();
					break;
				case REPOSITORY:
					e = commit.repo.name;
					break;
				case FILE_TYPE:
					e = commitFile.fileType;
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
