package org.gitistics.visitor.commit;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.gitistics.visitor.commit.filechange.FileChangeCallback;

public class TreeWalkVisitorImpl implements CommitVisitor {

	private TreeWalkVisitorRoot root;
	
	private TreeWalkVisitorStandard standard;
	
	public TreeWalkVisitorImpl(Repository repository, FileChangeCallback... fileChangeCallbacks) {
		this.root = new TreeWalkVisitorRoot(repository, fileChangeCallbacks);
		this.standard = new TreeWalkVisitorStandard(repository, fileChangeCallbacks);
	}
	
	public void visit(RevCommit commit) {
		root.visit(commit);
		standard.visit(commit); 
	}
	
}
