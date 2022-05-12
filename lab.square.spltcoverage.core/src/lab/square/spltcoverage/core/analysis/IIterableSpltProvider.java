package lab.square.spltcoverage.core.analysis;

import java.util.Map;

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
	public Map<String, Boolean> getFeatureSet();
	
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
	
	/**
	 * It should return the relative directory to store product coverage files in [BaseDirectory].
	 * If nothing, then just return "/".
	 *  * The generator will generate products by the name '1', '2', '3', ... on the end of the return value.
	 * @return productDirectory
	 */
	public String getProductDirectory();
	
	/**
	 * It should return the relative directory to store test case coverage files in [ProductDirectory].
	 * If nothing, then just return "/".
	 * @return testMethodDirectory
	 */
	public String getTestCaseDirectory();
	
	/**
	 * It should return the relative directory to store test method coverage files in [testCaseDirectory].
	 * If nothing, then just return "/".
	 * @return testMethodDirectory
	 */
	public String getTestMethodDirectory();
}
