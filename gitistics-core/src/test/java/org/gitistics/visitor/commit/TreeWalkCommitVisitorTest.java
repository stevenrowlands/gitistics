package org.gitistics.visitor.commit;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.gitistics.test.AbstractGitTest;
import org.gitistics.visitor.commit.treewalk.TreeWalkVisitor;
import org.hamcrest.Matchers;
import org.junit.Test;

public class TreeWalkCommitVisitorTest extends AbstractGitTest {
	
	@Test
	public void testCommitWalkerEmptyCommit() throws Exception {
		RevCommit initial = git.commit("commit1").get();
		TreeWalkVisitor visitor = visitor();
		execute(visitor, initial);
		verifyZeroInteractions(visitor);
	}
	
	@Test
	public void testCommitWalkerMultipleCommits() throws Exception {
		RevCommit initial = git.add("A", "1").commit("commit1").get();
		RevCommit second = git.add("B", "1").commit("commit1").get();
		TreeWalkVisitor visitor = visitor();
		execute(visitor, initial, second);
		verify(visitor).visit(argThat(Matchers.equalTo(initial)), argThat(Matchers.any(TreeWalk.class)));
		verify(visitor).visit(argThat(Matchers.equalTo(second)), argThat(Matchers.any(TreeWalk.class)));
	}
	
	private TreeWalkVisitor visitor() {
		return mock(TreeWalkVisitor.class);
	}
	
	private void execute(TreeWalkVisitor treeWalkVisitor, RevCommit...commits) {
		TreeWalkCommitVisitor visitor = new TreeWalkCommitVisitor(repo, treeWalkVisitor);
		for (RevCommit commit : commits) {
			visitor.visit(commit);
		}
	}
}
