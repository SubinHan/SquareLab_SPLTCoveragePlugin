package lab.square.spltcoverage.core.analysis;

import java.util.ArrayList;
import java.util.Collection;

import lab.square.spltcoverage.core.model.ProductCoverage;
import lab.square.spltcoverage.core.model.ProductGraph;

public class ProductGraphAnalyzer {
	Collection<ProductGraph> heads;
	Collection<ProductGraph> problems;
	Collection<ProductGraph> visited;

	public ProductGraphAnalyzer(Collection<ProductGraph> heads) {
		this.heads = heads;
		this.problems = new ArrayList<ProductGraph>();
		this.visited = new ArrayList<ProductGraph>();
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

				ProductCoverage pc = graph.getProductCoverage();
				Collection<ProductGraph> parents = graph.getParents();
				for (ProductGraph parent : graph.getParents()) {
					if(parent == null)
						continue;
					int thisScore = pc.getScore();
					int targetScore = parent.getProductCoverage().getScore();
					
					if(thisScore == targetScore) {
						if(!problems.contains(graph)) {
							problems.add(graph);
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

	public Collection<ProductGraph> getProblemProducts() {
		return this.problems;
	}

	private void acceptGraph(GraphVisitor visitor) {
		visited.clear();
		for(ProductGraph head : heads)
			visitor.visit(head);
	}

	private interface GraphVisitor {
		public void visit(ProductGraph graph);
	}
}
