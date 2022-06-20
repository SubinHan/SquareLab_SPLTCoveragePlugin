package lab.square.spltcoverage.test;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;

import lab.square.spltcoverage.core.analysis.ProductLinker;
import lab.square.spltcoverage.io.FeatureSetGroupReader;
import lab.square.spltcoverage.model.ProductNode;

public class LinkerFeatureHierarchizeTest {

	// TODO: Automate this.
	@Test
	public void testHierarchize() {
		FeatureSetGroupReader reader = new FeatureSetGroupReader("D:\\workspacechallenege\\challenge-master\\workspace_IncLing\\Tools\\All_valid_conf\\FeatureAMP2\\products");
		Collection<Map<String, Boolean>> products = reader.readAll();
		Collection<ProductNode> heads = ProductLinker.link(products);
		printProductGraphs(heads);
	}
	
	public void printProductGraphs(Collection<ProductNode> heads) {
		for(ProductNode head : heads) {
			printFeaturesRecur(head, 0);
		}
	}
	
	private void printFeaturesRecur(ProductNode node, int depth) {
		printSpace(depth);
		printFeatures(node.getFeatureSet());
		for(ProductNode child : node.getChildren()) {
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
