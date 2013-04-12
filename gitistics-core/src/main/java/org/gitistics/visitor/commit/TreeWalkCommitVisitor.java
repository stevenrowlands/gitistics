package org.gitistics.visitor.commit;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.gitistics.treewalk.TreeWalkUtils;
import org.gitistics.visitor.commit.treewalk.TreeWalkVisitor;

/**
 * Given a commit, generates a TreeWalk over the objects that were modified as part of this commit
 * 
 * For merges, only include files that were modified in the actual merge (not from combining trees)
 * 
 * Defers to its composite TreeWalkVisitor's
 */
public class TreeWalkCommitVisitor implements CommitVisitor {
	
	private TreeWalkVisitor [] walkers;
	
	private Repository repository;
	
	public TreeWalkCommitVisitor(Repository repository, TreeWalkVisitor...walkers) {
		this.repository = repository;
		this.walkers = walkers;
	}
	
	public void visit(RevCommit commit) {
		TreeWalk walk = TreeWalkUtils.treeWalkForCommit(repository, commit);
		try {
			while (walk.next()) {
				visit(commit, walk);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			walk.release();
		}
	}
	
	private void visit(RevCommit commit, TreeWalk walk) {
		for (TreeWalkVisitor walker : walkers) {
			walker.visit(commit, walk);
		}
	}
}
