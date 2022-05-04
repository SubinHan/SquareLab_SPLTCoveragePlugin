package lab.square.spltcoverage.report;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import lab.square.spltcoverage.model.ProductGraph;

public class HierarchySimplifier {
	
	Set<ProductGraph> generatedClones;
	
	public HierarchySimplifier() {
		generatedClones = new HashSet<ProductGraph>();
	}
	
	public Collection<ProductGraph> cutByDepth(Collection<ProductGraph> heads, int depth) {
		Collection<ProductGraph> toReturn = new HashSet<ProductGraph>();
		
		if(depth < 1)
			return new HashSet<>();
		
		for(ProductGraph head : heads) {
			toReturn.add(cutByDepth(head, depth, 1));
		}
		
		return toReturn;
	}
	
	private ProductGraph cutByDepth(ProductGraph node, int targetDepth, int currentDepth) {
		ProductGraph clone = isGenerated(node);
		if(clone == null) {
			clone = new ProductGraph(node.getProductCoverage());
			generatedClones.add(clone);
		}
		
		if(currentDepth == targetDepth) {
			return clone;
		}
		for (ProductGraph child : node.getChildren()) {
			clone.addChild(cutByDepth(child, targetDepth, currentDepth+1));
		}
		return clone;
	}

	private ProductGraph isGenerated(ProductGraph node) {
		for (ProductGraph generated : generatedClones) {
			if(generated.getFeatureSet().equals(node.getFeatureSet()))
				return generated;
		}
		return null;
	}
}
