package org.gitistics.jpa.repository;

import org.gitistics.jpa.entities.CommitFile;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CommitFileRepository extends PagingAndSortingRepository<CommitFile, Integer> {

}
