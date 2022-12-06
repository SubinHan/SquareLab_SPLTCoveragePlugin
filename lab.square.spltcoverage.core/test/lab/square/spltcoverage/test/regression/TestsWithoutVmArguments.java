package lab.square.spltcoverage.test.regression;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import lab.square.spltcoverage.test.CoverageGeneratorLauncherTest;
import lab.square.spltcoverage.test.CoverageReaderTest;
import lab.square.spltcoverage.test.FeatureExpressionParserTest;
import lab.square.spltcoverage.test.FeatureIdeConfigReaderTest;
import lab.square.spltcoverage.test.FeatureSetGroupReaderTest;
import lab.square.spltcoverage.test.FeatureSetTest;
import lab.square.spltcoverage.test.LinkerTest;
import lab.square.spltcoverage.test.ProductNodeTest;
import lab.square.spltcoverage.test.SplCoverageGeneratorLauncherTest;
import lab.square.spltcoverage.test.SplCoverageReaderTest;
import lab.square.spltcoverage.test.antenna.AntennaCoverageAccumulatorTest;
import lab.square.spltcoverage.test.antenna.AntennaCoverageReaderTest;
import lab.square.spltcoverage.test.antenna.AntennaModelTest;
import lab.square.spltcoverage.test.antenna.FeatureLocatorTest;
import lab.square.spltcoverage.test.antenna.ProductFeatureLocationTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	CoverageGeneratorLauncherTest.class,
	CoverageReaderTest.class, 
	FeatureExpressionParserTest.class, 
	FeatureIdeConfigReaderTest.class, 
	FeatureSetGroupReaderTest.class, 
	FeatureSetTest.class,
	LinkerTest.class, 
	ProductNodeTest.class, 
	SplCoverageGeneratorLauncherTest.class,
	SplCoverageReaderTest.class,
	AntennaCoverageAccumulatorTest.class,
	AntennaCoverageReaderTest.class,
	AntennaModelTest.class, 
	FeatureLocatorTest.class, 
	ProductFeatureLocationTest.class})
public class TestsWithoutVmArguments {

}
