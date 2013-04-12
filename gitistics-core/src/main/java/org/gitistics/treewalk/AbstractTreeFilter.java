package org.gitistics.treewalk;

import org.eclipse.jgit.treewalk.filter.TreeFilter;

/**
 * Provides the common default implementations for TreeFilter
 */
public abstract class AbstractTreeFilter extends TreeFilter {
	
	@Override
	public boolean shouldBeRecursive() {
		return false;
	}

	@Override
	public TreeFilter clone() {
		return this;
	}
}
