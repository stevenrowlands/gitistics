package org.gitistics.jpa.repository;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.gitistics.jpa.entities.Repo;
import org.gitistics.jpa.repository.RepoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:repository-context.xml")
@Transactional
public class RepoRepositoryTest {

	@Autowired
	RepoRepository repoRepository;

	@Test
	public void testGitRepository() throws Exception {
		Repo r = new Repo("/some/dir/.git");
		r.setName("My Name");
		repoRepository.save(r);
		assertThat(repoRepository.count(), is(1L));
		Repo foundRepo = repoRepository.findOne("/some/dir/.git");
		assertThat(foundRepo.getLocation(), is("/some/dir/.git"));
		assertThat(foundRepo.getName(), is("My Name"));
	}
}
