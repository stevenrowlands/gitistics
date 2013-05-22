package org.gitistics.jpa.repository;

import org.gitistics.jpa.entities.Repo;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Rather unfortunate naming...
 */
public interface RepoRepository extends PagingAndSortingRepository<Repo, String> {}

