package org.gitistics.jpa.outlier;

import static org.gitistics.jpa.entities.QCommit.commit;
import static org.gitistics.jpa.entities.QRepo.repo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.gitistics.jpa.entities.Commit;
import org.gitistics.jpa.repository.CommitRepository;
import org.gitistics.outlier.Outlier;
import org.gitistics.outlier.OutlierHandler;
import org.gitistics.outlier.OutlierParam;
import org.springframework.stereotype.Component;

import com.mysema.query.jpa.impl.JPAQuery;

@Component
public class JPAOutlierHandler implements OutlierHandler {

	@PersistenceContext
	private EntityManager em;

	@Inject
	private CommitRepository commits;
	
	public List<Outlier> outliers(OutlierParam filter) {
		JPAQuery query = new JPAQuery(em)
				.from(commit)
				.innerJoin(commit.repo, repo)
				.orderBy(commit.linesAdded.desc());
		
		query.limit(filter.getPageSize());
		query.offset(filter.getPage() * filter.getPageSize());
		
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
		List<Commit> commits = query.list(commit);			
		List<Outlier> outliers = new ArrayList<Outlier>();
		for (Commit commit : commits) {
			Outlier o = new Outlier();
			o.setAuthorName(commit.getAuthorName());
			o.setCommitId(commit.getCommitId());
			o.setLinesAdded(commit.getLinesAdded());
			o.setLinesRemoved(commit.getLinesRemoved());
			o.setValid(commit.isValid());
			o.setCommitDate(commit.getCommitDate());
			outliers.add(o);
		}
		return outliers;
	}
	
	public void toggleValid(String commitId) {
		Commit cd = commits.findOne(commitId);
		cd.setValid(cd.isValid());
		commits.save(cd);
	}
}
