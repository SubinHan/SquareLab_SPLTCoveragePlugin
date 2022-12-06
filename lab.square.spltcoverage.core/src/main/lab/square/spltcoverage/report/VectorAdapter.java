package lab.square.spltcoverage.report;

import java.util.Collection;
import java.util.List;

import lab.square.similaritymeasure.core.Vector;
import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.model.ProductNode;
import lab.square.spltcoverage.utils.Tools;

public class VectorAdapter extends Vector{
	
	boolean[] vector;
	
	public VectorAdapter(Collection<FeatureSet> products, FeatureSet target) {
		List<String> existsFeatures = Tools.getAllExistsFeatures(products); 
		
		this.vector = adapt(existsFeatures, target);
	}
	
	public VectorAdapter(List<String> existsFeatures, FeatureSet target) {
		this.vector = adapt(existsFeatures, target);
	}

	private boolean[] adapt(List<String> existsFeatures, FeatureSet target) {
		boolean[] vector = new boolean[existsFeatures.size()];
		
		for(int i = 0; i < existsFeatures.size(); i++) {
			vector[i] = target.hasFeature(existsFeatures.get(i));
		}
		
		return vector;
	}

	@Override
	public int getDimension() {
		return this.vector.length;
	}

	@Override
	public boolean getValue(int dimension) {
		return this.vector[dimension];
	}

}
