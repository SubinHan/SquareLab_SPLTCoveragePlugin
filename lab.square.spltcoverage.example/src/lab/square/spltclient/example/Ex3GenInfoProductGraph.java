package lab.square.spltclient.example;

import java.io.IOException;
import java.util.Collection;

import lab.square.spltcoverage.core.analysis.ProductLinker;
import lab.square.spltcoverage.model.ProductNode;
import lab.square.spltcoverage.model.SplCoverage;
import lab.square.spltcoverage.report.GraphVizGenerator;

public class Ex3GenInfoProductGraph {
	
	private static SplCoverage splCoverage;
	private static final String OUTPUT_PATH = "spltoutput/graph.png";
	private static Collection<ProductNode> heads;
	
	public static void main(String[] args) {
		// read again before we created coverages.
		splCoverage = Ex2ReadAntennaPlCoverage.readSplCoverage();

		// We can link the products by using ProductLinker.
		// It returns the root nodes of product hierarchy.
		heads = ProductLinker.link(splCoverage);
		
		// We can generate the image of product hierarchy by using VizGenerator.
		generateProductGraph();
	}
	
	private static void generateProductGraph() {
		try {
			GraphVizGenerator.generate(heads, GraphVizGenerator.CONFIG_LIGHT_LEFTTORIGHT, OUTPUT_PATH);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
