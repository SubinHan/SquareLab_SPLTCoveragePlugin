package lab.square.spltcoverage.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import lab.square.spltcoverage.core.analysis.ProductLinker;
import lab.square.spltcoverage.io.AbstractSplCoverageReader;
import lab.square.spltcoverage.io.FeatureSetGroupReader;
import lab.square.spltcoverage.io.SplCoverageReaderFactory;
import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.model.ProductNode;
import lab.square.spltcoverage.model.SplCoverage;
import lab.square.spltcoverage.test.target.Configuration;
import lab.square.spltcoverage.utils.Tools;

/*
 * Test 시: core의 plugin.xml dependency -> org.jacoco (0.8.6) 이어야 함.
 * Plug-in testing으로 수행할 것.
 */
public class LinkerTest {
	
	private Collection<ProductNode> visited;
	private Collection<FeatureSet> expected;
	private SplCoverage splCoverage;

	@Before
	public void setUp() {
		visited = new ArrayList<>();
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
		
		expected.add(new FeatureSet(featureSet1));
		expected.add(new FeatureSet(featureSet2));
		expected.add(new FeatureSet(featureSet3));
		expected.add(new FeatureSet(featureSet4));
		expected.add(new FeatureSet(featureSet5));
	}
	
	@Test
	public void testLinkerWithOnlyFeatureSets() {
		String directory;
		directory = TestConfig.FEATURE_SET_GROUP_NEW;

		testLinker(directory);
	}

	private void testLinker(String directory) {
		FeatureSetGroupReader reader = new FeatureSetGroupReader(directory);
		Collection<FeatureSet> products = reader.readAll();

		Collection<ProductNode> heads = ProductLinker.link(products);
		verifyGraph(heads);
	}
	
	@Test
	public void testLinkerWithSplCoverage() {
		AbstractSplCoverageReader reader =
				SplCoverageReaderFactory.createInvariableSplCoverageReader(TestConfig.CLASSPATH_SELF);
		
		splCoverage = reader.readSplCoverage(TestConfig.SPL_COVERAGE_PATH);
		
		Collection<ProductNode> heads = ProductLinker.link(splCoverage);
		verifyGraph(heads);
	}

	private void verifyGraph(Collection<ProductNode> heads) {
		if (heads.isEmpty())
			fail();

		for (ProductNode head : heads)
			visitGraphRecur(head);
	}

	private void visitGraphRecur(ProductNode graph) {
		if (visited.contains(graph))
			return;
		visited.add(graph);
		System.out.println("//============================//");
		System.out.println("Level: " + graph.getLevel());
		System.out.println("Feature Set:");
		System.out.print("  ");
		printFeatures(graph.getFeatureSet());
		System.out.println("Parent's Feature Set:");
		for (ProductNode parent : graph.getParents()) {
			if (parent == null)
				continue;
			System.out.print("  ");
			printFeatures(parent.getFeatureSet());
		}
		
		assertTrue(Tools.contains(expected, graph.getFeatureSet()));

		for (ProductNode child : graph.getChildren()) {
			visitGraphRecur(child);
		}
	}

	private void printFeatures(FeatureSet featureSet) {
		for (String feature : featureSet.getFeatures()) {
			System.out.print(feature + " ");
		}
		System.out.println();
	}
	
	
}
