package lab.square.spltcoverage.model;

import java.util.Collection;
import java.util.Collections;

public class ProductSourceInfo {
	public final String classpath;
	public final Collection<String> testClassPaths;
	public final FeatureSet featureSet;
	public final Collection<String> additionalDependencies;
	
	public ProductSourceInfo(String classpath, Collection<String> testClassPaths, FeatureSet featureSet) {
		this(classpath, testClassPaths, featureSet, Collections.emptyList());
	}	
	
	public ProductSourceInfo(String classpath, Collection<String> testClassPaths, FeatureSet featureSet, Collection<String> additionalDependencies) {
		this.classpath = classpath;
		this.testClassPaths = testClassPaths;
		this.featureSet = featureSet;
		this.additionalDependencies = additionalDependencies;
	}	
	
	
}
