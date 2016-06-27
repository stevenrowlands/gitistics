package org.gitistics.jpa.post;

import java.sql.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.gitistics.jpa.entities.Commit;
import org.gitistics.jpa.entities.CommitStreak;
import org.gitistics.jpa.repository.CommitRepository;
import org.gitistics.jpa.repository.CommitStreakRepository;
import org.gitistics.visitor.commit.PostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

@Component
public class JPAPostProcessor implements PostProcessor {

	@Autowired
	private CommitStreakRepository commitStreakRepository;

	@Autowired
	private CommitRepository commitRepository;

	@PersistenceContext
	private EntityManager em;

	private CommitStreak currentStreak;

	public void process() {
		for (Commit commit : commitRepository.findAll(new Sort(Direction.ASC, "commitDate"))) {
			if (currentStreak == null) {
				currentStreak = newStreak(commit);
			}

			if (currentStreak.getCommitterName().equals(commit.getCommitterName())) {
				currentStreak.setCommits(currentStreak.getCommits() + 1);
			} else {
				if (currentStreak.getCommits() > 5) {
					currentStreak.setEndDate(commit.getCommitDate());
					commitStreakRepository.save(currentStreak);
				}
				currentStreak = newStreak(commit);
			}
		}
		if (currentStreak.getCommits() > 5) {
			commitStreakRepository.save(currentStreak);
		}
	}

	private CommitStreak newStreak(Commit commit) {
		CommitStreak currentStreak = new CommitStreak();
		currentStreak.setStartCommitId(commit.getCommitId());
		currentStreak.setCommitterName(commit.getCommitterName());
		currentStreak.setStartDate(commit.getCommitDate());
		currentStreak.setRepo(commit.getRepo());
		return currentStreak;
	}

}
