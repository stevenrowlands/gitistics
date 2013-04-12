package org.gitistics.treewalk;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.gitective.core.TreeUtils;

public class TreeWalkUtils {

	public static TreeWalk treeWalkForCommit(Repository repository, RevCommit commit) {
		TreeWalk walk = TreeUtils.withParents(repository, commit);
		if (commit.getParentCount() == 0) {
			walk.setFilter(new SingleTreeDiffFilter(0));
		} else {
			walk.setFilter(new SingleTreeDiffFilter(commit.getParentCount() - 1));
		}
		walk.setRecursive(true);
		return walk;
	}
}
