package org.gitistics.visitor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.gitistics.AbstractGitTest;
import org.gitistics.visitor.commit.CommitVisitor;
import org.junit.Test;

public class CommitWalkerTest extends AbstractGitTest {
	
	@Test
	public void testCommitWalkerSingleCommit() throws Exception {
		RevCommit initial = git.commit("commit1").get();
		CommitVisitor visitor = visitor();
		execute(initial, visitor);
		verify(visitor).visit(initial);
	}
	
	@Test
	public void testCommitWalkerSingleCommitMultipleVisitors() throws Exception {
		RevCommit initial = git.commit("commit1").get();
		CommitVisitor v1 = visitor();
		CommitVisitor v2 = visitor();
		execute(initial, v1, v2);
		verify(v1).visit(initial);
		verify(v2).visit(initial);
	}
	
	
	@Test
	public void testCommitWalkerMultipleCommit() throws Exception {
		RevCommit initial = git.commit("commit1").get();
		RevCommit second = git.commit("commit2").get();
		CommitVisitor visitor = visitor();
		execute(second, visitor);
		verify(visitor).visit(initial);
		verify(visitor).visit(second);
	}
	
	
	private CommitVisitor visitor() {
		return mock(CommitVisitor.class);
	}
	
	private void execute(RevCommit initial, CommitVisitor...visitors) {
		try {
			CommitWalker walker = new CommitWalker(visitors);
			RevWalk walk = new RevWalk(repo);
			walk.markStart(initial);
			walker.walk(walk);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
