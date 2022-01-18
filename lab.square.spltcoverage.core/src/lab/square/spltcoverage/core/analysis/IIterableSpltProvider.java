package lab.square.spltcoverage.core.analysis;

import java.util.Map;

public interface IIterableSpltProvider {
	public boolean makeNextProduct();
	public Map<String, Boolean> getFeatureSet();
	public Class[] getTargetClasses();
	public Class[] getTestClasses();
	public String getBaseDirectory();
	public String getProductDirectory();
	public String getTestCaseDirectory();
	public String getTestMethodDirectory();
}
