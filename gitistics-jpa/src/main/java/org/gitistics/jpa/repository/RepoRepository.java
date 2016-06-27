package org.gitistics.jpa.repository;

import javax.transaction.Transactional;

import org.gitistics.jpa.entities.Repo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Rather unfortunate naming...
 */
public interface RepoRepository extends PagingAndSortingRepository<Repo, String> { 

	
	@Transactional
	@Modifying
	@Query("delete Commit c where c.repo = ?1")
	int deleteCommits(Repo repo);
	
	
	Repo findByName(String name);
}
