package lab.square.spltcoverage.test.antenna;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import lab.square.spltcoverage.core.antenna.AntennaCoverageReader;
import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.model.ICoverageModelComponent;
import lab.square.spltcoverage.model.ICoverageModelComposite;
import lab.square.spltcoverage.model.antenna.AntennaProductCoverage;

public class AntennaCoverageReaderTest {
	
	private static final String PRODUCT_PATH = "D:/workspace_experiment_challenge/lab.square.spltcoverage.core/testResources/AntennaSplCoverage/product1";
	private static final String JAVA_SOURCE_PATH = "D:/workspace_experiment_challenge/lab.square.spltcoverage.core/src";
	private static final String CLASSPATH = "D:/workspace_experiment_challenge/lab.square.spltcoverage.core/target/classes";
	
	private static AntennaProductCoverage pc;
	
	@BeforeClass
	public static void setUp() {
		try {
			pc = AntennaCoverageReader.read(PRODUCT_PATH, CLASSPATH, JAVA_SOURCE_PATH);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
