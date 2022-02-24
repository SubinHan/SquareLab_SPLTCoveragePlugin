package lab.square.spltcoverage.model;

import java.util.Collection;
import java.util.HashSet;

import org.jacoco.core.analysis.IClassCoverage;

/**
 * The TestCaseCoverage class is a class containing the coverage data of a test case that had run.
 * It has the TestMethodCoverages.
 * @author SQUARELAB
 *
 */
public class TestCaseCoverage implements ICoverageModelComposite {
	private Collection<IClassCoverage> classCoverages;
	private Collection<ICoverageModelComponent> testMethodCoverages;
	private String testCaseName;
	private Class[] targetClasses;
	
	/**
	 * Create an empty TestCaseCoverage.
	 * @param testCaseName
	 */
	public TestCaseCoverage(String testCaseName) {
		testMethodCoverages = new HashSet<ICoverageModelComponent>();
		classCoverages = new HashSet<IClassCoverage>();
		this.testCaseName = testCaseName;
	}
	
	/**
	 * Create an empty TestCaseCoverage and set the targetClasses.
	 * @param testCaseName		The name of the test case to identify.
	 * @param targetClasses		The target class is used when decide equality.
	 * 							If the target class is not null,
	 * 							equals() checks if the target classes' coverage is the same only. 
	 */
	public TestCaseCoverage(String testCaseName, Class... targetClasses) {
		this(testCaseName);
		this.targetClasses = targetClasses;
	}
	
	/**
	 * Get the TestMethodCoverage of the given test method name.
	 * @param testMethodName
	 * @return
	 */
	public ICoverageModelComponent getTestMethodCoverage(String testMethodName) {
		for(ICoverageModelComponent tmc : testMethodCoverages) {
			if(tmc.getName().equals(testMethodName))
				return tmc;
		}
		return null;
	}
	
	/**
	 * Get all the TestMethodCoverages.
	 * @return
	 */
	public Collection<ICoverageModelComponent> getChildren(){
		return testMethodCoverages;
	}
	
	/**
	 * Get the test case name.
	 * @return
	 */
	public String getName() {
		return testCaseName;
	}
	
	/**
	 * Add the TestMethodCoverage.
	 * @param testMethodCoverage
	 */
	public void addChild(ICoverageModelComponent testMethodCoverage) {
		this.testMethodCoverages.add(testMethodCoverage);
	}

	/**
	 * Add the IClassCoveerages.
	 * It must be derived by the test methods' coverage containing.
	 * @param classCoverages
	 */
	public void addClassCoverages(Collection<IClassCoverage> classCoverages) {
		this.classCoverages.addAll(classCoverages);
	}
	
	/**
	 * Get the target classes.
	 * 
	 * @return
	 */
	public Class[] getTargetClasses() {
		return targetClasses;
	}
	
	/**
	 * Get the class coverages of the test case.
	 * @return
	 */
	public Collection<IClassCoverage> getClassCoverages() {
		return this.classCoverages;
	}

	/**
	 * Set target classes.
	 * The target class is used when decide equality.
	 * If the target class is not null,
	 * equals() checks if the target classes' coverage is the same only. 
	 * 
	 * @param targetClasses
	 */
	public void setTargetClasses(Class[] targetClasses) {
		this.targetClasses = targetClasses;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		for(ICoverageModelComponent tmc : testMethodCoverages) {
			hash += tmc.hashCode();
		}
		hash += testCaseName.hashCode();
		
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
	 * @param classNames
	 * @return
	 */
	public boolean equals(Object obj, String... classNames) {
		TestCaseCoverage compareTo;
		if (!(obj instanceof TestCaseCoverage))
			return false;

		compareTo = (TestCaseCoverage) obj;
		
		if(!testCaseName.equals(compareTo.testCaseName))
			return false;
		
		for(ICoverageModelComponent component : testMethodCoverages) {
			TestMethodCoverage tmc = (TestMethodCoverage)component;
			if(!tmc.equals(compareTo.getTestMethodCoverage(tmc.getName()), classNames))
				return false;
		}
		
		return true;
	}

	public int getScore() {
		int score = 0;
		
		for(ICoverageModelComponent tmc : testMethodCoverages) {
			score += tmc.getScore();
		}
		
		return score;
	}
	
}
