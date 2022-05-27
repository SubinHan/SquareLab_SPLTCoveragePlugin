package lab.square.spltcoverage.report;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import lab.square.spltcoverage.model.ProductNode;

public class HierarchySimplifier {
	
	Set<ProductNode> generatedClones;
	
	public HierarchySimplifier() {
		generatedClones = new HashSet<ProductNode>();
	}
	
	public Collection<ProductNode> cutByDepth(Collection<ProductNode> heads, int depth) {
		Collection<ProductNode> toReturn = new HashSet<ProductNode>();
		
		if(depth < 1)
			return new HashSet<>();
		
		for(ProductNode head : heads) {
			toReturn.add(cutByDepth(head, depth, 1));
		}
		
		return toReturn;
	}
	
	private ProductNode cutByDepth(ProductNode node, int targetDepth, int currentDepth) {
		ProductNode clone = isGenerated(node);
		if(clone == null) {
			clone = new ProductNode(node.getProductCoverage());
			generatedClones.add(clone);
		}
		
		if(currentDepth == targetDepth) {
			return clone;
		}
		for (ProductNode child : node.getChildren()) {
			clone.addChild(cutByDepth(child, targetDepth, currentDepth+1));
		}
		return clone;
	}

	private ProductNode isGenerated(ProductNode node) {
		for (ProductNode generated : generatedClones) {
			if(generated.getFeatureSet().equals(node.getFeatureSet()))
				return generated;
		}
		return null;
	}
}
