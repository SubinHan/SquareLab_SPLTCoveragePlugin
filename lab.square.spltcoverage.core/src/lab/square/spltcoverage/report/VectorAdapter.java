package lab.square.spltcoverage.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import lab.square.similaritymeasure.core.IVector;
import lab.square.similaritymeasure.core.Vector;

public class VectorAdapter extends Vector{
	
	boolean[] vector;
	
	public VectorAdapter(Collection<Map<String, Boolean>> products, Map<String, Boolean> target) {
		List<String> existsFeatures = getExistsFeatures(products); 
		
		this.vector = adapt(existsFeatures, target);
	}
	
	public VectorAdapter(List<String> existsFeatures, Map<String, Boolean> target) {
		this.vector = adapt(existsFeatures, target);
	}
	
	private boolean[] adapt(List<String> existsFeatures, Map<String, Boolean> target) {
		boolean[] vector = new boolean[existsFeatures.size()];
		
		for(int i = 0; i < existsFeatures.size(); i++) {
			Boolean hasFeature = target.get(existsFeatures.get(i));
			if(hasFeature == null)
				vector[i] = false;
			else
				vector[i] = hasFeature;
		}
		
		return vector;
	}

	private List<String> getExistsFeatures(Collection<Map<String, Boolean>> products) {
		List<String> existsFeatures = new ArrayList<String>();
		
		for(Map<String, Boolean> product : products) {
			for(String key : product.keySet()) {
				if(!existsFeatures.contains(key))
					existsFeatures.add(key);
			}
		}
		
		return existsFeatures;
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
	public void printAll() {
		;
	}
}
