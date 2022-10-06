package lab.square.spltcoverage.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import lab.square.spltcoverage.io.CoverageReader;
import lab.square.spltcoverage.model.ICoverageModelComponent;
import lab.square.spltcoverage.model.ICoverageModelComposite;
import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.test.target.Configuration;
import lab.square.spltcoverage.utils.Tools;

public class CoverageReaderTest {
	
	private static final String COVERAGES_PATH = "testResources/SingleProductCoverage/";
	private static final String CLASS_PATH = "src/lab/square/spltcoverage/test/target/";

	@Test
	public void testCoverageReaderStaticMethod() throws IOException {
		ProductCoverage coverage = CoverageReader.read(COVERAGES_PATH, CLASS_PATH);
		assertReadCorrectly(coverage, makeExpectedFeatureSet());
	}
	
	private void assertReadCorrectly(ProductCoverage coverage, Map<String, Boolean> expectedFeatureSet) {
		assertTrue(Tools.featureSetEquals(expectedFeatureSet, coverage.getFeatureSet()));
		assertEquals(2, coverage.getChildren().size());
		
		int methodCount = 0;
		for(ICoverageModelComponent comp : coverage.getChildren()) {
			ICoverageModelComposite tcc = (ICoverageModelComposite)comp;
			methodCount += tcc.getChildren().size();
		}
		assertEquals(8, methodCount);
	}

	private Map<String, Boolean> makeExpectedFeatureSet() {
		Map<String, Boolean> expectedFeatureSet = new HashMap<>();
		expectedFeatureSet.put(Configuration.CONFIG_A, true);
		return expectedFeatureSet;
	}
	
}
