package lab.square.spltcoverage.core.model;

import java.util.Collection;

import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICounter;

/**
 * The TestMethodCoverage class is a class containing the coverage data of an atomic test that had run.
 * @author SQUARELAB
 *
 */

public class TestMethodCoverage {
	private Collection<IClassCoverage> classCoverages;
	private String testMethodName;
	private Class[] targetClasses;

	
	
	/**
	 * Create a new TestMethodCoverage.
	 * @param testMethodName	The name of the test method.
	 * @param classCoverages	The coverage data about a test method.
	 */
	public TestMethodCoverage(String testMethodName, Collection<IClassCoverage> classCoverages) {
		this.classCoverages = classCoverages;
		this.testMethodName = testMethodName;
	}
	
	/**
	 * Create a new TestMethodCoverage.
	 * @param testMethodName	The name of the test method to identify.
	 * @param classCoverages	The coverage data about a test method.
	 * @param targetClasses		The target class is used when decide equality.
	 * 							If the target class is not null,
	 * 							equals() checks if the target classes' coverage is the same only. 
	 */
	public TestMethodCoverage(String testMethodName, Collection<IClassCoverage> classCoverages, Class... targetClasses) {
		this(testMethodName, classCoverages);
		this.targetClasses = targetClasses;
	}

	/**
	 * Get coverage data of the specific class.
	 * @param className
	 * @return
	 */
	public IClassCoverage getCoverage(String className) {
		for (IClassCoverage cc : classCoverages) {
			if (className.equals(cc.getName())) {
				return cc;
			}
		}
		return null;
	}

	/**
	 * Get all the coverage data.
	 * @param className
	 * @return
	 */
	public Collection<IClassCoverage> getCoverages() {
		return classCoverages;
	}

	/**
	 * Get atomic test's name.
	 * @return
	 */
	public String getMethodName() {
		return testMethodName;
	}
	
	/**
	 * Set target classes.
	 * The target class is used when decide equality.
	 * If the target class is not null,
	 * equals() checks if the target classes' coverage is the same only. 
	 * 
	 * @param classes
	 */
	public void setTargetClasses(Class... classes) {
		targetClasses = classes;
	}

	@Override
	public int hashCode() {
		if(targetClasses == null)
			return super.hashCode();
		
		int hash = 0;
		final int prime = 7;
		
		for (Class klass : targetClasses) {
			for (IClassCoverage cc : classCoverages) {
				if(klass.getCanonicalName().replace(".", "/").equals(cc.getName())) {
					for(int i = cc.getFirstLine(); i <= cc.getLastLine(); i++) {
						if(cc.getLine(i).getStatus() == ICounter.FULLY_COVERED)
							hash += i;
					}
					break;
				}
			}
		}
		hash *= prime;
		hash += testMethodName.hashCode();
		
		return hash;
	}

	@Override
	public boolean equals(Object obj){
		if(targetClasses == null)
			return false;		
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
		TestMethodCoverage compareTo;
		if (!(obj instanceof TestMethodCoverage))
			return false;

		compareTo = (TestMethodCoverage) obj;

		if (!testMethodName.equals(compareTo.getMethodName()))
			return false;

		for (Class klass : classes) {
			for (IClassCoverage cc : compareTo.getCoverages()) {
				if(klass.getCanonicalName().replace(".", "/").equals(cc.getName())) {
					for(int i = cc.getFirstLine(); i <= cc.getLastLine(); i++) {
						if(cc.getLine(i).getStatus() != this.getCoverage(cc.getName()).getLine(i).getStatus())
							return false;
					}
					break;
				}
			}
		}

		return true;
	}

	/**
	 * Get the target classes.
	 * @return
	 */
	public Class[] getTargetClasses() {
		return targetClasses;
	}
}
