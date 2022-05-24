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
import lab.square.spltcoverage.io.FeatureSetGroupReader;
import lab.square.spltcoverage.model.ProductGraph;
import lab.square.spltcoverage.test.target.Configuration;

/*
 * Test 시: core의 plugin.xml dependency -> org.jacoco (0.8.6) 이어야 함.
 * Plug-in testing으로 수행할 것.
 */
public class LinkerTest2 {

	private Collection<ProductGraph> visited;
	
	private Collection<Map<String,Boolean>> expected;

	@Before
	public void setUp() {
		visited = new ArrayList<ProductGraph>();
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
		directory = "D:\\workspace_experiment_challenge\\lab.square.spltcoverage.core\\src\\lab\\square\\spltcoverage\\test\\target\\productsNewer";

		testLinker(directory);
	}

	private void testLinker(String directory) {
		FeatureSetGroupReader reader = new FeatureSetGroupReader(directory);
		Collection<Map<String, Boolean>> products = reader.readAll();

		Collection<ProductGraph> heads = ProductLinker.link(products);
		if (heads.isEmpty())
			fail();

		
		for (ProductGraph head : heads)
			visitGraphRecur(head);
	}

	private void visitGraphRecur(ProductGraph graph) {
		if (visited.contains(graph))
			return;
		visited.add(graph);
		double ratioSum = 0;
		System.out.println("//============================//");
		System.out.println("Level: " + graph.getLevel());
		System.out.println("Feature Set:");
		System.out.print("  ");
		printFeatures(graph.getFeatureSet());
		System.out.println("Parent's Feature Set:");
		for (ProductGraph parent : graph.getParents()) {
			if (parent == null)
				continue;
			System.out.print("  ");
			printFeatures(parent.getFeatureSet());
		}
		
		assertTrue(contains(expected, graph.getFeatureSet()));

		for (ProductGraph child : graph.getChildren()) {
			visitGraphRecur(child);
		}
	}


	private boolean contains(Collection<Map<String, Boolean>> featureSets, Map<String, Boolean> target) {
		for(Map<String, Boolean> featureSet : featureSets) {
			boolean flag = true;
			for(String key : featureSet.keySet()) {
				if(!equals(featureSet.get(key), target.get(key)))
					flag = false;
			}
			for(String key : target.keySet()) {
				if(!equals(featureSet.get(key), target.get(key)))
					flag = false;
			}
			if(flag)
				return true;
		}
		
		return false;
	}

	private boolean equals(Boolean boolean1, Boolean boolean2) {
		return getBooleanValue(boolean1) == getBooleanValue(boolean2);
	}
	
	private boolean getBooleanValue(Boolean value) {
		if(value == null)
			return false;
		return value;
	}

	private void printFeatures(Map<String, Boolean> featureSet) {
		for (String key : featureSet.keySet()) {
			if (featureSet.get(key))
				System.out.print(key + " ");
		}
		System.out.println();
	}
}
