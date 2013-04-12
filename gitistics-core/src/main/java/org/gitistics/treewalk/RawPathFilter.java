package org.gitistics.treewalk;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.jgit.treewalk.TreeWalk;

/**
 * Selects only tree entries where the raw path matches one of the specified raw paths
 */
public class RawPathFilter extends AbstractTreeFilter {

	private List<byte[]> rawPaths;

	public RawPathFilter(byte[] rawPath) {
		this(new ArrayList<byte []>());
		rawPaths.add(rawPath);
	}
	
	public RawPathFilter(List<byte[]> rawPaths) {
		this.rawPaths = rawPaths;
	}

	@Override
	public boolean include(final TreeWalk walker) {
		for (int i = 0; i < walker.getTreeCount(); i++) {
			for (byte[] b : rawPaths) {
				if (ArrayUtils.isEquals(b, walker.getRawPath())) {
					return true;
				}
			}
		}
		return false;
	}
}
