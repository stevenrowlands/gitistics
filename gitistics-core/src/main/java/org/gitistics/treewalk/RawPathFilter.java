package org.gitistics.treewalk;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.treewalk.TreeWalk;

/**
 * Selects only tree entries where the raw path matches one of the specified raw
 * paths
 */
public class RawPathFilter extends AbstractTreeFilter {

	private List<byte[]> rawPaths;

	public RawPathFilter(byte[] rawPath) {
		this(new ArrayList<byte[]>());
		rawPaths.add(rawPath);
	}

	public RawPathFilter(List<byte[]> rawPaths) {
		this.rawPaths = rawPaths;
	}

	@Override
	public boolean include(final TreeWalk walker) {
		for (int i = 0; i < walker.getTreeCount(); i++) {
			for (byte[] b : rawPaths) {
				if (startsWith(walker.getRawPath(), b)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns true if a2 starts with a1
	 * 
	 * @param a1
	 * @param a2
	 * @return
	 */
	private boolean startsWith(byte[] a1, byte[] a2) {
		if (a1.length > a2.length)
			return false;
		
		for (int i = 0; i < a1.length; i++) {
			if (a1[i] != a2[i]) {
				return false;
			}
		}
		return true;
	}
}
