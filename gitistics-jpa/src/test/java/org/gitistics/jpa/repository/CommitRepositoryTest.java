package org.gitistics.jpa.repository;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gitistics.jpa.entities.Commit;
import org.gitistics.jpa.entities.CommitFile;
import org.gitistics.jpa.entities.Repo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:repository-context.xml")
public class CommitRepositoryTest  {

	private static final String COMMIT_ID = "000aaa111bbb222ccc333ccc444ddd555eee666";
	@Autowired
	private CommitRepository commitRepository;
	
	@Autowired
	private RepoRepository repoRepository;
	
	@Test
	@Transactional
	@Rollback
	public void testBasicPersist() {
		Repo repo = new Repo("/some/dir/.git");
		repoRepository.save(repo);
		
		Commit commit = getBasicCommit(repo);
		commit.setFiles(new ArrayList<CommitFile>());
		commitRepository.save(commit);
		
		Commit find = commitRepository.findOne(COMMIT_ID);
		assertThat(find, is(notNullValue()));
		assertThat(find.getAuthorEmail(), equalTo("author@test.email.com"));
		assertThat(find.getAuthorName(), equalTo("author"));
		assertThat(find.getCommitterEmail(), equalTo("committer@test.email.com"));
		assertThat(find.getCommitterName(), equalTo("committer"));
		assertThat(find.getMessage(), equalTo("commit message"));
		assertThat(find.getParentCount(), equalTo(1));
	}
	
	@Test
	@Transactional
	@Rollback
	public void testPersistCommitFiles() {
		Repo repo = new Repo("/some/dir/.git");
		repoRepository.save(repo);
		
		Commit commit = getBasicCommit(repo);
		
		CommitFile file = new CommitFile();
		file.setLinesAdded(10);
		file.setLinesRemoved(10);
		file.setCommit(commit);
		file.setFileName("a.txt");
		commit.setFiles(Arrays.asList(file));
		commitRepository.save(commit);
		
		Commit find = commitRepository.findOne(COMMIT_ID);
		List<CommitFile> files = find.getFiles();
		assertThat(find, is(notNullValue()));
		assertThat(files.get(0).getFileName(), equalTo(file.getFileName()));
	}
	
	
	private Commit getBasicCommit(Repo repo) {
		Commit commit = new Commit(repo, COMMIT_ID);
		commit.setAuthorEmail("author@test.email.com");
		commit.setAuthorName("author");
		commit.setCommitterEmail("committer@test.email.com");
		commit.setCommitterName("committer");
		commit.setMessage("commit message");
		commit.setParentCount(1);
		return commit;
	}
	
}
 

