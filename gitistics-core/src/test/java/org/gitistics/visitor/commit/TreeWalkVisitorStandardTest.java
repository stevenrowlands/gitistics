package org.gitistics.visitor.commit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.revwalk.RevCommit;
import org.gitective.core.CommitUtils;
import org.gitistics.test.AbstractGitTest;
import org.gitistics.visitor.commit.filechange.FileChangeCallback;
import org.gitistics.visitor.commit.filechange.FileEdits;
import org.gitistics.visitor.commit.filechange.SimpleFileChangeCallback;
import org.junit.Test;


public class TreeWalkVisitorStandardTest extends AbstractGitTest {

	protected CommitVisitor getTreeWalker(FileChangeCallback callback) {
		TreeWalkVisitorStandard root = new TreeWalkVisitorStandard(repo, callback);
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
		assertThat(callback.getFilesChanged().get(0).getEdits().get(0).getChangeType(), equalTo(ChangeType.ADD));
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
		assertThat(callback.getFilesChanged().get(0).getEdits().get(0).getChangeType(), equalTo(ChangeType.DELETE));
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
		assertThat(callback.getFilesChanged().get(0).getEdits().get(0).getChangeType(), equalTo(ChangeType.MODIFY));
	}
	
	/**
	 * Test merge where no changes occur in the commit (its just a fast forward - so in the end 1 parent)
	 * 
	 * commit2 (+file1) (master,branch1)
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
 	 * So one file changed with two sets of different changes
* REPLACE(4-6, 4-8) 
* INSERT(6-6, 6-8) 
* 
* Lines are
* 0 - A\n
* 1 - B\n
* 2 - C\n
* 3 - D\n
* 4 - E\n X\n E\n
* 5 - F\n Y\n F\n
* 6 -         G\n
* 7           H\n
* 8
* 
* This should really generate statistics that give the lines added of 2
* If taken directly it will give lines added of 2+4 and lines removed of 2
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
 		git.add("file1", "ABC")
 			.commit("commit1")
 			.branch("branch1")
 			.checkout("master")
 			.modify("file1", "A\nB\nC\nD\nX\nY\n")
 			.commit("commit2")
 			.checkout("branch1")
 			.modify("file1", "A\nB\nC\nD\nE\nF\n");
 		RevCommit commit3 = git.commit("commit3").get();
 		git.checkout("master");
 		git.merge(commit3);
		git.modify("file1", "A\nB\nC\nD\nE\nF\nG\nH\n");
		RevCommit res = git.commit("Resolved").get();
		execute(res, callback);
		assertThat(callback.getFilesChanged().size(), equalTo(1));
		assertThat(callback.getFilesChanged().get(0).getEdits().size(), equalTo(2));
	}
	
	/**
	 * Test merge from two parents where we've modified against both branches
	 * and lengths are different
	 * 
	 * Expect that we get two change sets, one against each parent
	 * 
	 * So one file changed with EditList[DELETE(1-2,1-1), REPLACE(5-9,4-9),
	 * REPLACE(10-11,10-12)] EditList[INSERT(6-6,6-8), REPLACE(7-10,9-10),
	 * INSERT(11-11,11-12)]
	 * 
	 * Lines are
	 * 
	 * 0 - A\n A\n A\n 1 - B\n T\n B\n 2 - C\n B\n C\n 3 - D\n C\n D\n 4 - E\n
	 * D\n E\n 5 - F\n U\n F\n 6 - \n V\n G\n 7 \n W\n H\n 8 \n X\n \n 9 \n Y\n
	 * Y\n 10 K\n Z\n K\n 11 L\n
	 * 
	 * If taken directly it will give lines added of (0+5+2)+(2+1+1) and lines
	 * removed of (1+4+1)+(0+3+0) Added 11 and removed 9 (by edits relative to
	 * each branch) Overlaps - Added 3 lines (6,7,11) and removed 2 (7, 8)
	 * (based on one side being accepted for some regions) Line total would be
	 * +1 (commit1) +10 (commit2) +10 (commit3) +1 (merge) Real Lines would be
	 * 1, 11, 11, 12
	 * 
	 * master - merge | \ commit2 (~file1) branch1 - commit3 (~file1) | /
	 * commit1 (+file1)
	 * 
	 * Expect single file changed
	 */
	@Test
	public void testMergeCommitWithModificationsAgainstMultipleParentsLong() throws Exception {
		SimpleFileChangeCallback callback = new SimpleFileChangeCallback();
		git.add("file1", "ABC");
		git.commit("commit1");
		git.branch("branch1");
		git.checkout("master");
		git.modify("file1", "A\nT\nB\nC\nD\nU\nV\nW\nX\nY\nZ\n");
		git.commit("commit2");
		git.checkout("branch1");
		git.modify("file1", "A\nB\nC\nD\nE\nF\n\n\n\n\nK\n");
		RevCommit commit3 = git.commit("commit3").get();
		git.checkout("master");
		git.merge(commit3);
		git.modify("file1", "A\nB\nC\nD\nE\nF\nG\nH\n\nY\nK\nL\n");
		RevCommit res = git.commit("Resolved").get();
		execute(res, callback);
		assertThat(callback.getFilesChanged().size(), equalTo(1));
		assertThat(callback.getFilesChanged().get(0).getEdits().size(),equalTo(2));
		assertThat(callback.getFilesChanged().get(0).getEdits().get(0).getEdits().size(), equalTo(3));
		assertThat(callback.getFilesChanged().get(0).getEdits().get(1).getEdits().size(), equalTo(3));
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
		assertThat(callback.getFilesChanged().get(0).getEdits().get(0).getChangeType(), equalTo(ChangeType.RENAME));
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
		assertThat(callback.getFilesChanged().get(0).getEdits().get(0).getChangeType(), equalTo(ChangeType.RENAME));
		assertThat(callback.getFilesChanged().get(1).getEdits().get(0).getChangeType(), equalTo(ChangeType.COPY));
	}
	
	
	/**
	 * Arguably correct/incorrect as far as statistics go
	 * 
	 * File change will get picked up as merge commit changed relative to all other trees.
	 * 
	 * Diff output below when looking at merge commit. There were changes from the merge that were
	 * different relative to different parents
	 * 
	 * 1 file has changed as part of this commit
	 * 2 edit regions as part of this commit
	 * 
	 * 
	------------------------------------ file1 ------------------------------------
	index 00b7376,ea2eeee..5caa820
	@@@ -1,9 -1,9 +1,12 @@@
	+ X
	+ Y
	+ Z
	  A
	  B
	  C
	  D
	  E
	  F
	 +G
	 +H
	 +I
	 **/
	@Test
 	public void testMergeCommitWithModificationsInDifferentRegions() throws Exception {
 		SimpleFileChangeCallback callback = new SimpleFileChangeCallback();
 		git.add("file1", "A\nB\nC\nD\nE\nF\n");
 		git.commit("commit1");
 		git.branch("branch1");
 		git.checkout("master");
 		git.modify("file1", "A\nB\nC\nD\nE\nF\nG\nH\nI\n");
 		git.commit("commit2");
 		git.checkout("branch1");
 		git.modify("file1", "X\nY\nZ\nA\nB\nC\nD\nE\nF\n");
 		RevCommit commit3 = git.commit("commit3").get();
 		git.checkout("master");
 		MergeResult result = git.merge(commit3).get();
 		execute(CommitUtils.getCommit(repo, result.getNewHead()), callback);
 		assertThat(CommitUtils.getCommit(repo, result.getNewHead()).getParentCount(), equalTo(2));
 		assertThat(callback.getFilesChanged().size(), equalTo(1));
 		List<FileEdits> edits = callback.getChanges().getChanges().get(0).getEdits();
 		assertThat(edits.size(), equalTo(2));
 		assertThat(edits.get(0).getEdits().get(0).getBeginB(), equalTo(0));
 		assertThat(edits.get(0).getEdits().get(0).getEndB(), equalTo(3));
 		assertThat(edits.get(1).getEdits().get(0).getBeginB(), equalTo(9));
 		assertThat(edits.get(1).getEdits().get(0).getEndB(), equalTo(12));
	}
}
