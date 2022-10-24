package lab.square.spltcoverage.test.antennatarget;

import java.util.Arrays;
import java.util.Collection;

import lab.square.spltcoverage.core.analysis.IProductProvider;
import lab.square.spltcoverage.test.TestConfig;

public class TestProductProvider implements IProductProvider {

	private static final String CLASS_A_TEST = TestConfig.ANTENNA_PRODUCT1_TEST_A_CLASSPATH_NESTED;
	private static final String CLASS_B_TEST = TestConfig.ANTENNA_PRODUCT1_TEST_B_CLASSPATH_NESTED;
	
	@Override
	public Collection<String> getTestClassPaths() {
		return Arrays.asList(CLASS_A_TEST,
				CLASS_B_TEST);
	}

	@Override
	public String getClasspath() {
		return TestConfig.CLASSPATH_SELF;
	}
 
	@Override
	public String getOutputPath() {
		return "testResources/testOutput/GeneratedAntennaCoverages/";
	}

}
