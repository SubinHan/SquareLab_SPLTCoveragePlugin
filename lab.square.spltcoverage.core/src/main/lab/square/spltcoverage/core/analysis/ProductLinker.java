package lab.square.spltcoverage.core.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import lab.square.similaritymeasure.core.IVector;
import lab.square.similaritymeasure.core.Jaccard;
import lab.square.similaritymeasure.core.MostSimilarVector;
import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.model.ISplCoverageVisitor;
import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.ProductNode;
import lab.square.spltcoverage.model.ProductNodeVectorAdapter;
import lab.square.spltcoverage.model.SplCoverage;
import lab.square.spltcoverage.model.TestCaseCoverage;
import lab.square.spltcoverage.model.TestMethodCoverage;

public final class ProductLinker {
	private static int min;

	private ProductLinker() {
	}
	
	public static Collection<ProductNode> link(Collection<FeatureSet> products) {
		List<ProductProvider> collectedProducts = collectProduct(products);
		return link(collectedProducts);
	}
	
	private static List<ProductProvider> collectProduct(Collection<FeatureSet> products) {
		List<ProductProvider> result = new ArrayList<>();
		
		for(FeatureSet featureSet : products) {
			result.add(new ProductProvider(featureSet, null));
		}
		
		return result;
	}
	
	public static Collection<ProductNode> link(SplCoverage splCoverage) {
		List<ProductProvider> collectedProducts = collectProduct(splCoverage);
		return link(collectedProducts);
	}
	
	private static List<ProductProvider> collectProduct(SplCoverage splCoverage) {
		List<ProductProvider> result = new ArrayList<>();
		
		for(ProductCoverage pc : splCoverage.getProductCoverages()) {
			result.add(new ProductProvider(pc.getFeatureSet(), pc));
		}
		
		return result;
	}
	
	private static Collection<ProductNode> link(List<ProductProvider> products) {
		List<ProductNode> nodes = createProductNodes(products);
		
		Collection<FeatureSet> allFeatureSet = collectFeatureSets(products);
		List<String> allFeatures = FeatureSet.getAllFeatureList(allFeatureSet);
		
		for(ProductNode toLink : nodes) {
			if(toLink.getFeatureSet().getNumFeatures() == 0)
				continue;
			
			IVector toLinkAsVector = new ProductNodeVectorAdapter(allFeatures, toLink);
			List<IVector> parentCandidates = new ArrayList<>();
			List<IVector> childCandidate = new ArrayList<>();
			
			parentCandidates.add(toLinkAsVector);
			childCandidate.add(toLinkAsVector);
			
			for(ProductNode node : nodes) {
				if(isCanBeParentOf(node, toLink))
				{
					parentCandidates.add(new ProductNodeVectorAdapter(allFeatures, node));
					continue;
				}
				
				if(isCanBeParentOf(toLink, node)) {
					childCandidate.add(new ProductNodeVectorAdapter(allFeatures, node));
				}
			}
			
			linkMostSimilarNodesToParent(toLink, parentCandidates);
			linkMostSimilarNodesToChild(toLink, childCandidate);
		}
		
		setLevels(nodes);
		
		Collection<ProductNode> heads = findHeads(nodes);
		
		return heads;
	}

	private static List<ProductNode> createProductNodes(List<ProductProvider> products) {
		List<ProductNode> nodes = new ArrayList<>();
		
		for(ProductProvider product : products) {
			ProductNode node = null;
			if(product.productCoverage != null)
				node = new ProductNode(product.productCoverage);
			else
				node = new ProductNode(product.featureSet);
			nodes.add(node);
		}
		return nodes;
	}
	
	private static Collection<FeatureSet> collectFeatureSets(List<ProductProvider> products) {
		Collection<FeatureSet> result = new ArrayList<>();
		
		for(ProductProvider product : products) {
			result.add(product.featureSet);
		}
		
		return result;
	}

	private static boolean isCanBeParentOf(ProductNode parent, ProductNode child) {
		if(parent.getFeatureSet().getNumFeatures() >= child.getFeatureSet().getNumFeatures())
			return false;
		
		FeatureSet common = parent.getFeatureSet().intersect(child.getFeatureSet());
		if(common.getNumFeatures() != parent.getFeatureSet().getNumFeatures())
			return false;
		
		return true;
	}
	
	private static void linkMostSimilarNodesToParent(ProductNode toLink, List<IVector> products) {
		Jaccard similarity = new Jaccard();
		MostSimilarVector vectors = similarity.calculateMostSimilar(products, 0);
		
		for(IVector vector : vectors.vectors) {
			ProductNodeVectorAdapter adapter = (ProductNodeVectorAdapter) vector;

			linkParentAndChild(adapter.getNode(), toLink);
		}
	}
	
	private static void linkMostSimilarNodesToChild(ProductNode toLink, List<IVector> products) {
		Jaccard similarity = new Jaccard();
		MostSimilarVector vectors = similarity.calculateMostSimilar(products, 0);
		
		for(IVector vector : vectors.vectors) {
			ProductNodeVectorAdapter adapter = (ProductNodeVectorAdapter) vector;

			linkParentAndChild(toLink, adapter.getNode());
		}
	}

	private static void linkParentAndChild(ProductNode parent, ProductNode child) {
		if(child.getParents().contains(parent))
			return;
		
		child.addParent(parent);
		parent.addChild(child);
	}
	
	private static void setLevels(List<ProductNode> nodes) {
		for(ProductNode node : nodes) {
			node.setLevel(-1);
		}
		
		for(ProductNode node : nodes) {
			if(node.getChildren().isEmpty()) {
				setLevelDfs(node);
			}
		}
	}

	private static int setLevelDfs(ProductNode node) {
		int maxLevel = 0;
		
		if(node.getParents().isEmpty()) {
			node.setLevel(0);
			return 0;
		}
		
		if(node.getLevel() != -1) {
			return node.getLevel();
		}
		
		for(ProductNode parent : node.getParents()) {
			int level = 0;
			level = setLevelDfs(parent) + 1;
			if(maxLevel < level)
				maxLevel = level;
		}
		
		node.setLevel(maxLevel);
		return maxLevel;
	}

	private static Collection<ProductNode> findHeads(List<ProductNode> nodes) {
		Collection<ProductNode> heads = new ArrayList<>();
		
		for(ProductNode node : nodes) {
			if(node.getParents().isEmpty())
				heads.add(node);
		}
		
		return heads;
	}

	private static final class ProductProvider {
		public final FeatureSet featureSet;
		public final ProductCoverage productCoverage;
		
		public ProductProvider(FeatureSet featureSet, ProductCoverage productCoverage) {
			this.featureSet = featureSet;
			this.productCoverage = productCoverage;
		}
	}
}
