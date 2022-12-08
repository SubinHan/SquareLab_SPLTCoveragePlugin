package lab.square.spltcoverage.test.target;

import java.util.ArrayList;
import java.util.Map;

import lab.square.spltcoverage.core.analysis.IIterableSpltProvider;
import lab.square.spltcoverage.model.FeatureSet;

public class TestSpltProvider implements IIterableSpltProvider {

	private int i;
	
	public TestSpltProvider() {
		i = 0;
	}
	
	@Override
	public boolean makeNextProduct() {
		ArrayList<Map<String, Boolean>> validConfigs = Configuration.ValidConfigs.validConfigs;
		
		if(i < validConfigs.size()) {
			Configuration.setConfiguration(validConfigs.get(i));
			i++;
			return true;
		}
		
		return false;
	}

	@Override
	public FeatureSet getFeatureSet() {
		return new FeatureSet(Configuration.getConfigurations());
	}

	@Override
	public Class[] getTargetClasses() {
		return new Class[] { ClassA.class, ClassB.class };
	}

	@Override
	public Class[] getTestClasses() {
		return new Class[] { ClassATest.class, ClassBTest.class };
	}

	@Override
	public String getBaseDirectory() {
		return "testResources/testOutput/GeneratedCoverages";
	}

}
