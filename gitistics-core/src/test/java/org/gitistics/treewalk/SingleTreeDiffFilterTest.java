package org.gitistics.treewalk;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.gitective.core.CommitUtils;
import org.gitective.core.TreeUtils;
import org.gitistics.test.AbstractGitTest;
import org.hamcrest.Matchers;
import org.junit.Test;

public class SingleTreeDiffFilterTest extends AbstractGitTest {
	

	/**
	 * commit1 has nothing
	 * commit2 has the the new file 'file1'.
	 * 
	 * so comparison with a parent which doesn't have the file should include the 
	 * walk over 'file1'
	 * 
	 * It does not matter which tree we choose for the comparison
	 * 
	 */
	@Test
	public void testSingleParentWithSingleFile() throws Exception {
		RevCommit commit2 = git.commit("commit1")
				.add("file1", "A")
				.commit("commit2")
				.get();
		runSingleParentWithSingleFile(0, commit2);
		runSingleParentWithSingleFile(1, commit2);
	}
	
	private void runSingleParentWithSingleFile(int index, RevCommit commit) throws Exception {
		SingleTreeDiffFilter filterA = new SingleTreeDiffFilter(index);
		TreeWalk walk = TreeUtils.withParents(repo, commit);
		walk.setFilter(filterA);
		assertThat(walk.next(), equalTo(true));
		assertThat(walk.getPathString(), equalTo("file1"));
	}
	
	
	/**
	 * commit1 has the the new file 'file1'.
	 * commit2 is basically the same as commit1.
	 * 
	 * so comparison with a parent which doesn't have the file should not include the 
	 * walk over 'file1'
	 * 
	 * It does not matter which tree we choose for the comparison
	 * 
	 */
	@Test
	public void testSingleParentWithSingleFileReverse() throws Exception {
		RevCommit commit2 = git.add("file1", "A")
				.commit("commit1")
				.commit("commit2")
				.get();
		runSingleParentWithSingleFileReverse(0, commit2);
		runSingleParentWithSingleFileReverse(1, commit2);
	}
	
	
	@Test
	public void testSingleParentWithSingleFileRemoved() throws Exception {
		RevCommit commit2 = git.add("file1", "A")
				.commit("commit1")
				.remove("file1")
				.commit("commit2")
				.get();
		SingleTreeDiffFilter filterA = new SingleTreeDiffFilter(1);
		TreeWalk walk = TreeUtils.withParents(repo, commit2);
		walk.setFilter(filterA);
		assertThat(walk.next(), equalTo(true));
		assertThat(walk.getPathString(), equalTo("file1"));
	}
	
	/**
	 * master - merge
	 * |                \
	 * commit3 (+file3)   branch1 - commit2 (+file2)
	 * |                /
	 * commit1 (+file1)
     */
	@Test
	public void testMultipleParents() throws Exception {
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
		MergeResult merge = git.merge(commit3).get();
		
		SingleTreeDiffFilter filter = new SingleTreeDiffFilter(2);
		
		// The merge has nothing interesting
		TreeWalk walk = TreeUtils.withParents(repo, CommitUtils.getCommit(repo, merge.getNewHead()));
		walk.setFilter(filter);
		assertThat(walk.next(), equalTo(false));
		
		// The parents relative to each other will have +file2 or +file3
		// I.e. If commit3 is chosen we will get +file2 as +file2 is added
		// in both the other trees
		filter = new SingleTreeDiffFilter(1);
		walk = TreeUtils.withParents(repo, CommitUtils.getCommit(repo, merge.getNewHead()));
		walk.setFilter(filter);
		assertThat(walk.next(), equalTo(true));
		assertThat(walk.getPathString(), Matchers.anyOf(equalTo("file2"), equalTo("file3")));
		assertThat(walk.next(), equalTo(false));
		
	}
	
	
	public void runSingleParentWithSingleFileReverse(int index, RevCommit commit) throws Exception {
		SingleTreeDiffFilter filterA = new SingleTreeDiffFilter(index);
		TreeWalk walk = TreeUtils.withParents(repo, commit);
		walk.setFilter(filterA);
		assertThat(walk.next(), equalTo(false));
		
	}
}
