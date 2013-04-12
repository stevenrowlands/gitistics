package org.gitistics.visitor.commit;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.gitistics.treewalk.RawPathFilter;
import org.gitistics.treewalk.TreeWalkUtils;
import org.gitistics.visitor.commit.filechange.FileChange;

public class TreeWalkVisitorStandard extends AbstractCommitVisitor {

	private DiffFormatter formatter;
	
	private Repository repository;
	
	private List<byte []> rawBytes;
	
	public TreeWalkVisitorStandard(Repository repository) {
		this.repository = repository;
		this.formatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
		formatter.setRepository(repository);
		formatter.setDiffComparator(RawTextComparator.DEFAULT);
		formatter.setDetectRenames(true);
	}

	public void visit(RevCommit commit) {
		if (commit.getParentCount() == 0)
			return;

		formatter.setPathFilter(new RawPathFilter(getRawBytes(commit)));
		
		for (RevCommit parent : commit.getParents()) {
			diffCommitWithParent(commit, parent);
		}
	}
	
	private List<byte []> getRawBytes(RevCommit commit) {
		if (rawBytes == null) {
			TreeWalk walk = TreeWalkUtils.treeWalkForCommit(repository, commit);
			rawBytes = new ArrayList<byte[]>();
			try {
				while (walk.next()) {
					rawBytes.add(walk.getRawPath());
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				walk.release();
			}
		}
		return rawBytes;
	}

	private void diffCommitWithParent(RevCommit commit, RevCommit parent) {
		try {
			List<DiffEntry> entries = formatter.scan(parent, commit);
			for (DiffEntry e : entries) {
				handleFileChangeCallbacks(commit, e);
				handleEdits(commit, parent, e);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			formatter.release();
		}
	}

	private void handleFileChangeCallbacks(RevCommit commit, DiffEntry entry) {
		FileChange change = new FileChange();
		change.setChangeType(entry.getChangeType());
		change.setPath(entry.getNewPath());
		change.setCommit(commit);
		callback(change);
	}

	private void handleEdits(RevCommit commit, RevCommit parent, DiffEntry entry) {
		if (!hasEditVisitors())
			return;

		try {
			FileHeader h = formatter.toFileHeader(entry);
			EditList edits = h.toEditList();
			edit(commit, parent, edits);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
