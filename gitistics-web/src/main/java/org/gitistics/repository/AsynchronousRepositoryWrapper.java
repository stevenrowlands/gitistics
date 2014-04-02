package org.gitistics.repository;

import org.gitistics.jpa.entities.Repo;

public class AsynchronousRepositoryWrapper implements RepositoryProcessor, Runnable  {

	private RepositoryProcessor wrapped;
	private Repo repo;
	
	public AsynchronousRepositoryWrapper(RepositoryProcessor wrapped, Repo repo) {
		this.wrapped = wrapped;
		this.repo = repo;
	}
	
	public 	void process(Repo repo) {
		new Thread(this).start();
	}
	
	public void run() {
		wrapped.process(repo);
	}
	
}
