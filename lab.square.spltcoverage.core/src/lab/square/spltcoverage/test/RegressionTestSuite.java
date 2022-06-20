package lab.square.spltcoverage.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({CoverageGeneratorTest2.class, CoverageReaderTest.class, LinkerTest2.class, 
	FeatureSetGroupReaderTest.class, SplCoverageReaderTest.class, AntennaCoverageGeneratorTest.class, FeatureLocatorTest.class})
public class RegressionTestSuite {

}
