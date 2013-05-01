package org.gitistics.visitor.commit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.revwalk.RevCommit;
import org.gitective.core.CommitUtils;
import org.gitistics.AbstractGitTest;
import org.gitistics.visitor.commit.filechange.FileChangeCallback;
import org.gitistics.visitor.commit.filechange.SimpleFileChangeCallback;
import org.junit.Test;


public class TreeWalkVisitorStandardTest extends AbstractGitTest {

	protected CommitVisitor getTreeWalker(FileChangeCallback callback) {
		TreeWalkVisitorStandard root = new TreeWalkVisitorStandard(repo);
		root.setFileChangeCallback(callback);
		return root;
	}
	
	public void execute(RevCommit commit, FileChangeCallback callback) throws Exception {
		CommitVisitor visitor = getTreeWalker(callback);
		visitor.visit(commit);
	}
	
	/**
	 * Test a single commit with a single parent that includes no files
	 * 
	 * commit2
	 * commit1
	 */
	@Test
	public void testSingleParentNoFiles() throws Exception {
		SimpleFileChangeCallback callback = new SimpleFileChangeCallback();
		git.commit("commit1");
		RevCommit commit2 = git.commit("commit2").get();
		execute(commit2, callback);
		assertThat(callback.getFilesChanged().size(), equalTo(0));
	}
	
	/**
	 * Test a single commit that includes the addition of a single file
	 * 
	 * commit2 (+file1)
	 * commit1
	 */
	@Test
	public void testSingleParentAddSingleFile() throws Exception {
		SimpleFileChangeCallback callback = new SimpleFileChangeCallback();
		git.commit("commit1");
		git.add("file1", "A");
		RevCommit commit2 = git.commit("commit2").get();
		execute(commit2, callback);
		assertThat(callback.getFilesChanged().size(), equalTo(1));
		assertThat(callback.getFilesChanged().get(0).getChangeType(), equalTo(ChangeType.ADD));
	}
	
	/**
	 * Test a single commit that includes the deletion of single file
	 * 
	 * commit2 (-file1)
	 * commit1 (+file1)
	 */
	@Test
	public void testSingleParentRemoveSingleFile() throws Exception {
		SimpleFileChangeCallback callback = new SimpleFileChangeCallback();
		git.add("file1", "A");
		git.commit("commit1");
		git.remove("file1");
		RevCommit commit2 = git.commit("commit2").get();
		execute(commit2, callback);
		assertThat(callback.getFilesChanged().size(), equalTo(1));
		assertThat(callback.getFilesChanged().get(0).getChangeType(), equalTo(ChangeType.DELETE));
	}
	
	/**
	 * Test a single commit that includes the modification of a single file
	 * 
	 * commit2 (~file1)
	 * commit1 (+file1)
	 */
	@Test
	public void testSingleParentModifySingleFile() throws Exception {
		SimpleFileChangeCallback callback = new SimpleFileChangeCallback();
		git.add("file1", "A");
		git.commit("commit1");
		git.modify("file1", "B");
		RevCommit commit2 = git.commit("commit2").get();
		execute(commit2, callback);
		assertThat(callback.getFilesChanged().size(), equalTo(1));
		assertThat(callback.getFilesChanged().get(0).getChangeType(), equalTo(ChangeType.MODIFY));
	}
	
	/**
	 * Test merge where no changes occur in the commit (its just a fast forward)
	 * 
	 * master,branch1 - commit2 (+file1)
	 * commit1 
	 */
	@Test
	public void testMergeFastForward() throws Exception {
		SimpleFileChangeCallback callback = new SimpleFileChangeCallback();
		git.commit("commit1");
		git.branch("branch1");
		git.add("file1", "A");
		RevCommit commit2 = git.commit("commit2").get();
		git.checkout("master");
		MergeResult result = git.merge(commit2).get();
		execute(CommitUtils.getCommit(repo, result.getNewHead()), callback);
		assertThat(callback.getFilesChanged().size(), equalTo(1));
	}
	
