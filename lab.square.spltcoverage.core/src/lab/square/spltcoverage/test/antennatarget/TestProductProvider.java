package lab.square.spltcoverage.test.antennatarget;

import java.util.Arrays;
import java.util.Collection;

import lab.square.spltcoverage.core.analysis.IProductProvider;

public class TestProductProvider implements IProductProvider {

	@Override
	public Collection<String> getTestClassPaths() {
		return Arrays.asList("bin/lab/square/spltcoverage/test/antennatarget/ClassATest.class",
				"bin/lab/square/spltcoverage/test/antennatarget/ClassBTest.class");
	}

	@Override
	public String getClasspath() {
		return "src/";
	}
 
	@Override
	public String getOutputPath() {
		return "testResources/GeneratedAntennaCoverages/";
	}

}
