package lab.square.spltcoverage.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.jacoco.core.analysis.ICounter;
import org.junit.BeforeClass;
import org.junit.Test;

import lab.square.spltcoverage.io.CoverageReader;
import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.model.ICoverageModelComponent;
import lab.square.spltcoverage.model.ICoverageModelComposite;
import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.test.target.Configuration;

public class CoverageReaderTest {
	
	private static final String CLASS_A = TestConfig.CLASS_A_CLASSNAME;
	private static final String COVERAGES_PATH = TestConfig.SINGLE_PRODUCT_COVERAGE_PATH;
	private static final String CLASS_PATH = TestConfig.CLASSPATH_SELF;

	private static ProductCoverage coverage;
	
	@BeforeClass
	public static void setUp() {
		try {
			coverage = CoverageReader.read(COVERAGES_PATH, CLASS_PATH);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCoverageReaderStaticMethod() {
		assertReadCorrectly(coverage, makeExpectedFeatureSet());
	}
	
	@Test
	public void testCoverageStatus() {
		assertEquals(ICounter.FULLY_COVERED, coverage.getProductCoverageStatusAtLineOfClass(6, CLASS_A));
		assertEquals(ICounter.FULLY_COVERED, coverage.getProductCoverageStatusAtLineOfClass(15, CLASS_A));
		assertEquals(ICounter.NOT_COVERED, coverage.getProductCoverageStatusAtLineOfClass(8, CLASS_A));
		assertEquals(ICounter.NOT_COVERED, coverage.getProductCoverageStatusAtLineOfClass(13, CLASS_A));
		assertEquals(ICounter.PARTLY_COVERED, coverage.getProductCoverageStatusAtLineOfClass(5, CLASS_A));
		assertEquals(ICounter.PARTLY_COVERED, coverage.getProductCoverageStatusAtLineOfClass(12, CLASS_A));
	}
	
	private void assertReadCorrectly(ProductCoverage coverage, FeatureSet expectedFeatureSet) {
		assertTrue(expectedFeatureSet.equals(coverage.getFeatureSet()));
		assertEquals(2, coverage.getChildren().size());
		
		int methodCount = 0;
		for(ICoverageModelComponent comp : coverage.getChildren()) {
			ICoverageModelComposite tcc = (ICoverageModelComposite)comp;
			methodCount += tcc.getChildren().size();
		}
		assertEquals(8, methodCount);
	}

	private FeatureSet makeExpectedFeatureSet() {
		FeatureSet expectedFeatureSet = new FeatureSet();
		expectedFeatureSet.setFeature(Configuration.CONFIG_A, true);
		return expectedFeatureSet;
	}
	
}
