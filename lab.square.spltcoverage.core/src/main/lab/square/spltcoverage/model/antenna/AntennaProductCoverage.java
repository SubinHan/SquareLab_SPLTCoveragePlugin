package lab.square.spltcoverage.model.antenna;

import java.io.IOException;
import java.util.Collection;

import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.model.ProductCoverage;

public class AntennaProductCoverage extends ProductCoverage {
	
	private ProductFeatureLocation productFeatureLocation;
	
	public AntennaProductCoverage(FeatureSet featureSet, String javaSourcePath) {
		this(featureSet, javaSourcePath, "");
	}

	public AntennaProductCoverage(FeatureSet featureSet, String javaSourcePath, String name) {
		super(featureSet, name);
		initProductFeatureLocation(javaSourcePath);
	}

	private void initProductFeatureLocation(String javaSourcePath) {
		try {
			productFeatureLocation = new ProductFeatureLocation(javaSourcePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Collection<FeatureLocation> getFeatureLocationsOfClass(String classNameWithDots) {
		return productFeatureLocation.getFeatureLocationsOfClass(classNameWithDots);
	}

	public String getFeatureExpressionAtLineOfClass(int i, String classNameWithDots) {
		Collection<FeatureLocation> featureLocationsOfClass = getFeatureLocationsOfClass(classNameWithDots); 
		return FeatureLocation.getFeatureExpressionAtLineOfFeatureLocations(i, featureLocationsOfClass);
	}
	
	
}
