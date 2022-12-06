package lab.square.spltcoverage.core.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.logging.Logger;

import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.ProductNode;

public class ProductGraphAnalyzer {
	
	private static final Logger logger = Logger.getLogger(ProductGraphAnalyzer.class.getName());
	
	Collection<ProductNode> heads;
	Collection<ProductNode> problemProducts;
	Collection<ProductNode> visited;
	Collection<Collection<String>> problemFeatures;

	public ProductGraphAnalyzer(Collection<ProductNode> heads) {
		this.heads = heads;
		this.problemProducts = new ArrayList<>();
		this.visited = new ArrayList<>();
		this.problemFeatures = new ArrayList<>();
		init();
	}

	private void init() {
		initProblems();
	}

	private void initProblems() {
		acceptGraph(new GraphVisitor() {

			@Override
			public void visit(ProductNode graph) {
				if (visited.contains(graph))
					return;
				visited.add(graph);
				logger.info("Graph visited: " + graph.getFeatureSet());

				ProductCoverage pc = graph.getProductCoverage();
				for (ProductNode parent : graph.getParents()) {
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

				for (ProductNode child : graph.getChildren()) {
					visit(child);
				}
			}
		});
	}

	protected Collection<String> calculateDifference(FeatureSet smaller, FeatureSet bigger) {
		HashSet<String> toReturn = new HashSet<>();

		for (String feature : bigger.getFeatures()) {
			if (!smaller.hasFeature(feature)) {
				toReturn.add(feature);
			}
		}
		return toReturn;
	}

	public Collection<ProductNode> getProblemProducts() {
		return this.problemProducts;
	}

	public Collection<Collection<String>> getProblemFeatures() {
		return problemFeatures;
	}

	private void acceptGraph(GraphVisitor visitor) {
		visited.clear();
		for (ProductNode head : heads)
			visitor.visit(head);
	}

	private interface GraphVisitor {
		public void visit(ProductNode graph);
	}
}
