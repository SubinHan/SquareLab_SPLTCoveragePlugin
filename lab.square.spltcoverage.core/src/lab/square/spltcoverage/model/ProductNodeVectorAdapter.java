package lab.square.spltcoverage.model;

import java.util.List;

import lab.square.similaritymeasure.core.IVector;

public class ProductNodeVectorAdapter implements IVector {

	private final ProductNode node;
	private final boolean[] vector;
	
	public ProductNodeVectorAdapter(List<String> existsFeatures, ProductNode node) {
		this.node = node;
		this.vector = adapt(existsFeatures, node.getFeatureSet());
	}
	
	private boolean[] adapt(List<String> existsFeatures, FeatureSet target) {
		boolean[] vector = new boolean[existsFeatures.size()];
		
		for(int i = 0; i < existsFeatures.size(); i++) {
			vector[i] = target.hasFeature(existsFeatures.get(i));
		}
		
		return vector;
	}
	
	public ProductNode getNode() {
		return this.node;
	}
	
	@Override
	public int getDimension() {
		return this.vector.length;
	}

	@Override
	public boolean getValue(int dimension) {
		return this.vector[dimension];
	}

	@Override
	public boolean equals(IVector vector) {
		if(this.getDimension() != vector.getDimension())
			return false;
		
		for(int i = 0; i < vector.getDimension(); i++) {
			if(this.vector[i] != vector.getValue(i))
				return false;
		}
		
		return true;
	}
}
