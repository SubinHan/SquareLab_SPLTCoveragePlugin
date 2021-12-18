package lab.square.spltcoverage.core.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.ProductCoverageManager;
import lab.square.spltcoverage.model.ProductGraph;
import lab.square.spltcoverage.model.TestCaseCoverage;
import lab.square.spltcoverage.model.TestMethodCoverage;

public class ProductLinker {
	private static int min;

	private ProductLinker() {
	}

	public static Collection<ProductGraph> link(Collection<Map<String, Boolean>> products) {
		int min = getMinNumFeatureInFeatureSets(products);
		Collection<Map<String, Boolean>> notGeneratedYet = new LinkedList<Map<String, Boolean>>(products);
		Collection<ProductGraph> generatedGraph = new LinkedList<ProductGraph>();
		Collection<Map<String, Boolean>> baseProducts = findHaveNumFeatures(products, min);
		Collection<ProductGraph> heads = new HashSet<ProductGraph>();

		int level = 0;
		for (Map<String, Boolean> product : baseProducts) {
			heads.add(makeGraphRecur(products, generatedGraph, notGeneratedYet, null, product, level));
		}

		int distance = 2;
		while (!notGeneratedYet.isEmpty()) {
			linkMoreRecur(products, generatedGraph, notGeneratedYet, distance++);
		}

		return heads;
	}

	public static Collection<ProductGraph> link(ProductCoverageManager manager) {
		int min = getMinNumFeature(manager);
		Collection<ProductCoverage> notGeneratedYet = new LinkedList<ProductCoverage>(manager.getProductCoverages());
		Collection<ProductGraph> generatedGraph = new LinkedList<ProductGraph>();
		List<ProductCoverage> baseProducts = findHaveNumFeatures(manager, min);
		Collection<ProductGraph> heads = new HashSet<ProductGraph>();

		int level = 0;
		for (ProductCoverage pc : baseProducts) {
			heads.add(makeGraphRecur(manager, generatedGraph, notGeneratedYet, null, pc, level));
		}

		int distance = 2;
		while (!notGeneratedYet.isEmpty()) {
			linkMoreRecur(manager, generatedGraph, notGeneratedYet, distance++);
		}

		return heads;
	}

