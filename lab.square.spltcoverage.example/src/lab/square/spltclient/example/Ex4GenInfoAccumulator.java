package lab.square.spltclient.example;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import lab.square.spltcoverage.core.analysis.ProductLinker;
import lab.square.spltcoverage.core.antenna.AntennaCoverageAccumulator;
import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.model.ProductNode;
import lab.square.spltcoverage.model.SplCoverage;
import lab.square.spltcoverage.model.antenna.AntennaProductCoverage;

public class Ex4GenInfoAccumulator {

	private static SplCoverage splCoverage;
	private static Collection<ProductNode> heads;
	
	public static void main(String[] args) {
		// read again before we created coverages.
		splCoverage = Ex2ReadAntennaPlCoverage.readSplCoverage();

		// We can link the products by using ProductLinker.
		// It returns the root nodes of product hierarchy.
		heads = ProductLinker.link(splCoverage);

		// We can use the accumulator to accumulate PL coverages.
		// I accumulated iterating products by BFS,
		// and printed how much the accumulated product coverage changed compared to previous. 
		accumulateAndPrintResults();
	}
	
	private static void accumulateAndPrintResults() {
		AntennaCoverageAccumulator accumulator = new AntennaCoverageAccumulator();
		
		Queue<ProductNode> q = new LinkedList<>();
		Set<FeatureSet> featureSetVisited = new HashSet<>();
		
		for(ProductNode root : heads) {
			q.add(root);
			featureSetVisited.add(root.getFeatureSet());
		}
		
		// perform BFS.
		while(!q.isEmpty()) {			
			ProductNode node = q.poll();
			featureSetVisited.add(node.getFeatureSet());
			
			accumulator.accumulate((AntennaProductCoverage)node.getProductCoverage());
			System.out.println("===========visited!===========");
			System.out.println("Product Name: " + node.getProductCoverage().getName());
			System.out.println("Feature Set: " + node.getFeatureSet().toString());
			System.out.println("Faeture Set(Simplified): " + Arrays.toString(node.getFeatureSet().getFeatures().toArray()));
			System.out.println();
			System.out.println("New feature added: " + Arrays.toString(accumulator.getNewlyVisitedFeatures().toArray()));
			System.out.println("is coverage changed?: " + accumulator.isCoverageChanged());
			System.out.println();
			
			for(String className : accumulator.getNewlyActivatedClasses()) {
				System.out.println("\tclass: " + className);
				System.out.println("\tnewly activated: " + accumulator.getNewlyActivatedLineCountOfClass(className));
				System.out.println("\tnewly covered: " + accumulator.getNewlyCoveredLineCountOfClass(className));
				
				System.out.println("\tnewly covered line numbers: " + accumulator.getNewlyCoveredLineNumbersOfClass(className));
				
				int accumulatedActivated = accumulator.getTotalActivatedLineCountOfClass(className);
				int accumulatedCovered = accumulator.getTotalCoveredLineCountOfClass(className);				
				double accumulatedRatio = (double)accumulatedCovered / (double)accumulatedActivated * 100;
				String formattedRatio = String.format("%.2f", accumulatedRatio);
				
				System.out.println("\taccumulated line coverage: " + accumulatedCovered + "/" + accumulatedActivated + "(" + formattedRatio +"%)");
				System.out.println();
			}
			System.out.println();
			
			int totalActivated = accumulator.getTotalActivatedLine();
			int totalCovered = accumulator.getTotalCoveredLine();
			double totalRatio = (double)totalCovered / (double)totalActivated * 100;
			String formattedRatio = String.format("%.2f", totalRatio);
			System.out.println("total line coverage: " + totalCovered + "/" + totalActivated + "(" + formattedRatio + "%)");
			
			for(ProductNode child : node.getChildren()) {
				q.add(child);
			}
		}
	}

}
