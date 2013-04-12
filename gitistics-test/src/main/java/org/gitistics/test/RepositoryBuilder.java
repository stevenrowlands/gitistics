package org.gitistics.test;

import org.eclipse.jgit.lib.Repository;


/**
 * A repository builder is responsible for creating git repositories for testing
 */
public interface RepositoryBuilder {

	public RepositoryWorker<Repository> open();
	
	public void close();
}
