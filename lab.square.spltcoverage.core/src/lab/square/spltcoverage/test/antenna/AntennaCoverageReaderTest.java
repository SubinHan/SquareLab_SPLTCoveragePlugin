package lab.square.spltcoverage.test.antenna;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import lab.square.spltcoverage.core.antenna.FeatureExpressionParser;
import lab.square.spltcoverage.io.antenna.AntennaCoverageReader;
import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.model.ICoverageModelComponent;
import lab.square.spltcoverage.model.ICoverageModelComposite;
import lab.square.spltcoverage.model.antenna.AntennaProductCoverage;

public class AntennaCoverageReaderTest {
	
	private static final String PRODUCT2_CLASS_A = "test.spltcoverage.antennaproduct.ClassA";
	private static final String PRODUCT_PATH = "D:/workspace_experiment_challenge/lab.square.spltcoverage.core/testResources/AntennaSplCoverage2/product2";
	private static final String JAVA_SOURCE_PATH = "D:/workspace_experiment_challenge/lab.square.spltcoverage.core/testResources/AntennaPlProjects/antenna.test.product2/src";
	private static final String CLASSPATH = "D:/workspace_experiment_challenge/lab.square.spltcoverage.core/testResources/AntennaPlProjects/antenna.test.product2/bin";
	
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
		
		assertEquals(2, methodCount);
	}
	
	@Test
	public void testClassCount() {
		assertEquals(2, pc.getChildren().size());
	}
	
	@Test
	public void testSource() {		
		assertNotNull(pc.getFeatureLocationsOfClass(PRODUCT2_CLASS_A));
		
		FeatureSet featureExpressionOfLine9 = new FeatureSet();
		featureExpressionOfLine9.addFeature("A");

		FeatureSet featureExpressionOfLine13 = new FeatureSet();
		featureExpressionOfLine13.setFeature("A", false);
		featureExpressionOfLine13.addFeature("B");
		
		assertTrue(FeatureExpressionParser.evaluate(pc.getFeatureExpressionAtLineOfClass(7, PRODUCT2_CLASS_A), featureExpressionOfLine9));
		assertFalse(FeatureExpressionParser.evaluate(pc.getFeatureExpressionAtLineOfClass(7, PRODUCT2_CLASS_A), featureExpressionOfLine13));
		
		assertTrue(FeatureExpressionParser.evaluate(pc.getFeatureExpressionAtLineOfClass(9, PRODUCT2_CLASS_A), featureExpressionOfLine13));
		assertFalse(FeatureExpressionParser.evaluate(pc.getFeatureExpressionAtLineOfClass(9, PRODUCT2_CLASS_A), featureExpressionOfLine9));
	}
}
