package org.gitistics.visitor;

import org.eclipse.jgit.revwalk.RevWalk;

public interface Walker {

	/**
	 * Given a RevWalk. Visit each commit
	 */
	public void walk(RevWalk walk);
	
}
