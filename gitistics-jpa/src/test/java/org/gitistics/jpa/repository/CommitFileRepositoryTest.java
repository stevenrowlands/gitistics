package org.gitistics.jpa.repository;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

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
public class CommitFileRepositoryTest  {

	@Autowired 
	private CommitFileRepository commitFileRepository;
	
	@Autowired
	private CommitRepository commitRepository;
	
	@Autowired
	private RepoRepository repoRepository;
	
	@Test
	@Transactional
	@Rollback
	public void test() {
		Repo r = new Repo("/some/dir/.git");
		repoRepository.save(r);
		
		Commit c = new Commit(r, "a");

		c.setRepo(r);
		commitRepository.save(c);
		
		CommitFile cf = new CommitFile(c);
		cf.setCommit(c);
		cf.setFileName("ABC");
		cf.setLinesAdded(1);
		cf.setLinesRemoved(20);
		commitFileRepository.save(cf);
		
		CommitFile t = commitFileRepository.findAll().iterator().next();
		assertNotNull(t);
		assertThat(t.getFileName(), equalTo("ABC"));
		assertThat(t.getLinesAdded(), equalTo(1l));
		assertThat(t.getLinesRemoved(), equalTo(20l));
	}
	
}
 

