package org.gitistics.test;

import org.eclipse.jgit.lib.Repository;
import org.junit.After;
import org.junit.Before;

public abstract class AbstractGitTest {

	protected RepositoryBuilder api = new RepositoryBuilderImpl();
	
	protected RepositoryWorker<Repository> git;
	
	protected Repository repo;
	
	@Before
	public void setup() {
		this.git = api.open();
		this.repo = git.get();
	}
	
	@After
	public void clean() {
		api.close();
	}
}
