package lab.square.spltcoverage.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import lab.square.spltcoverage.core.analysis.ProductLinker;
import lab.square.spltcoverage.io.FeatureSetGroupReader;
import lab.square.spltcoverage.model.ProductNode;
import lab.square.spltcoverage.test.target.Configuration;
import lab.square.spltcoverage.utils.Tools;

/*
 * Test 시: core의 plugin.xml dependency -> org.jacoco (0.8.6) 이어야 함.
 * Plug-in testing으로 수행할 것.
 */
public class LinkerTest2 {
	
	private Collection<ProductNode> visited;
	
	private Collection<Map<String,Boolean>> expected;

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
		
		expected.add(featureSet1);
		expected.add(featureSet2);
		expected.add(featureSet3);
		expected.add(featureSet4);
		expected.add(featureSet5);
	}
	
	@Test
	public void testLinkerWithOnlyFeatureSets() {
		String directory;
		directory = "D:\\workspace_experiment_challenge\\lab.square.spltcoverage.core\\testResources\\productsNewer";

		testLinker(directory);
	}

	private void testLinker(String directory) {
		FeatureSetGroupReader reader = new FeatureSetGroupReader(directory);
		Collection<Map<String, Boolean>> products = reader.readAll();

		Collection<ProductNode> heads = ProductLinker.link(products);
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

	private void printFeatures(Map<String, Boolean> featureSet) {
		for (Entry<String, Boolean> entry : featureSet.entrySet()) {
			if (Boolean.TRUE.equals(entry.getValue()))
				System.out.print(entry.getKey() + " ");
		}
		System.out.println();
	}
}
