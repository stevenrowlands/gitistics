package org.gitistics.visitor.commit.filechange;

import org.eclipse.jgit.diff.Edit;

public class LineChanges {

	private FileChange change;

	private int removed = 0;

	private int added = 0;

	public LineChanges(FileChange change) {
		this.change = change;
		count();
	}

	private void count() {
		for (FileEdits fileEdits : change.getEdits()) {
			for (Edit edit : fileEdits.getEdits()) {
				switch (edit.getType()) {
				case DELETE:
					// that is sequence B has removed the elements between
					// [beginA, endA).
					removed += edit.getEndA() - edit.getBeginA();
					break;
				case INSERT:
					// that is sequence B inserted the elements in region
					// [beginB, endB) at beginA.
					added += edit.getEndB() - edit.getBeginB();
					break;
				case REPLACE:
					// that is sequence B has replaced the range of elements
					// between [beginA, endA) with those found in [beginB,
					// endB).
					added += edit.getEndB() - edit.getBeginB();
					removed += edit.getEndA() - edit.getBeginA();
					break;
				case EMPTY:
					break;
				}
			}
		}
	}

	public int getRemoved() {
		return removed;
	}

	public int getAdded() {
		return added;
	}
}
