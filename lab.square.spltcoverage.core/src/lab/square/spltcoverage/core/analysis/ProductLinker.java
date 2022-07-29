package lab.square.spltcoverage.core.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lab.square.spltcoverage.model.ISplCoverageVisitor;
import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.ProductNode;
import lab.square.spltcoverage.model.SplCoverage;
import lab.square.spltcoverage.model.TestCaseCoverage;
import lab.square.spltcoverage.model.TestMethodCoverage;

public final class ProductLinker {
	private static int min;

	private ProductLinker() {
	}

	public static Collection<ProductNode> link(Collection<Map<String, Boolean>> products) {
		int min = getMinNumFeatureInFeatureSets(products);
		int max = getMaxNumFeatureInFeatureSets(products);
		Collection<Map<String, Boolean>> notGeneratedYet = new LinkedList<>(products);
		Collection<ProductNode> generatedGraph = new LinkedList<>();
		Collection<Map<String, Boolean>> baseProducts = findHaveNumFeatures(products, min);
		Collection<ProductNode> heads = new HashSet<>();

		int level = 0;
		for (Map<String, Boolean> product : baseProducts) {
			heads.add(makeGraphRecur(products, generatedGraph, notGeneratedYet, null, product, level));
		}

		int distance = 1;
		while (!notGeneratedYet.isEmpty()) {
			linkMoreRecur(heads, products, generatedGraph, notGeneratedYet, distance++);
			if(distance > max) {
				break;
			}
		}

		return heads;
	}

	

	@Deprecated
	public static Collection<ProductNode> link(SplCoverage manager) {
		int min = getMinNumFeature(manager);
		Collection<ProductCoverage> notGeneratedYet = new LinkedList<>(manager.getProductCoverages());
		Collection<ProductNode> generatedGraph = new LinkedList<>();
		List<ProductCoverage> baseProducts = findHaveNumFeatures(manager, min);
		Collection<ProductNode> heads = new HashSet<>();

		int level = 0;
		for (ProductCoverage pc : baseProducts) {
			heads.add(makeGraphRecur(manager, generatedGraph, notGeneratedYet, null, pc, level));
		}

		int distance = 1;
		while (!notGeneratedYet.isEmpty()) {
			linkMoreRecur(manager, generatedGraph, notGeneratedYet, distance++);
		}

		return heads;
	}

