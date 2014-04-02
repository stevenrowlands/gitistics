package org.gitistics.repository;

import org.gitistics.jpa.entities.Repo;

public interface RepositoryProcessor {
	
	void process(Repo repo);
	
}
