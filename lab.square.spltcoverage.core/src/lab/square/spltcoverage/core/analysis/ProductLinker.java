package lab.square.spltcoverage.core.analysis;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.ProductCoverageManager;
import lab.square.spltcoverage.model.ProductGraph;
import lab.square.spltcoverage.model.TestCaseCoverage;
import lab.square.spltcoverage.model.TestMethodCoverage;

public class ProductLinker {
	private int min;
	private ProductCoverageManager manager;
	private Collection<ProductCoverage> notGeneratedYet;
	private Collection<ProductGraph> generatedGraph;

	public ProductLinker(ProductCoverageManager manager) {
		this();
		this.manager = manager;
	}

	private ProductLinker() {
		this.notGeneratedYet = new LinkedList<ProductCoverage>();
		this.generatedGraph = new LinkedList<ProductGraph>();
	}

	public Collection<ProductGraph> linkAll() {
		getMinNumFeature();
		notGeneratedYet = new LinkedList<ProductCoverage>(manager.getProductCoverages());
		List<ProductCoverage> baseProducts = findHaveNumFeatures(min);
		Collection<ProductGraph> heads = new HashSet<ProductGraph>();

		int level = 0;
		for (ProductCoverage pc : baseProducts) {
			heads.add(makeGraphRecur(null, pc, level));
		}

		int distance = 2;
		while (!notGeneratedYet.isEmpty()) {
			linkMoreRecur(distance++);
		}

		return heads;
	}

	private List<ProductCoverage> findChildProducts(ProductCoverage pc) {
		int numFeatures = 1;
		Map<String, Boolean> featureSet = pc.getFeatureSet();
		numFeatures += getNumFeatures(featureSet);

		List<ProductCoverage> beforeFiltered = findHaveNumFeatures(numFeatures);
		List<ProductCoverage> toReturn = new LinkedList<ProductCoverage>();
		for (ProductCoverage productCoverage : beforeFiltered) {
			int different = 0;
			Map<String, Boolean> targetFeatureSet = productCoverage.getFeatureSet();
			for (String key : featureSet.keySet()) {
				if (featureSet.get(key) != targetFeatureSet.get(key)) {
					different++;
				}
			}
			if (different == 1)
				toReturn.add(productCoverage);
		}

		return toReturn;
	}

	private ProductGraph findGraph(ProductCoverage pc) {
		for (ProductGraph graph : generatedGraph) {
			if (graph.getProductCoverage().justEquals(pc))
				return graph;
		}

		return null;
	}

	private List<ProductCoverage> findHaveNumFeatures(int targetNumFeatures) {
		if (targetNumFeatures < 0)
			return null;

		List<ProductCoverage> toReturn = new LinkedList<ProductCoverage>();
		manager.accept(new ISpltCoverageVisitor() {

			@Override
			public void visit(ProductCoverage pc) {
				int numFeatures = 0;
				Map<String, Boolean> featureSet = pc.getFeatureSet();
				numFeatures = getNumFeatures(featureSet);
				if (numFeatures == targetNumFeatures) {
					toReturn.add(pc);
				}
			}

			@Override
			public void visit(ProductCoverageManager pcm) {
				for (ProductCoverage pc : pcm.getProductCoverages()) {
					this.visit(pc);
				}
			}

			@Override
			public void visit(TestCaseCoverage tcc) {
			}

			@Override
			public void visit(TestMethodCoverage tmc) {
			}
		});

		return toReturn;
	}

	private Collection<ProductGraph> findHaveNumFeaturesInGraphs(Collection<ProductGraph> graphs, int numFeatures) {
		Collection<ProductGraph> toReturn = new LinkedList<ProductGraph>();

		for (ProductGraph graph : graphs) {
			Map<String, Boolean> featureSet = graph.getProductCoverage().getFeatureSet();
			int targetNumFeatures = getNumFeatures(featureSet);
			if (targetNumFeatures == numFeatures) {
				toReturn.add(graph);
			}
		}

		return toReturn;
	}

