package org.gitistics.revwalk;

import java.io.IOException;

import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.StopWalkException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;

public class CommitRangeFilter extends RevFilter {

	private int start;
	
	private int end;
	
	private int position = 0;
	
	private boolean enabled = false;
	
	public CommitRangeFilter(int start, int end)  {
		this.start = start;
		this.end = end;
	}
	
	@Override
	public RevFilter clone() {
		return new CommitRangeFilter(start, end);
	}
	
	@Override
	public boolean include(RevWalk walker, RevCommit cmit)
			throws StopWalkException, MissingObjectException,
			IncorrectObjectTypeException, IOException {
		position++;
		if (position == start) {
			enabled = true;
		}
		if (position > end) {
			enabled = false;
		}
		return enabled;
	}
}
