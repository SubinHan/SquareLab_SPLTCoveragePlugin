package lab.square.spltcoverage.test.antenna;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.BeforeClass;
import org.junit.Test;

import lab.square.spltcoverage.core.antenna.AntennaCoverageReader;
import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.model.ICoverageModelComponent;
import lab.square.spltcoverage.model.ICoverageModelComposite;
import lab.square.spltcoverage.model.antenna.AntennaProductCoverage;

public class AntennaCoverageReaderTest {
	
	private final String PRODUCT_PATH = "D:/workspace_experiment_challenge/lab.square.spltcoverage.coretestResources/AntennaSplCoverage/product1";
	private final String JAVA_SOURCE_PATH = "D:/workspace_experiment_challenge/lab.square.spltcoverage.core/src";
	private final String CLASSPATH = "D:/workspace_experiment_challenge/lab.square.spltcoverage.core/target/classes";
	
	AntennaProductCoverage pc;
	
	@BeforeClass
	public void setUp() {
		pc = AntennaCoverageReader.read(PRODUCT_PATH, CLASSPATH, JAVA_SOURCE_PATH);
	}
	
	@Test
	public void testFeatureSet() {
		FeatureSet featureSet = pc.getFeatureSet();
		assertFalse(featureSet.hasFeature("A"));
		assertFalse(featureSet.hasFeature("B"));
	}
	
	@Test
	public void testMethodCount() {
		int methodCount = 0;
		for (ICoverageModelComponent comp : pc.getChildren()) {
			methodCount += ((ICoverageModelComposite)comp).getChildren().size();
		}
		
		assertEquals(5, methodCount);
	}
	
	@Test
	public void testClassCount() {
		assertEquals(2, pc.getChildren().size());
	}
}
