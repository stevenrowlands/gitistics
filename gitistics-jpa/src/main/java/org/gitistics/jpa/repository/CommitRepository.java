package org.gitistics.jpa.repository;

import org.gitistics.jpa.entities.Commit;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CommitRepository extends PagingAndSortingRepository<Commit, String> {}

