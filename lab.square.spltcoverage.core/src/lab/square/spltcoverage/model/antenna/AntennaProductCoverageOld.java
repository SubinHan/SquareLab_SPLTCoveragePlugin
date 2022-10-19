package lab.square.spltcoverage.model.antenna;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.model.ProductCoverage;

public class AntennaProductCoverageOld extends ProductCoverage {

	private Map<String, Collection<FeatureLocation>> productFeatureLocations;
	
	public AntennaProductCoverageOld(FeatureSet featureSet, Map<String, Collection<FeatureLocation>> productFeatureLocations) {
		super(featureSet);
		initFeatureLocations(featureSet, productFeatureLocations);
	}

	private void initFeatureLocations(FeatureSet featureSet,
			Map<String, Collection<FeatureLocation>> productFeatureLocations) {
		this.productFeatureLocations = new HashMap<>();
		productFeatureLocations.entrySet().stream().forEach(entry -> {
			List<FeatureLocation> featureLocations = new ArrayList<>();
			
			entry.getValue().stream().forEach(fl -> {
				if(fl.isFeatureLocationOf(featureSet)) {
					featureLocations.add(fl);
				}
			});
			
			this.productFeatureLocations.put(entry.getKey(), featureLocations);
		});
	}
	
	public Collection<FeatureLocation> getFeatureLocationsOf(String className) {
		if(productFeatureLocations.containsKey(className))
			return new ArrayList<>(productFeatureLocations.get(className));
		return new ArrayList<>();
	}
	
	public Set<String> getClassNameSet(){
		return productFeatureLocations.keySet();
	}

}