	private Collection<ProductCoverage> findHaveNumFeaturesInProducts(Collection<ProductCoverage> pcs,
			int numFeatures) {
		Collection<ProductCoverage> toReturn = new LinkedList<ProductCoverage>();

		for (ProductCoverage pc : pcs) {
			Map<String, Boolean> featureSet = pc.getFeatureSet();
			int targetNumFeatures = getNumFeatures(featureSet);
			if (targetNumFeatures == numFeatures) {
				toReturn.add(pc);
			}
		}

		return toReturn;
	}

	private Collection<ProductGraph> findParents(ProductGraph head) {
		Map<String, Boolean> featureSet = head.getProductCoverage().getFeatureSet();
		int distanceToParent = 2;

		Collection<ProductGraph> toReturn = new LinkedList<ProductGraph>();

		do {
			int numFeatures = -distanceToParent;
			numFeatures += getNumFeatures(featureSet);

			Collection<ProductGraph> beforeFilter = findHaveNumFeaturesInGraphs(generatedGraph, numFeatures);

			for (ProductGraph graph : beforeFilter) {
				Map<String, Boolean> targetFeatureSet = graph.getProductCoverage().getFeatureSet();
				int different = 0;
				for (String key : featureSet.keySet()) {
					if (featureSet.get(key) != targetFeatureSet.get(key)) {
						different++;
					}
				}
				if (different == distanceToParent) {
					toReturn.add(graph);
				}
			}
			
			distanceToParent++;
		} while (toReturn.size() == 0 || distanceToParent > getNumFeatures(featureSet));
		return toReturn;
	}

	private void getMinNumFeature() {
		min = Integer.MAX_VALUE;
		manager.accept(new ISpltCoverageVisitor() {

			@Override
			public void visit(ProductCoverage pc) {
				int numFeatures = 0;
				Map<String, Boolean> featureSet = pc.getFeatureSet();
				numFeatures = getNumFeatures(featureSet);
				if (numFeatures < min) {
					min = numFeatures;
				}
			}

			@Override
			public void visit(ProductCoverageManager pcm) {
				for (ProductCoverage pc : pcm.getProductCoverages()) {
					this.visit(pc);
				}
			}

			@Override
			public void visit(TestCaseCoverage tcc) {
			}

			@Override
			public void visit(TestMethodCoverage tmc) {
			}
		});
	}

	private int getMinNumFeature(Collection<ProductCoverage> productCoverages) {
		int min = Integer.MAX_VALUE;
		for (ProductCoverage pc : productCoverages) {
			int numFeatures = 0;
			Map<String, Boolean> featureSet = pc.getFeatureSet();
			numFeatures = getNumFeatures(featureSet);
			if (numFeatures < min) {
				min = numFeatures;
			}
		}
		return min;
	}

	private int getNumFeatures(Map<String, Boolean> featureSet) {
		int numFeatures = 0;
		for (String key : featureSet.keySet()) {
			if (featureSet.get(key)) {
				numFeatures++;
			}
		}
		return numFeatures;
	}

	private void linkMoreRecur(int distanceToParent) {
		int oldCount;
		int newCount = notGeneratedYet.size();

		do {
			int min = getMinNumFeature(notGeneratedYet);
			Collection<ProductCoverage> baseProducts = findHaveNumFeaturesInProducts(notGeneratedYet, min);
			Collection<ProductGraph> heads = new HashSet<ProductGraph>();

			for (ProductCoverage pc : baseProducts) {
				heads.add(makeGraphRecur(null, pc, getNumFeatures(pc.getFeatureSet()) - distanceToParent));
			}

			for (ProductGraph head : heads) {
				Collection<ProductGraph> parents = findParents(head);
				for (ProductGraph parent : parents) {
					parent.addChild(head);
					head.addParent(parent);
				}
			}

			oldCount = newCount;
			newCount = notGeneratedYet.size();
		} while (oldCount != newCount);
	}

	private ProductGraph makeGraphRecur(ProductGraph parent, ProductCoverage pc, int level) {
		ProductGraph graph = findGraph(pc);
		if (graph == null) {
			graph = new ProductGraph(pc);
			graph.setLevel(level);
			generatedGraph.add(graph);
			notGeneratedYet.remove(pc);
		} else {
			graph.addParent(parent);
			return graph;
		}
		graph.addParent(parent);
		for (ProductCoverage child : findChildProducts(pc)) {
			graph.addChild(makeGraphRecur(graph, child, level + 1));
		}

		return graph;
	}

}
