package org.gitistics.outlier;

import java.util.List;

public interface OutlierHandler {

	public List<?> outliers(OutlierParam param);
	
	public void toggleValid(String commitId);
}
