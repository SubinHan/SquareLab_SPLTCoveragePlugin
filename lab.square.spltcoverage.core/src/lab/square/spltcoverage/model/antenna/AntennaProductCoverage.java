package lab.square.spltcoverage.model.antenna;

import java.util.Collection;

import lab.square.spltcoverage.core.antenna.FeatureLocation;
import lab.square.spltcoverage.core.antenna.FeatureLocator;
import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.model.ProductCoverage;

public class AntennaProductCoverage extends ProductCoverage {
	
	AntennaSourceFile sourceFile;
	
	public AntennaProductCoverage(FeatureSet featureSet, String javaSourcePath) {
		this(featureSet, javaSourcePath, "");
	}

	public AntennaProductCoverage(FeatureSet featureSet, String javaSourcePath, String name) {
		super(featureSet, name);
		
		sourceFile = new AntennaSourceFile(javaSourcePath);
	}
	
	
}
