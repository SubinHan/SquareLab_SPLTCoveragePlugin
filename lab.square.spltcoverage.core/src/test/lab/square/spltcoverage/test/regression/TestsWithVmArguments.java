package lab.square.spltcoverage.test.regression;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import lab.square.spltcoverage.test.CoverageGeneratorTest2;
import lab.square.spltcoverage.test.CoverageReaderTest;
import lab.square.spltcoverage.test.FeatureExpressionParserTest;
import lab.square.spltcoverage.test.FeatureSetGroupReaderTest;
import lab.square.spltcoverage.test.FeatureSetTest;
import lab.square.spltcoverage.test.LinkerTest;
import lab.square.spltcoverage.test.ProductNodeTest;
import lab.square.spltcoverage.test.SplCoverageReaderTest;
import lab.square.spltcoverage.test.antenna.AntennaCoverageGeneratorTest;
import lab.square.spltcoverage.test.antenna.AntennaModelTest;
import lab.square.spltcoverage.test.antenna.FeatureLocatorTest;

// VM argument: -javaagent:resources/jacocoagent.jar=jmx=true -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=7777 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=localhost

@RunWith(Suite.class)
@Suite.SuiteClasses({CoverageGeneratorTest2.class, AntennaCoverageGeneratorTest.class})
public class TestsWithVmArguments {

}
