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
import lab.square.spltcoverage.test.TestConfig;

public class AntennaCoverageReaderTest {
	
	private static final String PRODUCT_CLASS_A = TestConfig.ANTENNA_CLASS_A_CLASSNAME;
	private static final String PRODUCT_PATH = TestConfig.ANTENNA_SPL_COVERAGE_PATH + "/product2";
	private static final String JAVA_SOURCE_PATH = TestConfig.ANTENNA_PRODUCT2_PATH + "/src";
	private static final String CLASSPATH = TestConfig.ANTENNA_PRODUCT2_PATH + "/bin";
	
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
		assertNotNull(pc.getFeatureLocationsOfClass(PRODUCT_CLASS_A));
		
		FeatureSet featureExpressionOfLine9 = new FeatureSet();
		featureExpressionOfLine9.addFeature("A");

		FeatureSet featureExpressionOfLine13 = new FeatureSet();
		featureExpressionOfLine13.setFeature("A", false);
		featureExpressionOfLine13.addFeature("B");
		
		assertTrue(FeatureExpressionParser.evaluate(pc.getFeatureExpressionAtLineOfClass(7, PRODUCT_CLASS_A), featureExpressionOfLine9));
		assertFalse(FeatureExpressionParser.evaluate(pc.getFeatureExpressionAtLineOfClass(7, PRODUCT_CLASS_A), featureExpressionOfLine13));
		
		assertTrue(FeatureExpressionParser.evaluate(pc.getFeatureExpressionAtLineOfClass(9, PRODUCT_CLASS_A), featureExpressionOfLine13));
		assertFalse(FeatureExpressionParser.evaluate(pc.getFeatureExpressionAtLineOfClass(9, PRODUCT_CLASS_A), featureExpressionOfLine9));
	}
}
