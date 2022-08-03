package lab.square.spltcoverage.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class ProductSourceInfo {
	public final String testClasspathRoot;
	public final Collection<String> testClassPaths;
	public final Map<String, Boolean> featureSet;
	public final Collection<String> additionalDependencies;
	
	public ProductSourceInfo(String classpath, Collection<String> testClassPaths, Map<String, Boolean> featureSet) {
		this(classpath, testClassPaths, featureSet, Collections.emptyList());
	}	
	
	public ProductSourceInfo(String classpath, Collection<String> testClassPaths, Map<String, Boolean> featureSet, Collection<String> additionalDependencies) {
		this.testClasspathRoot = classpath;
		this.testClassPaths = testClassPaths;
		this.featureSet = featureSet;
		this.additionalDependencies = additionalDependencies;
	}	
	
	
}
