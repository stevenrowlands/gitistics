package org.gitistics.test;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

public class RepositoryWorkerImpl<T> implements RepositoryWorker<T> {

	private T t;
	
	private File dir;
	
	private Repository repository;
	
	private Git git;
	
	public RepositoryWorkerImpl(File dir, T t) throws Exception {
		this.dir = dir;
		this.t = t;
		this.repository = new org.eclipse.jgit.internal.storage.file.FileRepository(new File(dir, ".git"));
		this.git = Git.wrap(repository);
	}
	
	public RepositoryWorker<RevCommit> commit(String message) {
		try {
			return new RepositoryWorkerImpl<RevCommit>(dir, git.commit().setMessage(message).call());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public RepositoryWorker<DirCache> add(String file, String content) {
		try {
			File f = new File(dir, file);
			if (!f.exists()) {
				f.createNewFile();
			}
			FileUtils.writeStringToFile(f, content);
			DirCache dirCache = Git.wrap(repository).add().addFilepattern(file).call();
			return new RepositoryWorkerImpl<DirCache>(dir, dirCache);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public RepositoryWorker<DirCache> modify(String file, String newContent) throws Exception {
		File f = new File(dir, file);
		FileUtils.writeStringToFile(f, newContent);
		return new RepositoryWorkerImpl<DirCache>(dir, Git.wrap(repository).add().addFilepattern(file).call());
	}
	
	public RepositoryWorker<DirCache> remove(String file) throws Exception {
		File f = new File(dir, file);
		if (f.exists()) {
			f.delete();
		}
		return new RepositoryWorkerImpl<DirCache>(dir, Git.wrap(repository).add().setUpdate(true).addFilepattern(".").call());
	}
	
	public RepositoryWorker<Ref> branch(String name) throws Exception {
		return new RepositoryWorkerImpl<Ref>(dir, this.git.checkout().setCreateBranch(true).setName(name).call());
	}
	
	public RepositoryWorker<Ref> checkout(String name) throws Exception {
		return new RepositoryWorkerImpl<Ref>(dir, this.git.checkout().setName(name).call());
	}
	
	public RepositoryWorker<MergeResult> merge(RevCommit r) throws Exception {
		return new RepositoryWorkerImpl<MergeResult>(dir, git.merge().include(r).call());
	}
	
	public T get() {
		return t;
	}
}
