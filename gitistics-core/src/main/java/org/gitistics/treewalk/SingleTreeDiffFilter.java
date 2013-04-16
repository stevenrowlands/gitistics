package org.gitistics.treewalk;

import org.eclipse.jgit.treewalk.TreeWalk;

/**
 * Selects only tree entries where all other tree's differ from the single tree at the specified index
 * 
 * <p>
 * This is useful to see which entries were actually changed in a specific tree.
 * 
 * An example usage is for a merge commit in determining what paths were actually
 * modified as part of the merge and weren't just pulled in from a parent tree
 * <p>
 */
public class SingleTreeDiffFilter extends AbstractTreeFilter {

	private int index;
	
	public SingleTreeDiffFilter(int index) {
		this.index = index;
	}
	
	@Override
	public boolean include(final TreeWalk walker) {
		int n = walker.getTreeCount();
		if (n == 1) {
			return true;
		}

		final int m = walker.getRawMode(index);
		for (int i = 0; i < n; i++) {
			if (i == index) continue;
			if (walker.getRawMode(i) == m && walker.idEqual(i, index)) {
				return false;
			}
		}
		return true;
	}
}