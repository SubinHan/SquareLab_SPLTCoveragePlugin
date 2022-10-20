package lab.square.spltcoverage.model;

import java.util.Collection;
import java.util.HashSet;

import org.jacoco.core.analysis.IClassCoverage;

/**
 * The ProductCoverage class is a class containing the coverage data of a product that had run by the test cases.
 * @author SQUARELAB
 */
public class ProductCoverage implements ICoverageModelComposite {
	private Collection<IClassCoverage> classCoverages;
	private Collection<ICoverageModelComponent> testCaseCoverages;
	private FeatureSet featureSet;
	private String productName;
	private Class[] targetClasses;
	
	/**
	 * Create an empty ProductCoverage.
	 * @param featureSet
	 */
	public ProductCoverage(FeatureSet featureSet) {
		this(featureSet, "");
	}
	
	public ProductCoverage(FeatureSet featureSet, String name) {
		this.featureSet = featureSet;
		this.testCaseCoverages = new HashSet<>();
		this.classCoverages = new HashSet<>();
		this.productName = name;
	}
	
	/**
	 * Create an empty ProductCoverage and set the targetClasses.
	 * @param featureSet		The feature set of the product, to identify.
	 * @param targetClasses		The target class is used when decide equality.
	 * 							If the target class is not null,
	 * 							equals() checks if the target classes' coverage is the same only. 
	 */
	public ProductCoverage(FeatureSet featureSet, Class... targetClasses) {
		this(featureSet);
		this.targetClasses = targetClasses;
	}
	
	public String getName() {
		return this.productName;
	}
	
	/**
	 * Get the TestCaseCoverage of the given test case name.
	 * @param testCaseName
	 * @return
	 */
	public ICoverageModelComponent getTestCaseCoverage(String testCaseName) {
		for(ICoverageModelComponent tcc : this.testCaseCoverages) {
			if(tcc.getName().equals(testCaseName))
				return tcc;
		}
		return null;
	}
	
	/**
	 * Get all the TestCaseCoverages.
	 * @return
	 */
	public Collection<ICoverageModelComponent> getChildren() {
		return this.testCaseCoverages;
	}
	
	/**
	 * Get the feature set.
	 * @return
	 */
	public FeatureSet getFeatureSet(){
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
		
		for(ICoverageModelComponent tcc : testCaseCoverages) {
			score += tcc.getScore();
		}
		
		return score;
	}
	
	/**
	 * Add the TestCaseCoverage.
	 * @param testCaseCoverage
	 */
	public void addChild(ICoverageModelComponent testCaseCoverage) {
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
	public void setTargetClasses(Class... targetClasses) {
		this.targetClasses = targetClasses;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		for(ICoverageModelComponent tcc : testCaseCoverages) {
			hash += tcc.hashCode();
		}
		
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) throws IllegalStateException {
		if(targetClasses == null) {	
			String[] classNames = new String[classCoverages.size()];
			int i = 0;
			for(IClassCoverage cc : classCoverages) {
				classNames[i++] = cc.getName();
			}
			
			return equals(obj, classNames);
		}
		
		String[] classNames = new String[targetClasses.length];
		int i = 0;
		for(Class klass : targetClasses) {
			classNames[i++] = klass.getCanonicalName().replace(".", "/");
		}
		
		return equals(obj, classNames);
	}
	
	/**
	 * Checks equality within only given classes.
	 * You can use also setTargetClasses() and equals().
	 * @param obj
	 * @param classes
	 * @return
	 */
	public boolean equals(Object obj, String... classNames) {
		ProductCoverage compareTo;
		if (!(obj instanceof ProductCoverage))
			return false;

		compareTo = (ProductCoverage) obj;
		
		for(ICoverageModelComponent component : testCaseCoverages) {
			TestCaseCoverage tcc = (TestCaseCoverage) component;
			if(!tcc.equals(compareTo.getTestCaseCoverage(tcc.getName()), classNames))
				return false;
		}
		
		return true;
	}
	
	public boolean justEquals(Object obj) {
		return this == obj;
	}

	public int getProductCoverageStatusAtLineOfClass(int i, String classNameWithDots) {
		IClassCoverage classCoverage = findCoverageByClassNameWithDots(classNameWithDots);
		return classCoverage.getLine(i).getStatus();
	}

	private IClassCoverage findCoverageByClassNameWithDots(String classNameWithDots) {
		for(IClassCoverage classCoverage : classCoverages) {
			if(classCoverage.getName().replace('/', '.').equals(classNameWithDots))
				return classCoverage;
		}

		return null;
	}
}
