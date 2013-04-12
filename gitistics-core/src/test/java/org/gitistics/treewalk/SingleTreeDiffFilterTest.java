package org.gitistics.treewalk;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.gitective.core.TreeUtils;
import org.gitistics.AbstractGitTest;
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
	
	
	public void runSingleParentWithSingleFileReverse(int index, RevCommit commit) throws Exception {
		SingleTreeDiffFilter filterA = new SingleTreeDiffFilter(index);
		TreeWalk walk = TreeUtils.withParents(repo, commit);
		walk.setFilter(filterA);
		assertThat(walk.next(), equalTo(false));
		
	}
}
