package lab.square.spltcoverage.core.analysis;

import lab.square.spltcoverage.model.FeatureSet;

public interface IIterableSpltProvider {
	/**
	 * It will be called the generator's test has done.
	 * 
	 * @return
	 */
	public boolean makeNextProduct();
	
	/**
	 * It should returns the feature set of iterating product.
	 * @return
	 */
	public FeatureSet getFeatureSet();
	
	/**
	 * It should return the classes to trace coverage.
	 * @return
	 */
	public Class[] getTargetClasses();
	
	/**
	 * It should return the classes to test.
	 * @return
	 */
	public Class[] getTestClasses();
	
	/**
	 * It should return the directory path to store SPL coverage.
	 * For Example: "D:/MySplCoverage"
	 * @return
	 */
	public String getBaseDirectory();
}