	/**
	 * Test merge from two parents where we've only added files
	 * 
	 * merge relative to commit2 has +file3
	 * merge relative to commit3 has +file2 
	 * 
	 * We treat this as no files changed in this commit as we did nothing
	 * in the merge itself
	 * 
	 * master - merge
	 * |                \
	 * commit3 (+file3)   branch1 - commit2 (+file2)
	 * |                /
	 * commit1 (+file1)
	 */
	@Test
	public void testMultipleParentMultipleAddFileNoChanges() throws Exception {
		SimpleFileChangeCallback callback = new SimpleFileChangeCallback();
		git.add("file1", "A");
		git.commit("commit1");
		git.branch("branch1");
		git.checkout("master");
		git.add("file2", "A");
		git.commit("commit2");
		git.checkout("branch1");
		git.add("file3", "A"); 
		RevCommit commit3 = git.commit("commit3").get();
		git.checkout("master");
		MergeResult result = git.merge(commit3).get();
		execute(CommitUtils.getCommit(repo, result.getNewHead()), callback);
		assertThat(callback.getFilesChanged().size(), equalTo(0));
	}
	
	/**
	 * Test merge from two parents where we've modified against both branches
	 * 
	 * Expect that we get two change sets, one against each parent
	 * 
	 * master - merge
	 * |                \
	 * commit2 (~file1)   branch1 - commit3 (~file1)
	 * |                /
	 * commit1 (+file1)
	 */
	@Test
	public void testMergeCommitWithModificationsAgainstMultipleParents() throws Exception {
		SimpleFileChangeCallback callback = new SimpleFileChangeCallback();
		git.add("file1", "ABC");
		git.commit("commit1");
		git.branch("branch1");
		git.checkout("master");
		git.modify("file1", "A\nB\nC\nD\nX\nY\n");
		git.commit("commit2");
		git.checkout("branch1");
		git.modify("file1", "A\nB\nC\nD\nE\nF\n");
		RevCommit commit3 = git.commit("commit3").get();
		git.checkout("master");
		git.merge(commit3);
		git.modify("file1", "A\nB\nC\nD\nE\nF\nG\nH");
		RevCommit res = git.commit("Resolved").get();
		execute(res, callback);
		assertThat(callback.getFilesChanged().size(), equalTo(2));
	}
	
	/**
	 * Test merge from two parents where we've modified on both branches
	 * and then chosen one branch for the resolution
	 * 
	 * merge relative to commit2 has changes
	 * merge relative to commit3 has NO changes
	 * 
	 * We treat this as no files changed in this commit as the modification
	 * was already done in commit3...
	 * 
	 * master - merge
	 * |                \
	 * commit2 (~file1)   branch1 - commit3 (~file1)
	 * |                /
	 * commit1 (+file1)
	 */
	@Test
	public void testMergeCommitWithModificationsAgainstSingleParents() throws Exception {
		SimpleFileChangeCallback callback = new SimpleFileChangeCallback();
		git.add("file1", "ABC");
		git.commit("commit1");
		git.branch("branch1");
		git.checkout("master");
		git.modify("file1", "ABCD\nX\nY");
		git.commit("commit2");
		git.checkout("branch1");
		git.modify("file1", "ABCD\nE\nF");
		RevCommit commit3 = git.commit("commit3").get();
		git.checkout("master");
		git.merge(commit3);
		git.modify("file1", "ABCD\nE\nF");
		RevCommit res = git.commit("Resolved").get();
		assertThat(res.getParentCount(), equalTo(2));
		execute(res, callback);
		assertThat(callback.getFilesChanged().size(), equalTo(0));
	}
	
	@Test
	public void testCommitRename() throws Exception {
		SimpleFileChangeCallback callback = new SimpleFileChangeCallback();
		RevCommit commit2 = git.add("file1", "A")
				.commit("commit1")
				.add("file2", "A")
				.remove("file1")
				.commit("commit2")
		    	.get();
		execute(commit2, callback);
		assertThat(callback.getFilesChanged().size(), equalTo(1));
		assertThat(callback.getFilesChanged().get(0).getChangeType(), equalTo(ChangeType.RENAME));
	}
	
	
	@Test
	public void testCommitCopy() throws Exception {
		SimpleFileChangeCallback callback = new SimpleFileChangeCallback();
		git.add("file1", "A");
		git.commit("commit1");
		git.remove("file1");
		git.add("file2", "A");
		git.add("file3", "A");
		RevCommit commit2 = git.commit("commit2").get();
		execute(commit2, callback);
		assertThat(callback.getFilesChanged().size(), equalTo(2));
		assertThat(callback.getFilesChanged().get(0).getChangeType(), equalTo(ChangeType.RENAME));
		assertThat(callback.getFilesChanged().get(1).getChangeType(), equalTo(ChangeType.COPY));
	}
}