	private static List<ProductCoverage> findChildProducts(ProductCoverageManager manager, ProductCoverage pc) {
		int numFeatures = 1;
		Map<String, Boolean> featureSet = pc.getFeatureSet();
		numFeatures += getNumFeatures(featureSet);

		List<ProductCoverage> beforeFiltered = findHaveNumFeatures(manager, numFeatures);
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

	private static Collection<Map<String, Boolean>> findChildProducts(Collection<Map<String, Boolean>> products,
			Map<String, Boolean> target) {
		int numFeatures = 1;
		numFeatures += getNumFeatures(target);

		Collection<Map<String, Boolean>> beforeFiltered = findHaveNumFeatures(products, numFeatures);
		Collection<Map<String, Boolean>> toReturn = new LinkedList<Map<String, Boolean>>();
		for (Map<String, Boolean> featureSet : beforeFiltered) {
			int different = 0;
			for (String key : target.keySet()) {
				if (target.get(key) != featureSet.get(key)) {
					different++;
				}
			}
			if (different == 1)
				toReturn.add(featureSet);
		}

		return toReturn;
	}

	private static ProductGraph findGraphEquals(Collection<ProductGraph> collection, ProductCoverage toFind) {
		for (ProductGraph graph : collection) {
			if (graph.getProductCoverage().justEquals(toFind))
				return graph;
		}

		return null;
	}
	
	private static ProductGraph findGraphEquals(Collection<ProductGraph> collection, Map<String, Boolean> toFind) {
		for (ProductGraph graph : collection) {
			if (graph.getFeatureSet().equals(toFind))
				return graph;
		}

		return null;
	}

	private static List<ProductCoverage> findHaveNumFeatures(ProductCoverageManager manager, int targetNumFeatures) {
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

	private static Collection<Map<String, Boolean>> findHaveNumFeatures(Collection<Map<String, Boolean>> products,
			int targetNumFeatures) {
		if (targetNumFeatures <= 0)
			return null;

		Collection<Map<String, Boolean>> toReturn = new LinkedList<Map<String, Boolean>>();

		for (Map<String, Boolean> featureSet : products) {
			int numFeatures = 0;
			numFeatures = getNumFeatures(featureSet);
			if (numFeatures == targetNumFeatures) {
				toReturn.add(featureSet);
			}
		}

		return toReturn;
	}

	private static Collection<ProductGraph> findHaveNumFeaturesInGraphs(Collection<ProductGraph> graphs,
			int numFeatures) {
		Collection<ProductGraph> toReturn = new LinkedList<ProductGraph>();

		for (ProductGraph graph : graphs) {
			Map<String, Boolean> featureSet = graph.getFeatureSet();
			int targetNumFeatures = getNumFeatures(featureSet);
			if (targetNumFeatures == numFeatures) {
				toReturn.add(graph);
			}
		}

		return toReturn;
	}

	private static Collection<ProductCoverage> findHaveNumFeaturesInProducts(Collection<ProductCoverage> pcs,
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
	
	private static Collection<Map<String, Boolean>> findHaveNumFeaturesInFeatureSets(Collection<Map<String, Boolean>> products,
			int numFeatures) {
		Collection<Map<String, Boolean>> toReturn = new LinkedList<Map<String, Boolean>>();

		for (Map<String, Boolean> featureSet : products) {
			int targetNumFeatures = getNumFeatures(featureSet);
			if (targetNumFeatures == numFeatures) {
				toReturn.add(featureSet);
			}
		}

		return toReturn;
	}

	private static Collection<ProductGraph> findParents(Collection<ProductGraph> generatedGraph, ProductGraph head) {
		Map<String, Boolean> featureSet = head.getFeatureSet();
		int distanceToParent = 2;

		Collection<ProductGraph> toReturn = new LinkedList<ProductGraph>();

		do {
			int numFeatures = -distanceToParent;
			numFeatures += getNumFeatures(featureSet);

			Collection<ProductGraph> beforeFilter = findHaveNumFeaturesInGraphs(generatedGraph, numFeatures);

			for (ProductGraph graph : beforeFilter) {
				Map<String, Boolean> targetFeatureSet = graph.getFeatureSet();
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

	private static int getMinNumFeature(ProductCoverageManager manager) {
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
		int toReturn = min;
		return toReturn;
	}

	private static int getMinNumFeatureInProductCoverages(Collection<ProductCoverage> productCoverages) {
		Collection<Map<String, Boolean>> products = new ArrayList<Map<String, Boolean>>();

		for (ProductCoverage pc : productCoverages) {
			products.add(pc.getFeatureSet());
		}
		return getMinNumFeatureInFeatureSets(products);
	}

	private static int getMinNumFeatureInFeatureSets(Collection<Map<String, Boolean>> products) {
		int min = Integer.MAX_VALUE;
		for (Map<String, Boolean> featureSet : products) {
			int numFeatures = 0;
			numFeatures = getNumFeatures(featureSet);
			if (numFeatures < min) {
				min = numFeatures;
			}
		}
		return min;
	}

	private static int getNumFeatures(Map<String, Boolean> featureSet) {
		int numFeatures = 0;
		for (String key : featureSet.keySet()) {
			if (featureSet.get(key)) {
				numFeatures++;
			}
		}
		return numFeatures;
	}

	private static void linkMoreRecur(ProductCoverageManager manager, Collection<ProductGraph> generatedGraph,
			Collection<ProductCoverage> notGeneratedYet, int distanceToParent) {
		int oldCount;
		int newCount = notGeneratedYet.size();

		do {
			int min = getMinNumFeatureInProductCoverages(notGeneratedYet);
			Collection<ProductCoverage> baseProducts = findHaveNumFeaturesInProducts(notGeneratedYet, min);
			Collection<ProductGraph> heads = new HashSet<ProductGraph>();

			for (ProductCoverage pc : baseProducts) {
				heads.add(makeGraphRecur(manager, generatedGraph, notGeneratedYet, null, pc,
						getNumFeatures(pc.getFeatureSet()) - distanceToParent));
			}

			for (ProductGraph head : heads) {
				Collection<ProductGraph> parents = findParents(generatedGraph, head);
				for (ProductGraph parent : parents) {
					parent.addChild(head);
					head.addParent(parent);
				}
			}

			oldCount = newCount;
			newCount = notGeneratedYet.size();
		} while (oldCount != newCount);
	}
	
	private static void linkMoreRecur(Collection<Map<String, Boolean>> products, Collection<ProductGraph> generatedGraph,
			Collection<Map<String, Boolean>> notGeneratedYet, int distanceToParent) {
		int oldCount;
		int newCount = notGeneratedYet.size();

		do {
			int min = getMinNumFeatureInFeatureSets(notGeneratedYet);
			Collection<Map<String, Boolean>> baseProducts = findHaveNumFeaturesInFeatureSets(notGeneratedYet, min);
			Collection<ProductGraph> heads = new HashSet<ProductGraph>();

			for (Map<String, Boolean> featureSet : baseProducts) {
				heads.add(makeGraphRecur(products, generatedGraph, notGeneratedYet, null, featureSet,
						getNumFeatures(featureSet) - distanceToParent));
			}

			for (ProductGraph head : heads) {
				Collection<ProductGraph> parents = findParents(generatedGraph, head);
				for (ProductGraph parent : parents) {
					parent.addChild(head);
					head.addParent(parent);
				}
			}

			oldCount = newCount;
			newCount = notGeneratedYet.size();
		} while (oldCount != newCount);
	}

	private static ProductGraph makeGraphRecur(ProductCoverageManager manager, Collection<ProductGraph> generatedGraph,
			Collection<ProductCoverage> notGeneratedYet, ProductGraph parent, ProductCoverage pc, int level) {
		ProductGraph graph = findGraphEquals(generatedGraph, pc);
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
		for (ProductCoverage child : findChildProducts(manager, pc)) {
			graph.addChild(makeGraphRecur(manager, generatedGraph, notGeneratedYet, graph, child, level + 1));
		}

		return graph;
	}

	private static ProductGraph makeGraphRecur(Collection<Map<String, Boolean>> products,
			Collection<ProductGraph> generatedGraph, Collection<Map<String, Boolean>> notGeneratedYet, ProductGraph parent,
			Map<String, Boolean> targetProduct, int level) {
		ProductGraph graph = findGraphEquals(generatedGraph, targetProduct);
		if (graph == null) {
			graph = new ProductGraph(targetProduct);
			graph.setLevel(level);
			generatedGraph.add(graph);
			notGeneratedYet.remove(targetProduct);
		} else {
			graph.addParent(parent);
			return graph;
		}
		graph.addParent(parent);
		for (Map<String, Boolean> child : findChildProducts(products, targetProduct)) {
			graph.addChild(makeGraphRecur(products, generatedGraph, notGeneratedYet, graph, child, level + 1));
		}

		return graph;
	}

}
