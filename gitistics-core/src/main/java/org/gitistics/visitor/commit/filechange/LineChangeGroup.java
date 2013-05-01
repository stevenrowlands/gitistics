package org.gitistics.visitor.commit.filechange;

public class LineChangeGroup {

	private FileChanges changes;

	private int removed = 0;

	private int added = 0;

	public LineChangeGroup(FileChanges changes) {
		this.changes = changes;
		count();
	}

	private void count() {
		for (FileChange change : changes.getChanges()) {
			LineChanges lc = change.getLineChanges();
			added = lc.getAdded();
			removed = lc.getRemoved();
		}
	}

	public int getRemoved() {
		return removed;
	}

	public int getAdded() {
		return added;
	}
}
