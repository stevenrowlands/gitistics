package org.gitistics.visitor.commit;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
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
import org.gitistics.visitor.commit.filechange.FileChangeCallback;
import org.gitistics.visitor.commit.filechange.FileChanges;
import org.gitistics.visitor.commit.filechange.FileEdits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TreeWalkVisitorStandard extends AbstractCommitVisitor {

	private static final Logger log = LoggerFactory.getLogger(TreeWalkVisitorStandard.class);
	
	private Repository repository;
	
	public TreeWalkVisitorStandard(Repository repository, FileChangeCallback... fileChangeCallbacks) {
		super(fileChangeCallbacks);
		this.repository = repository;
	}

	public void visit(RevCommit commit) {
		if (commit.getParentCount() == 0)
			return;

		DiffFormatter formatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
		formatter.setRepository(repository);
		formatter.setDiffComparator(RawTextComparator.DEFAULT);
		formatter.setDetectRenames(true);
		formatter.setPathFilter(new RawPathFilter(getRawBytes(commit)));
		
		FileChanges changes = new FileChanges(repository, commit);
		for (RevCommit parent : commit.getParents()) {
			List<DiffEntry> entries = diffCommitWithParent(formatter, commit, parent);
			for (DiffEntry e : entries) {
				String path = getPath(e);
				try {
					log.debug("processing {}", path);
					FileChange change = changes.getChange(path);
					if (change == null) {
						change = new FileChange();
						change.setPath(path);
						changes.addChange(change);
					}
					FileEdits edits = getEdits(formatter, commit, e);
					edits.setParent(parent);
					change.addEdit(edits);
				} catch (Throwable t) {
					log.error("unable to process {} for commit {}", path, commit.getId().getName());
				}
			}
		}
		callback(changes);
	}
	
	private String getPath(DiffEntry entry) {
		if (entry.getChangeType().equals(ChangeType.DELETE))
			return entry.getOldPath();
		else
			return entry.getNewPath();
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

	private List<DiffEntry> diffCommitWithParent(DiffFormatter formatter, RevCommit commit, RevCommit parent) {
		try {
			formatter.setContext(0);
			return formatter.scan(parent, commit);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			formatter.release();
		}
	}

	private FileEdits getEdits(DiffFormatter formatter, RevCommit commit, DiffEntry entry) {
		EditList edits = handleEdits(formatter, commit, entry);
		FileEdits fileEdits = new FileEdits();
		fileEdits.setChangeType(entry.getChangeType());
		fileEdits.setEdits(edits);
		return fileEdits;
	}

	private EditList handleEdits(DiffFormatter formatter, RevCommit commit, DiffEntry entry) {
		try {
			formatter.setContext(0);
			FileHeader h = formatter.toFileHeader(entry);
			return h.toEditList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
