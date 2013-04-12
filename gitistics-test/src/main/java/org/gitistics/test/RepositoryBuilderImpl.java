package org.gitistics.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RepositoryBuilderImpl implements RepositoryBuilder {

	private static final Logger logger = LoggerFactory.getLogger(RepositoryBuilderImpl.class);
	
	private List<File> repositories = new ArrayList<File>();
	
	public RepositoryWorker<Repository> open() {
		try {
			String tmpDir = System.getProperty("java.io.tmpdir");
			File dir = new File(tmpDir, UUID.randomUUID().toString().replaceAll("-", ""));
			Git.init().setDirectory(dir).setBare(false).call();
			logger.info("opening git repository at {}", dir.getAbsolutePath());
			repositories.add(dir);
			File file = new File(dir, ".git");
			Repository repository = new FileRepository(file);
			return new RepositoryWorkerImpl<Repository>(dir, repository);
		} catch (Exception e) {
			throw new RuntimeException("Error creating git repository ", e);
		}
	}

	public void close() {
		try {
			for (File f : repositories) {
				FileUtils.deleteDirectory(f);
			}
			repositories.clear();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
