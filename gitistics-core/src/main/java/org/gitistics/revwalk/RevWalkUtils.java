package org.gitistics.revwalk;

import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;

public class RevWalkUtils {
	/** 
	 * Get a rev walk across all commits on all branches
	 */
	public static RevWalk newRevWalk(Repository repository) throws Exception {
 		Git git = new Git(repository);
 		List<Ref> branches = git.branchList().setListMode(ListMode.ALL).call();
 		RevWalk walk = new RevWalk(repository);
 		for (Ref ref : branches) {
 			ObjectId objectId = ref.getPeeledObjectId();
 			if (objectId == null)
 				objectId = ref.getObjectId();
 			walk.markStart(walk.lookupCommit(objectId));
 		}
		return walk;
	}
}
