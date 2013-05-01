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
import org.gitistics.visitor.commit.filechange.FileChanges;

public class TreeWalkVisitorStandard extends AbstractCommitVisitor {

	private DiffFormatter formatter;
	
	private Repository repository;
	
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
		
		FileChanges changes = new FileChanges(commit);
		for (RevCommit parent : commit.getParents()) {
			List<DiffEntry> entries = diffCommitWithParent(commit, parent);
			for (DiffEntry e : entries) {
				FileChange change = handleFileChangeCallbacks(commit, parent, e);
				changes.addChange(change);
			}
		}
		callback(changes);
	}
	
	private List<byte []> getRawBytes(RevCommit commit) {
		TreeWalk walk = TreeWalkUtils.treeWalkForCommit(repository, commit);
		List<byte []> rawBytes = new ArrayList<byte[]>();
		try {
			while (walk.next()) {
				rawBytes.add(walk.getRawPath());
			}
			return rawBytes;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			walk.release();
		}
	}

	private List<DiffEntry> diffCommitWithParent(RevCommit commit, RevCommit parent) {
		try {
			formatter.setContext(0);
			return formatter.scan(parent, commit);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			formatter.release();
		}
	}

	private FileChange handleFileChangeCallbacks(RevCommit commit, RevCommit parent, DiffEntry entry)  {
		EditList edits = handleEdits(commit, parent, entry);
		FileChange change = new FileChange();
		change.setChangeType(entry.getChangeType());
		if (entry.getNewPath() == null) // deleted
			change.setPath(entry.getOldPath());
		else 
			change.setPath(entry.getNewPath());
		change.setCommit(commit);
		change.setEdits(edits);
		change.setParent(parent);
		return change;
	}

	private EditList handleEdits(RevCommit commit, RevCommit parent, DiffEntry entry) {
		try {
			formatter.setContext(0);
			FileHeader h = formatter.toFileHeader(entry);
			return h.toEditList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
