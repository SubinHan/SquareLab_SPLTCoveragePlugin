package lab.square.spltcoverage.core.antenna.model;

import java.util.Collection;
import java.util.Map;

import lab.square.spltcoverage.core.antenna.FeatureLocation;
import lab.square.spltcoverage.model.ProductCoverage;

public class AntennaProductCoverage extends ProductCoverage {

	private Collection<FeatureLocation> featureLocations;
	
	public AntennaProductCoverage(Map<String, Boolean> featureSet, Collection<FeatureLocation> featureLocations) {
		super(featureSet);
		this.featureLocations = featureLocations;
	}

}
