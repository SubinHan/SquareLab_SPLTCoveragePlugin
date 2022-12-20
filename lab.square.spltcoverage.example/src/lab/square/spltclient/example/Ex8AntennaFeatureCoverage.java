package lab.square.spltclient.example;

import java.util.Collection;

import lab.square.spltcoverage.core.antenna.AntennaProductAnalysis;
import lab.square.spltcoverage.core.antenna.AntennaProductAnalysis.FeatureCoverage;
import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.SplCoverage;
import lab.square.spltcoverage.model.antenna.AntennaProductCoverage;

public class Ex8AntennaFeatureCoverage {

	static SplCoverage splCoverage;
	static final String TARGET_FEATURE = "FloorPermission";
	
	public static void main(String[] args) {
		// read again before we created coverages.
		splCoverage = Ex2ReadAntennaPlCoverage.readSplCoverage();
		
		Collection<ProductCoverage> pcs = splCoverage.getProductCoverages();
		
		// We can get information of feature coverage about single feature, or multiple features.
		// In this case, I'm interseted in "FloorPermission" Feature.
		FeatureSet targetFeatureSet = new FeatureSet();
		targetFeatureSet.addFeature(TARGET_FEATURE);
		
		for(ProductCoverage pc : pcs) {
			if(!(pc instanceof AntennaProductCoverage))
				continue;
			// If correspond product doesn't have "FloorPermission", then continue.
			if(!pc.getFeatureSet().hasFeature(TARGET_FEATURE))
				continue;
			
			AntennaProductCoverage apc = (AntennaProductCoverage)pc;
			
			// We can analysis by using AntennaProductAnalysis
			// by just passing an AntennaProduct object.
			AntennaProductAnalysis analysis = new AntennaProductAnalysis(apc);
			
			// We can get collection of FeatureCoverages
			// that contains only about given target feature set.
			Collection<FeatureCoverage> featureCoverages = analysis.getFeatureCoverage(targetFeatureSet);
			
			int totalLines = 0;
			int totalCoveredLines = 0;
			
			System.out.println(apc.getFeatureSet().getFeatures());
			for(FeatureCoverage each : featureCoverages) {
				System.out.println("\t" + each.node + ": " + each.numCoveredLines + "/" + each.numLines + ", " + (double)each.numCoveredLines / each.numLines);
				totalLines += each.numLines;
				totalCoveredLines += each.numCoveredLines;
			}
			System.out.println("\t total: " + totalCoveredLines + "/" + totalLines + ", " + (double)totalCoveredLines / totalLines);
		}
		
	}
}
