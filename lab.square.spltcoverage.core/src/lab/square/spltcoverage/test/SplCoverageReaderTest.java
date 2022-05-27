package lab.square.spltcoverage.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import lab.square.spltcoverage.io.SplCoverageReader;
import lab.square.spltcoverage.model.ICoverageModelComponent;
import lab.square.spltcoverage.model.ISpltCoverageVisitor;
import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.SplCoverage;
import lab.square.spltcoverage.model.TestCaseCoverage;
import lab.square.spltcoverage.model.TestMethodCoverage;
import lab.square.spltcoverage.test.target.Configuration;
import lab.square.spltcoverage.utils.Tools;

public class SplCoverageReaderTest {

	private static final String COVERAGES_PATH = "testResources/SplCoverage/";
	private static final String CLASS_PATH = "src/lab/square/spltcoverage/test/target/";
	private static List<Map<String, Boolean>> expected;
	public static int pcCount = 0;
	public static int tccCount = 0;
	public static int tmcCount = 0;
	
	@Before
	public void setUp() {
		expected = new ArrayList<>();
		
		Map<String, Boolean> featureSet1 = new HashMap<>();
		Map<String, Boolean> featureSet2 = new HashMap<>();
		Map<String, Boolean> featureSet3 = new HashMap<>();
		Map<String, Boolean> featureSet4 = new HashMap<>();
		Map<String, Boolean> featureSet5 = new HashMap<>();
		
		featureSet2.put(Configuration.CONFIG_A, true);
		
		featureSet3.put(Configuration.CONFIG_B, true);
		
		featureSet4.put(Configuration.CONFIG_A, true);
		featureSet4.put(Configuration.CONFIG_B, true);
		
		featureSet5.put(Configuration.CONFIG_A, true);
		featureSet5.put(Configuration.CONFIG_B, true);
		featureSet5.put(Configuration.CONFIG_C, true);
		
		expected.add(featureSet1);
		expected.add(featureSet2);
		expected.add(featureSet3);
		expected.add(featureSet4);
		expected.add(featureSet5);
	}
	
	@Test
	public void testSpltCoverageReader() throws IOException {
		SplCoverage splCoverage = new SplCoverage("test");
		
		SplCoverageReader reader = new SplCoverageReader(splCoverage, COVERAGES_PATH, CLASS_PATH);
		reader.read();
		
		splCoverage.accept(new ISpltCoverageVisitor() {
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
