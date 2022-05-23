package lab.square.spltcoverage.test;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.junit.Test;

import lab.square.spltcoverage.core.analysis.ProductLinker;
import lab.square.spltcoverage.io.FeatureSetGroupReader;
import lab.square.spltcoverage.model.ProductGraph;

/*
 * Test 시: core의 plugin.xml dependency -> org.jacoco (0.8.6) 이어야 함.
 * Plug-in testing으로 수행할 것.
 */
public class LinkerTest2 {

	private Collection<ProductGraph> visited = new ArrayList<ProductGraph>();

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

		for (ProductGraph child : graph.getChildren()) {
			visitGraphRecur(child);
		}
	}


	private void printFeatures(Map<String, Boolean> featureSet) {
		for (String key : featureSet.keySet()) {
			if (featureSet.get(key))
				System.out.print(key + " ");
		}
		System.out.println();
	}
}
