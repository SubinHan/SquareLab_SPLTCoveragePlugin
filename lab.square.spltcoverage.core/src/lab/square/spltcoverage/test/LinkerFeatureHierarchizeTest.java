package lab.square.spltcoverage.test;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;

import lab.square.spltcoverage.core.analysis.ProductLinker;
import lab.square.spltcoverage.io.FeatureSetGroupReader;
import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.ProductGraph;

public class LinkerFeatureHierarchizeTest {

	@Test
	public void testHierarchize() {
		FeatureSetGroupReader reader = new FeatureSetGroupReader("D:\\workspacechallenege\\challenge-master\\workspace_IncLing\\Tools\\All_valid_conf\\FeatureAMP2\\products");
		Collection<Map<String, Boolean>> products = reader.readAll();
		Collection<ProductGraph> heads = ProductLinker.link(products);
		printProductGraphs(heads);
	}
	
	public void printProductGraphs(Collection<ProductGraph> heads) {
		for(ProductGraph head : heads) {
			printFeaturesRecur(head, 0);
		}
	}
	
	private void printFeaturesRecur(ProductGraph node, int depth) {
		printSpace(depth);
		printFeatures(node.getFeatureSet());
		for(ProductGraph child : node.getChildren()) {
			printFeaturesRecur(child, depth + 1);
		}
	}

	public void printSpace(int howMany) {
		for(int i = 0; i < howMany; i++)
			System.out.print("  ");
	}
	
	private void printFeatures(Map<String, Boolean> featureSet) {
		for(String key : featureSet.keySet()) {
			if(featureSet.get(key))
				System.out.print(key + " ");
		}
		System.out.println();
	}
}
