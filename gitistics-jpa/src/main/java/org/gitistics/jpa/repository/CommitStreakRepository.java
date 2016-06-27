package org.gitistics.jpa.repository;

import org.gitistics.jpa.entities.Commit;
import org.gitistics.jpa.entities.CommitStreak;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CommitStreakRepository extends PagingAndSortingRepository<CommitStreak, String> {}

