package org.gitistics.jpa.commit.filechange;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.revwalk.RevCommit;
import org.gitistics.jpa.entities.Commit;
import org.gitistics.jpa.entities.Repo;
import org.gitistics.jpa.repository.CommitRepository;
import org.gitistics.jpa.repository.RepoRepository;
import org.gitistics.test.AbstractGitTest;
import org.gitistics.visitor.commit.filechange.FileChange;
import org.gitistics.visitor.commit.filechange.FileChanges;
import org.gitistics.visitor.commit.filechange.FileEdits;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:repository-context.xml")
public class JPAFileChangeCallbackTest extends AbstractGitTest{

	@Inject
	private JPAFileChangeCallback fileChangeCallback;

	@Inject
	private CommitRepository commitRepository;
	
	@Inject
	private RepoRepository repoRepository;
	
	@Test
	@Transactional
	@Rollback
	public void testSave() {
		// RecCommit does not mock nicely.
		RevCommit rc = git.add("test", "ABC").commit("test").get();
		FileChanges changes = new FileChanges(repo, rc);
		FileChange fileChange = new FileChange();
		fileChange.setPath("/test/path");
		FileEdits edits = new FileEdits();
		edits.setChangeType(ChangeType.ADD);
		EditList el = new EditList();
		Edit edit = new Edit(10, 10, 10, 20);
		el.add(edit);
		edits.setEdits(el);
		fileChange.addEdit(edits);
		changes.addChange(fileChange);
		repoRepository.save(new Repo(changes.getRepository().getDirectory().getAbsolutePath()));
		fileChangeCallback.filesChanged(changes);
		
		Commit commit = commitRepository.findOne(rc.getId().getName());
		assertNotNull(commit);
		assertThat(commit.getLinesAdded(), equalTo(10L));
		assertThat(commit.getLinesRemoved(), equalTo(0L));
		assertThat(commit.getFiles().size(), equalTo(1));
	}
}
