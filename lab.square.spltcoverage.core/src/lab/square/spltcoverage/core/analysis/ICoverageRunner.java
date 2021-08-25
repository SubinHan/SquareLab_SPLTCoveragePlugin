package lab.square.spltcoverage.core.analysis;

import java.util.Map;

public interface ICoverageRunner {
	public boolean onMakeNextProduct();
	public Map<String, Boolean> onGetFeatureSet();
	public Class[] onGetTargetClasses();
	public Class[] onGetTestClasses();
	public String onGetBaseDirectory();
	public String onGetProductDirectory();
	public String onGetTestCaseDirectory();
	public String onGetTestMethodDirectory();
}
