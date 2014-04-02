package org.gitistics.visitor.commit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.revwalk.RevCommit;
import org.gitistics.AbstractGitTest;
import org.gitistics.visitor.commit.filechange.FileChangeCallback;
import org.gitistics.visitor.commit.filechange.SimpleFileChangeCallback;
import org.junit.Test;

public class TreeWalkVisitorRootTest extends AbstractGitTest {
	
	protected CommitVisitor getTreeWalker(FileChangeCallback callback) {
		TreeWalkVisitorRoot root = new TreeWalkVisitorRoot(repo, callback);
		return root;
	}
	
	public void execute(RevCommit commit, FileChangeCallback callback) throws Exception {
		CommitVisitor visitor = getTreeWalker(callback);
		visitor.visit(commit);
	}
	
	/**
	 * Test a single commit that includes no files
	 * 
	 * commit1
	 *
	 */
	@Test
	public void testNoFiles() throws Exception {
		SimpleFileChangeCallback callback = new SimpleFileChangeCallback();
		execute(git.commit("commit1").get(), callback);
		assertThat(callback.getFilesChanged().size(), equalTo(0));
	}
	
	
	/**
	 * Test a single commit that includes the addition of a single file
	 * 
	 * commit1 (+file1)
	 *
	 */
	@Test
	public void testAddSingleFile() throws Exception {
		SimpleFileChangeCallback callback = new SimpleFileChangeCallback();
		execute(git.add("file1", "A\nB\nC").commit("commit1").get(), callback);
		assertThat(callback.getFilesChanged().size(), equalTo(1));
	}
	
	/**
	 * Test a single commit that includes multiple files
	 * 
	 * commit1 (+file1, +file2)
	 *
	 */
	@Test
	public void testMultipleFiles() throws Exception {
		SimpleFileChangeCallback callback = new SimpleFileChangeCallback();
		git.add("file1", "A");
		git.add("file2", "A");
		execute(git.commit("commit1").get(), callback);
		assertThat(callback.getFilesChanged().size(), equalTo(2));
		assertThat(callback.getFileChange(0).getEdit(0).getChangeType(), equalTo(ChangeType.ADD));
		assertThat(callback.getFileChange(1).getEdit(0).getChangeType(), equalTo(ChangeType.ADD));
	}

}
