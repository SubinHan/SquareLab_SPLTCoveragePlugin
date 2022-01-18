package lab.square.spltcoverage.model;

import java.util.Collection;
import java.util.Map;

public class ProductSourceInfo {
	public final String classpath;
	public final Collection<String> testClassPaths;
	public final Map<String, Boolean> featureSet;
	
	public ProductSourceInfo(String classpath, Collection<String> testClassPaths, Map<String, Boolean> featureSet) {
		this.classpath = classpath;
		this.testClassPaths = testClassPaths;
		this.featureSet = featureSet;
	}	
}
