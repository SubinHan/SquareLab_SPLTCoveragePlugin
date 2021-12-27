package lab.square.spltcoverage.core.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.ProductGraph;

public class ProductGraphAnalyzer {
	Collection<ProductGraph> heads;
	Collection<ProductGraph> problemProducts;
	Collection<ProductGraph> visited;
	Collection<Collection<String>> problemFeatures;

	public ProductGraphAnalyzer(Collection<ProductGraph> heads) {
		this.heads = heads;
		this.problemProducts = new ArrayList<ProductGraph>();
		this.visited = new ArrayList<ProductGraph>();
		this.problemFeatures = new ArrayList<Collection<String>>();
		init();
	}

	private void init() {
		initProblems();
	}

	private void initProblems() {
		acceptGraph(new GraphVisitor() {

			@Override
			public void visit(ProductGraph graph) {
				if (visited.contains(graph))
					return;
				visited.add(graph);
				System.out.println("Graph visited: " + graph.getFeatureSet());

				ProductCoverage pc = graph.getProductCoverage();
				for (ProductGraph parent : graph.getParents()) {
					if (parent == null)
						continue;

					if (pc.equals(parent.getProductCoverage())) {

						problemFeatures.add(
								calculateDifference(parent.getProductCoverage().getFeatureSet(), pc.getFeatureSet()));

						if (!problemProducts.contains(graph)) {
							problemProducts.add(graph);
							break;
						}
					}
				}

				for (ProductGraph child : graph.getChildren()) {
					visit(child);
				}
			}
		});
	}

	protected Collection<String> calculateDifference(Map<String, Boolean> smaller, Map<String, Boolean> bigger) {
		HashSet<String> toReturn = new HashSet<String>();

		for (String key : smaller.keySet()) {
			if (!smaller.get(key)) {
				if (bigger.get(key)) {
					toReturn.add(key);
				}
			}
		}
		return toReturn;
	}

	public Collection<ProductGraph> getProblemProducts() {
		return this.problemProducts;
	}

	public Collection<Collection<String>> getProblemFeatures() {
		return problemFeatures;
	}

	private void acceptGraph(GraphVisitor visitor) {
		visited.clear();
		for (ProductGraph head : heads)
			visitor.visit(head);
	}

	private interface GraphVisitor {
		public void visit(ProductGraph graph);
	}
}