	private static List<ProductCoverage> findChildProducts(SplCoverage manager, ProductCoverage pc) {
		int numFeatures = 1;
		Map<String, Boolean> featureSet = pc.getFeatureSet();
		numFeatures += getNumFeatures(featureSet);

		List<ProductCoverage> beforeFiltered = findHaveNumFeatures(manager, numFeatures);
		List<ProductCoverage> toReturn = new LinkedList<>();
		for (ProductCoverage productCoverage : beforeFiltered) {
			int different = 0;
			Map<String, Boolean> targetFeatureSet = productCoverage.getFeatureSet();
			for (Entry<String, Boolean> entry : featureSet.entrySet()) {
				if (!entry.getValue().equals(targetFeatureSet.get(entry.getKey()))) {
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
		Collection<Map<String, Boolean>> toReturn = new LinkedList<>();
		for (Map<String, Boolean> featureSet : beforeFiltered) {
			int different = 0;
			for (Entry<String, Boolean> entry : featureSet.entrySet()) {
				if (!target.get(entry.getKey()).equals(entry.getValue())) {
					different++;
				}
			}
			if (different == 1)
				toReturn.add(featureSet);
		}

		return toReturn;
	}

	private static ProductNode findGraphEquals(Collection<ProductNode> collection, ProductCoverage toFind) {
		for (ProductNode graph : collection) {
			if (graph.getProductCoverage().justEquals(toFind))
				return graph;
		}

		return null;
	}
	
	private static ProductNode findGraphEquals(Collection<ProductNode> collection, Map<String, Boolean> toFind) {
		for (ProductNode graph : collection) {
			if (graph.getFeatureSet().equals(toFind))
				return graph;
		}

		return null;
	}

	private static List<ProductCoverage> findHaveNumFeatures(SplCoverage manager, int targetNumFeatures) {
		if (targetNumFeatures < 0)
			return Collections.emptyList();

		List<ProductCoverage> toReturn = new LinkedList<>();
		manager.accept(new ISplCoverageVisitor() {

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
			public void visit(SplCoverage pcm) {
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
		if (targetNumFeatures < 0)
			return Collections.emptyList();

		Collection<Map<String, Boolean>> toReturn = new LinkedList<>();

		for (Map<String, Boolean> featureSet : products) {
			int numFeatures = 0;
			numFeatures = getNumFeatures(featureSet);
			if (numFeatures == targetNumFeatures) {
				toReturn.add(featureSet);
			}
		}

		return toReturn;
	}

	private static Collection<ProductNode> findHaveNumFeaturesInGraphs(Collection<ProductNode> graphs,
			int numFeatures) {
		Collection<ProductNode> toReturn = new LinkedList<>();

		for (ProductNode graph : graphs) {
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
		Collection<ProductCoverage> toReturn = new LinkedList<>();

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
		Collection<Map<String, Boolean>> toReturn = new LinkedList<>();

		for (Map<String, Boolean> featureSet : products) {
			int targetNumFeatures = getNumFeatures(featureSet);
			if (targetNumFeatures == numFeatures) {
				toReturn.add(featureSet);
			}
		}

		return toReturn;
	}

	private static Collection<ProductNode> findParents(Collection<ProductNode> generatedGraph, ProductNode head) {
		Map<String, Boolean> featureSet = head.getFeatureSet();
		int distanceToParent = 2;

		Collection<ProductNode> toReturn = new LinkedList<>();

		do {
			int numFeatures = -distanceToParent;
			numFeatures += getNumFeatures(featureSet);

			Collection<ProductNode> beforeFilter = findHaveNumFeaturesInGraphs(generatedGraph, numFeatures);

			for (ProductNode graph : beforeFilter) {
				Map<String, Boolean> targetFeatureSet = graph.getFeatureSet();
				int different = 0;
				for (Entry<String, Boolean> entry : featureSet.entrySet()) {
					if (!entry.getValue().equals(targetFeatureSet.get(entry.getKey()))) {
						different++;
					}
				}
				if (different == distanceToParent) {
					toReturn.add(graph);
				}
			}

			distanceToParent++;
		} while (toReturn.isEmpty() && distanceToParent <= getNumFeatures(featureSet));
		return toReturn;
	}

	private static int getMinNumFeature(SplCoverage manager) {
		min = Integer.MAX_VALUE;
		manager.accept(new ISplCoverageVisitor() {

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
			public void visit(SplCoverage pcm) {
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
		
		return min;
	}

	private static int getMinNumFeatureInProductCoverages(Collection<ProductCoverage> productCoverages) {
		Collection<Map<String, Boolean>> products = new ArrayList<>();

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
	
	private static int getMaxNumFeatureInFeatureSets(Collection<Map<String, Boolean>> products) {
		int max = Integer.MIN_VALUE;
		for (Map<String, Boolean> featureSet : products) {
			int numFeatures = 0;
			numFeatures = getNumFeatures(featureSet);
			if (numFeatures > max) {
				max = numFeatures;
			}
		}
		return max;
	}

	private static int getNumFeatures(Map<String, Boolean> featureSet) {
		int numFeatures = 0;
		for (Entry<String, Boolean> entry : featureSet.entrySet()) {
			if (Boolean.TRUE.equals(entry.getValue())) {
				numFeatures++;
			}
		}
		return numFeatures;
	}

	private static void linkMoreRecur(SplCoverage manager, Collection<ProductNode> generatedGraph,
			Collection<ProductCoverage> notGeneratedYet, int distanceToParent) {
		int oldCount;
		int newCount = notGeneratedYet.size();

		do {
			int min = getMinNumFeatureInProductCoverages(notGeneratedYet);
			Collection<ProductCoverage> baseProducts = findHaveNumFeaturesInProducts(notGeneratedYet, min);
			Collection<ProductNode> heads = new HashSet<>();

			for (ProductCoverage pc : baseProducts) {
				heads.add(makeGraphRecur(manager, generatedGraph, notGeneratedYet, null, pc,
						getNumFeatures(pc.getFeatureSet()) - distanceToParent));
			}

			for (ProductNode head : heads) {
				Collection<ProductNode> parents = findParents(generatedGraph, head);
				for (ProductNode parent : parents) {
					parent.addChild(head);
					head.addParent(parent);
				}
			}

			oldCount = newCount;
			newCount = notGeneratedYet.size();
		} while (oldCount != newCount);
	}
	
	private static void linkMoreRecur(Collection<ProductNode> heads, Collection<Map<String, Boolean>> products, Collection<ProductNode> generatedGraph,
			Collection<Map<String, Boolean>> notGeneratedYet, int distanceToParent) {
		int oldCount;
		int newCount = notGeneratedYet.size();

		do {
			int min = getMinNumFeatureInFeatureSets(notGeneratedYet);
			Collection<Map<String, Boolean>> baseProducts = findHaveNumFeaturesInFeatureSets(notGeneratedYet, min);
			Collection<ProductNode> localHeads = new HashSet<>();

			for (Map<String, Boolean> featureSet : baseProducts) {
				localHeads.add(makeGraphRecur(products, generatedGraph, notGeneratedYet, null, featureSet,
						getNumFeatures(featureSet) - distanceToParent));
			}

			for (ProductNode localHead : localHeads) {
				Collection<ProductNode> parents = findParents(generatedGraph, localHead);
				if(parents.isEmpty()) {
					heads.add(localHead);
				}
				for (ProductNode parent : parents) {
					parent.addChild(localHead);
					localHead.addParent(parent);
				}
			}

			oldCount = newCount;
			newCount = notGeneratedYet.size();
		} while (oldCount != newCount);
	}

	private static ProductNode makeGraphRecur(SplCoverage manager, Collection<ProductNode> generatedGraph,
			Collection<ProductCoverage> notGeneratedYet, ProductNode parent, ProductCoverage pc, int level) {
		ProductNode graph = findGraphEquals(generatedGraph, pc);
		if (graph == null) {
			graph = new ProductNode(pc);
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

	private static ProductNode makeGraphRecur(Collection<Map<String, Boolean>> products,
			Collection<ProductNode> generatedGraph, Collection<Map<String, Boolean>> notGeneratedYet, ProductNode parent,
			Map<String, Boolean> targetProduct, int level) {
		ProductNode graph = findGraphEquals(generatedGraph, targetProduct);
		if (graph == null) {
			graph = new ProductNode(targetProduct);
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
