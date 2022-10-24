package lab.square.spltcoverage.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import lab.square.spltcoverage.io.SplCoverageReader;
import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.model.ICoverageModelComponent;
import lab.square.spltcoverage.model.ISplCoverageVisitor;
import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.SplCoverage;
import lab.square.spltcoverage.model.TestCaseCoverage;
import lab.square.spltcoverage.model.TestMethodCoverage;
import lab.square.spltcoverage.test.target.Configuration;
import lab.square.spltcoverage.utils.Tools;

public class SplCoverageReaderTest {

	private static final String COVERAGES_PATH = TestConfig.SPL_COVERAGE_PATH;
	private static final String CLASS_PATH = TestConfig.CLASSPATH_SELF;
	private static List<FeatureSet> expected;
	private static int pcCount = 0;
	private static int tccCount = 0;
	private static int tmcCount = 0;
	
	@Before
	public void setUp() {
		expected = new ArrayList<>();
		
		FeatureSet featureSet1 = new FeatureSet();
		FeatureSet featureSet2 = new FeatureSet();
		FeatureSet featureSet3 = new FeatureSet();
		FeatureSet featureSet4 = new FeatureSet();
		FeatureSet featureSet5 = new FeatureSet();
		
		featureSet2.setFeature(Configuration.CONFIG_A, true);
		
		featureSet3.setFeature(Configuration.CONFIG_B, true);
		
		featureSet4.setFeature(Configuration.CONFIG_A, true);
		featureSet4.setFeature(Configuration.CONFIG_B, true);
		
		featureSet5.setFeature(Configuration.CONFIG_A, true);
		featureSet5.setFeature(Configuration.CONFIG_B, true);
		featureSet5.setFeature(Configuration.CONFIG_C, true);
		
		expected.add(featureSet1);
		expected.add(featureSet2);
		expected.add(featureSet3);
		expected.add(featureSet4);
		expected.add(featureSet5);
	}
	
	@Test
	public void testSpltCoverageReader() throws IOException {
		SplCoverage splCoverage = new SplCoverage("test");
		
		SplCoverageReader.readInvariablePlCoverageInto(splCoverage, COVERAGES_PATH, CLASS_PATH);
		
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
				
				assertTrue(Tools.contains(expected, pc.getFeatureSet()));
				SplCoverageReaderTest.pcCount++;
			}

			@Override
			public void visit(TestCaseCoverage tcc) {
				for (ICoverageModelComponent tmc : tcc.getChildren()){
					this.visit((TestMethodCoverage)tmc);
				}
				SplCoverageReaderTest.tccCount++;
			}

			@Override
			public void visit(TestMethodCoverage tmc) {
				SplCoverageReaderTest.tmcCount++;
			}
		});
		
		assertEquals(5, pcCount);
		assertEquals(10, tccCount);
		assertEquals(40, tmcCount);
	}
	
	
}
