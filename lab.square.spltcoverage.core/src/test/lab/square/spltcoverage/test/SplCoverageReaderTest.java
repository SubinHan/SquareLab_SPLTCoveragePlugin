package lab.square.spltcoverage.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import lab.square.spltcoverage.io.AbstractSplCoverageReader;
import lab.square.spltcoverage.io.SplCoverageReader;
import lab.square.spltcoverage.io.SplCoverageReaderFactory;
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
	private static final int NUM_PRODUCTS = 5;
	private static List<FeatureSet> expectedFeatureSets;
	private static Set<String> expectedProductNames;
	private static int pcCount = 0;
	private static int tccCount = 0;
	private static int tmcCount = 0;
	
	private static SplCoverage splCoverage;
	
	@BeforeClass
	public static void setUp() {
		setUpExpectedFeatureSets();
		setUpExpectedProductNames();
		setUpSplCoverage();
	}

	private static void setUpExpectedProductNames() {
		expectedProductNames = new HashSet<>();
		
		for(int i = 1; i <= NUM_PRODUCTS; i++) {
			expectedProductNames.add("Product" + i);
		}
	}

	private static void setUpSplCoverage() {
		AbstractSplCoverageReader reader = SplCoverageReaderFactory.createInvariableSplCoverageReader(CLASS_PATH);
		
		splCoverage = reader.readSplCoverage(COVERAGES_PATH);
	}

	private static void setUpExpectedFeatureSets() {
		expectedFeatureSets = new ArrayList<>();
		
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
		
		expectedFeatureSets.add(featureSet1);
		expectedFeatureSets.add(featureSet2);
		expectedFeatureSets.add(featureSet3);
		expectedFeatureSets.add(featureSet4);
		expectedFeatureSets.add(featureSet5);
	}
	
	@Test
	public void testSpltCoverageReader() throws IOException {
		
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
				
				assertTrue(Tools.contains(expectedFeatureSets, pc.getFeatureSet()));
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
	
	@Test
	public void testProductName() {
		for(ProductCoverage pc : splCoverage.getProductCoverages()) {
			assertTrue(expectedProductNames.contains(pc.getName()));
			expectedProductNames.remove(pc.getName());
		}
	}
}
