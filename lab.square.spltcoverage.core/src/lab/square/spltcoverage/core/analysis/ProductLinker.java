package lab.square.spltcoverage.core.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lab.square.spltcoverage.model.FeatureSet;
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
	
	public static Collection<ProductNode> link(Collection<FeatureSet> products) {
		int min = getMinNumFeatureInFeatureSets(products);
		int max = getMaxNumFeatureInFeatureSets(products);
		Collection<FeatureSet> notGeneratedYet = new LinkedList<>(products);
		Collection<ProductNode> generatedGraph = new LinkedList<>();
		Collection<FeatureSet> baseProducts = findHaveNumFeatures(products, min);
		Collection<ProductNode> heads = new HashSet<>();

		int level = 0;
		for (FeatureSet product : baseProducts) {
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

	public static Collection<ProductNode> link(SplCoverage splCoverage) {
		int min = getMinNumFeature(splCoverage);
		Collection<ProductCoverage> notGeneratedYet = new LinkedList<>(splCoverage.getProductCoverages());
		Collection<ProductNode> generatedGraph = new LinkedList<>();
		List<ProductCoverage> baseProducts = findHaveNumFeatures(splCoverage, min);
		Collection<ProductNode> heads = new HashSet<>();

		int level = 0;
		for (ProductCoverage pc : baseProducts) {
			heads.add(makeGraphRecur(splCoverage, generatedGraph, notGeneratedYet, null, pc, level));
		}

		int distance = 1;
		while (!notGeneratedYet.isEmpty()) {
			linkMoreRecur(splCoverage, generatedGraph, notGeneratedYet, distance++);
		}

		return heads;
	}

	private static List<ProductCoverage> findChildProducts(SplCoverage manager, ProductCoverage pc) {
		int numFeatures = 1;
		FeatureSet featureSet = pc.getFeatureSet();
		numFeatures += featureSet.getNumFeatures();

		List<ProductCoverage> beforeFiltered = findHaveNumFeatures(manager, numFeatures);
		List<ProductCoverage> toReturn = new LinkedList<>();
		for (ProductCoverage productCoverage : beforeFiltered) {
			FeatureSet targetFeatureSet = productCoverage.getFeatureSet();
			int different = targetFeatureSet.subtract(featureSet).getNumFeatures();
			if (different == 1)
				toReturn.add(productCoverage);
		}

		return toReturn;
	}

	private static Collection<FeatureSet> findChildProducts(Collection<FeatureSet> products,
			FeatureSet target) {
		int numFeatures = 1;
		numFeatures += target.getNumFeatures();

		Collection<FeatureSet> beforeFiltered = findHaveNumFeatures(products, numFeatures);
		Collection<FeatureSet> toReturn = new LinkedList<>();
		for (FeatureSet featureSet : beforeFiltered) {
			int different = featureSet.subtract(target).getNumFeatures();
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
	
	private static ProductNode findGraphEquals(Collection<ProductNode> collection, FeatureSet toFind) {
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
				FeatureSet featureSet = pc.getFeatureSet();
				numFeatures = featureSet.getNumFeatures();
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

	private static Collection<FeatureSet> findHaveNumFeatures(Collection<FeatureSet> products,
			int targetNumFeatures) {
		if (targetNumFeatures < 0)
			return Collections.emptyList();

		Collection<FeatureSet> toReturn = new LinkedList<>();

		for (FeatureSet featureSet : products) {
			int numFeatures = 0;
			numFeatures = featureSet.getNumFeatures();
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
			FeatureSet featureSet = graph.getFeatureSet();
			int targetNumFeatures = featureSet.getNumFeatures();
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
			FeatureSet featureSet = pc.getFeatureSet();
			int targetNumFeatures = featureSet.getNumFeatures();
			if (targetNumFeatures == numFeatures) {
				toReturn.add(pc);
			}
		}

		return toReturn;
	}
	
	private static Collection<FeatureSet> findHaveNumFeaturesInFeatureSets(Collection<FeatureSet> products,
			int numFeatures) {
		Collection<FeatureSet> toReturn = new LinkedList<>();

		for (FeatureSet featureSet : products) {
			int targetNumFeatures = featureSet.getNumFeatures();
			if (targetNumFeatures == numFeatures) {
				toReturn.add(featureSet);
			}
		}

		return toReturn;
	}

	private static Collection<ProductNode> findParents(Collection<ProductNode> generatedGraph, ProductNode head) {
		FeatureSet featureSet = head.getFeatureSet();
		int distanceToParent = 2;

		Collection<ProductNode> toReturn = new LinkedList<>();

		do {
			int numFeatures = -distanceToParent;
			numFeatures += featureSet.getNumFeatures();

			Collection<ProductNode> beforeFilter = findHaveNumFeaturesInGraphs(generatedGraph, numFeatures);

			for (ProductNode parentCandidate : beforeFilter) {
				FeatureSet targetFeatureSet = parentCandidate.getFeatureSet();				
				int different = featureSet.subtract(targetFeatureSet).getNumFeatures();
				if (different == distanceToParent) {
					toReturn.add(parentCandidate);
				}
			}

			distanceToParent++;
		} while (toReturn.isEmpty() && distanceToParent <= featureSet.getNumFeatures());
		return toReturn;
	}

	private static int getMinNumFeature(SplCoverage manager) {
		min = Integer.MAX_VALUE;
		manager.accept(new ISplCoverageVisitor() {

			@Override
			public void visit(ProductCoverage pc) {
				int numFeatures = 0;
				FeatureSet featureSet = pc.getFeatureSet();
				numFeatures = featureSet.getNumFeatures();
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
		Collection<FeatureSet> products = new ArrayList<>();

		for (ProductCoverage pc : productCoverages) {
			products.add(pc.getFeatureSet());
		}
		return getMinNumFeatureInFeatureSets(products);
	}

	private static int getMinNumFeatureInFeatureSets(Collection<FeatureSet> products) {
		int min = Integer.MAX_VALUE;
		for (FeatureSet featureSet : products) {
			int numFeatures = 0;
			numFeatures = featureSet.getNumFeatures();
			if (numFeatures < min) {
				min = numFeatures;
			}
		}
		return min;
	}
	
	private static int getMaxNumFeatureInFeatureSets(Collection<FeatureSet> products) {
		int max = Integer.MIN_VALUE;
		for (FeatureSet featureSet : products) {
			int numFeatures = 0;
			numFeatures = featureSet.getNumFeatures();
			if (numFeatures > max) {
				max = numFeatures;
			}
		}
		return max;
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
						pc.getFeatureSet().getNumFeatures() - distanceToParent));
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
	
	private static void linkMoreRecur(Collection<ProductNode> heads, Collection<FeatureSet> products, Collection<ProductNode> generatedGraph,
			Collection<FeatureSet> notGeneratedYet, int distanceToParent) {
		int oldCount;
		int newCount = notGeneratedYet.size();

		do {
			int min = getMinNumFeatureInFeatureSets(notGeneratedYet);
			Collection<FeatureSet> baseProducts = findHaveNumFeaturesInFeatureSets(notGeneratedYet, min);
			Collection<ProductNode> localHeads = new HashSet<>();

			for (FeatureSet featureSet : baseProducts) {
				localHeads.add(makeGraphRecur(products, generatedGraph, notGeneratedYet, null, featureSet,
						featureSet.getNumFeatures() - distanceToParent));
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

	private static ProductNode makeGraphRecur(Collection<FeatureSet> products,
			Collection<ProductNode> generatedGraph, Collection<FeatureSet> notGeneratedYet, ProductNode parent,
			FeatureSet targetProduct, int level) {
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
		for (FeatureSet child : findChildProducts(products, targetProduct)) {
			graph.addChild(makeGraphRecur(products, generatedGraph, notGeneratedYet, graph, child, level + 1));
		}

		return graph;
	}

}
