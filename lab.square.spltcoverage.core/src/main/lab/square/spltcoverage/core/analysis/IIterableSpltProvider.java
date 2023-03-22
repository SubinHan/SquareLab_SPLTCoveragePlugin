package lab.square.spltcoverage.core.analysis;

import lab.square.spltcoverage.model.FeatureSet;

/**
 * PLCoCo doesn't know how your PL project works, so you should provide
 * interface implementation to test your PL automatically using PLCoCo.
 * 
 * @author selab
 *
 */
public interface IIterableSpltProvider {
	/**
	 * It will be called when the test of each product has been done. You should
	 * make the next product by configuring runtime variables. It should iterate all
	 * of your products to test.
	 * 
	 * @return false when there are no next products.
	 */
	public boolean makeNextProduct();

	/**
	 * Returns a feature set of current product.
	 * 
	 * @return
	 */
	public FeatureSet getFeatureSet();

	/**
	 * Returns classes that you are interested in, about coverage. It may be
	 * implementation classes of the products.
	 * 
	 * @return
	 */
	public Class[] getTargetClasses();

	/**
	 * Returns test classes that will be executed by PLCoCo. It should be unit tests
	 * using JUnit.
	 * 
	 * @return
	 */
	public Class[] getTestClasses();

	/**
	 * Returns output directory path that will be stored execution files. For
	 * example, "D:/MySplCoverage". PLCoCo doesn't check the integrity of the path,
	 * so be careful about path string form.
	 * 
	 * @return
	 */
	public String getBaseDirectory();
}
