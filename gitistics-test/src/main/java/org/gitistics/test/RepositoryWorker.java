package org.gitistics.test;

import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;

public interface RepositoryWorker<T> {

	public RepositoryWorker<DirCache> add(String file, String content);
	
	public RepositoryWorker<DirCache> modify(String file, String newContent) throws Exception;

	public RepositoryWorker<DirCache> remove(String file) throws Exception;
	
	public RepositoryWorker<Ref> branch(String name) throws Exception;
	
	public RepositoryWorker<Ref> checkout(String name) throws Exception;
	
	public RepositoryWorker<RevCommit> commit(String message);

	public RepositoryWorker<MergeResult> merge(RevCommit r) throws Exception;
	
	public T get();
}
