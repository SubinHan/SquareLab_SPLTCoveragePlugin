package lab.square.spltcoverage.test.antenna;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import lab.square.spltcoverage.io.AbstractSplCoverageReader;
import lab.square.spltcoverage.io.SplCoverageReaderFactory;
import lab.square.spltcoverage.io.antenna.AntennaSplCoverageReader;
import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.model.ICoverageModelComponent;
import lab.square.spltcoverage.model.ISplCoverageVisitor;
import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.SplCoverage;
import lab.square.spltcoverage.model.TestCaseCoverage;
import lab.square.spltcoverage.model.TestMethodCoverage;
import lab.square.spltcoverage.test.TestConfig;

public class AntennaSplCoverageReaderTest {

	private static final String COVERAGE_PATH = TestConfig.ANTENNA_SPL_COVERAGE_PATH;
	
	private static final String JAVA_SOURCE_PATH_1 = TestConfig.ANTENNA_PRODUCT1_PATH + "/src";
	private static final String CLASSPATH_1 = TestConfig.ANTENNA_PRODUCT1_PATH + "/bin";

	private static final String JAVA_SOURCE_PATH_2 = TestConfig.ANTENNA_PRODUCT2_PATH + "/src";
	private static final String CLASSPATH_2 = TestConfig.ANTENNA_PRODUCT2_PATH + "/bin";
	
	private static int pcCount;
	private static int tccCount;
	private static int tmcCount;
	private static Set<FeatureSet> expectedFeatureSet;
	
	@Test
	public void testRead() {
		String[] classpaths = new String[] { CLASSPATH_1, CLASSPATH_2 };
		String[] javaSourcePaths = new String[] { JAVA_SOURCE_PATH_1, JAVA_SOURCE_PATH_2 };
		
		AbstractSplCoverageReader reader = SplCoverageReaderFactory.createAntennaSplCoverageReader(classpaths, javaSourcePaths);
		
		SplCoverage splCoverage = reader.readSplCoverage(COVERAGE_PATH);
		
		FeatureSet featureSet1 = new FeatureSet();
		FeatureSet featureSet2 = new FeatureSet();
		featureSet1.addFeature("A");
		featureSet1.addFeature("B");
		
		expectedFeatureSet = new HashSet<>();
		expectedFeatureSet.add(featureSet1);
		expectedFeatureSet.add(featureSet2);
		
		pcCount = 0;
		tccCount = 0;
		tmcCount = 0;
		
		splCoverage.accept(new ISplCoverageVisitor() {
			@Override
			public void visit(SplCoverage pcm) {
				for (ProductCoverage pc : pcm.getProductCoverages()) {
					this.visit(pc);
				}
			}
			
			@Override
			public void visit(ProductCoverage pc) {
				for (ICoverageModelComponent tcc : pc.getChildren()){
					this.visit((TestCaseCoverage)tcc);
				}
				
				assertTrue(expectedFeatureSet.contains(pc.getFeatureSet()));
				pcCount++;
			}

			@Override
			public void visit(TestCaseCoverage tcc) {
				for (ICoverageModelComponent tmc : tcc.getChildren()){
					this.visit((TestMethodCoverage)tmc);
				}
				tccCount++;
			}

			@Override
			public void visit(TestMethodCoverage tmc) {
				tmcCount++;
			}
		});
		
		assertEquals(2, pcCount);
		assertEquals(4, tccCount);
		assertEquals(7, tmcCount);
	}
}
