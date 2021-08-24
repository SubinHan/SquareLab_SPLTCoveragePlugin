package lab.square.spltcoverage.core.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.jacoco.core.analysis.IClassCoverage;

/**
 * The ProductCoverage class is a class containing the coverage data of a product that had run by the test cases.
 * @author SQUARELAB
 */
public class ProductCoverage {
	private Collection<IClassCoverage> classCoverages;
	private Collection<TestCaseCoverage> testCaseCoverages;
	private Map<String, Boolean> featureSet;
	private Class[] targetClasses;
	
	/**
	 * Create an empty ProductCoverage.
	 * @param featureSet
	 */
	public ProductCoverage(Map<String, Boolean> featureSet) {
		this.featureSet = featureSet;
		this.testCaseCoverages = new HashSet<TestCaseCoverage>();
		this.classCoverages = new HashSet<IClassCoverage>();
	}
	
	/**
	 * Create an empty ProductCoverage and set the targetClasses.
	 * @param featureSet		The feature set of the product, to identify.
	 * @param targetClasses		The target class is used when decide equality.
	 * 							If the target class is not null,
	 * 							equals() checks if the target classes' coverage is the same only. 
	 */
	public ProductCoverage(Map<String, Boolean> featureSet, Class... targetClasses) {
		this(featureSet);
		this.targetClasses = targetClasses;
	}
	
	/**
	 * Get the TestCaseCoverage of the given test case name.
	 * @param testCaseName
	 * @return
	 */
	public TestCaseCoverage getTestCaseCoverage(String testCaseName) {
		for(TestCaseCoverage tcc : this.testCaseCoverages) {
			if(tcc.getTestCaseName().equals(testCaseName))
				return tcc;
		}
		return null;
	}
	
	/**
	 * Get all the TestCaseCoverages.
	 * @return
	 */
	public Collection<TestCaseCoverage> getTestCaseCoverages() {
		return this.testCaseCoverages;
	}
	
	/**
	 * Get the feature set.
	 * @return
	 */
	public Map<String, Boolean> getFeatureSet(){
		return this.featureSet;
	}
	
	/**
	 * Get the class coverages of the product.
	 * @return
	 */
	public Collection<IClassCoverage> getClassCoverages() {
		return this.classCoverages;
	}
	
	public int getScore() {
		int score = 0;
		
		for(TestCaseCoverage tcc : testCaseCoverages) {
			score += tcc.getScore();
		}
		
		return score;
	}
	
	/**
	 * Add the TestCaseCoverage.
	 * @param testCaseCoverage
	 */
	public void addTestCaseCoverage(TestCaseCoverage testCaseCoverage) {
		this.testCaseCoverages.add(testCaseCoverage);
	}
	
	/**
	 * Add the IClassCoveerages.
	 * It must be derived by the test cases' coverage containing.
	 * @param classCoverages
	 */
	public void addClassCoverages(Collection<IClassCoverage> classCoverages) {
		this.classCoverages.addAll(classCoverages);
	}
	
	/**
	 * Get the target classes.
	 * @return
	 */
	public Class[] getTargetClasses() {
		return targetClasses;
	}

	/**
	 * Set the target classes.
	 * The target class is used when decide equality.
	 * If the target class is not null,
	 * equals() checks if the target classes' coverage is the same only. 
	 * @param targetClasses
	 */
	public void setTargetClasses(Class[] targetClasses) {
		this.targetClasses = targetClasses;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		for(TestCaseCoverage tcc : testCaseCoverages) {
			hash += tcc.hashCode();
		}
		
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) throws IllegalStateException {
		if(targetClasses == null)
			return justEquals(obj);
		
		return equals(obj, targetClasses);
	}
	
	/**
	 * Checks equality within only given classes.
	 * You can use also setTargetClasses() and equals().
	 * @param obj
	 * @param classes
	 * @return
	 */
	public boolean equals(Object obj, Class... classes) {
		ProductCoverage compareTo;
		if (!(obj instanceof ProductCoverage))
			return false;

		compareTo = (ProductCoverage) obj;
		
		for(TestCaseCoverage tcc : testCaseCoverages) {
			if(!tcc.equals(compareTo.getTestCaseCoverage(tcc.getTestCaseName()), classes))
				return false;
		}
		
		return true;
	}
	
	public boolean justEquals(Object obj) {
		return this == obj;
	}
}
