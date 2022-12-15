package lab.square.spltcoverage.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import org.jacoco.core.analysis.IClassCoverage;

/**
 * The ProductCoverageManager class is a class containing the ProductCoverages.
 * @author SQUARELAB
 */
public class SplCoverage implements ICoverageModelComposite {
	private String name;
	private Collection<IClassCoverage> classCoverages;
	private Collection<ICoverageModelComponent> productCoverages;
	
	/**
	 * Create an empty ProductCoverageManager.
	 */
	public SplCoverage(String name) {
		this.name = name;
		classCoverages = new ArrayList<>();
		productCoverages = new ArrayList<>();
	}
	
	/**
	 * Get the ProductCoverages.
	 * @return
	 */
	public Collection<ProductCoverage> getProductCoverages() {
		Collection<ProductCoverage> result = new ArrayList<>();
		for(ICoverageModelComponent pc : productCoverages) {
			result.add((ProductCoverage)pc);
		}
		return result;
	}
	
	public ProductCoverage getProductCoverage(Map<String, Boolean> featureSet) {
		for(ICoverageModelComponent pc : this.productCoverages) {
			if(((ProductCoverage)pc).getFeatureSet().equals(new FeatureSet(featureSet)))
				return (ProductCoverage)pc;
		}
		return null;
	}
	
	/**
	 * Get the ProductCoverage of the given feature set.
	 * @param featureSet
	 * @return
	 */
	public ProductCoverage getProductCoverage(FeatureSet featureSet) {
		for(ICoverageModelComponent pc : this.productCoverages) {
			if(((ProductCoverage)pc).getFeatureSet().equals(featureSet))
				return (ProductCoverage)pc;
		}
		return null;
	}
	
	
	/**
	 * Add the ProductCoverage.
	 * @param productCoverage
	 */
	public void addProductCoverage(ProductCoverage productCoverage) {
		productCoverages.add(productCoverage);
	}
	
	/**
	 * Get duplicated Products of the given target product.
	 * @param targetProduct		The target to find if the duplicated.
	 * @return					The collection of the duplicated products included given target product.
	 */
	public Collection<ProductCoverage> getDuplicatedProducts(ProductCoverage targetProduct) {
		HashSet<ProductCoverage> toCheck = new HashSet<>();
		ArrayList<ProductCoverage> toReturn = new ArrayList<>();
		toCheck.add(targetProduct);
		
		for(ICoverageModelComponent pc : productCoverages) {
			if(toCheck.contains(pc))
				toReturn.add((ProductCoverage)pc);
		}
		
		if(toReturn.size() == 1)
			return Collections.emptyList();
		
		return toReturn;
	}
	
	/**
	 * Visit to all subclasses with ISPLTCoverageVisitor.
	 * @param visitor		The ISPLTCoverageVisitor defined what to do for each model.
	 */
	public void accept(ISplCoverageVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Collection<IClassCoverage> getClassCoverages() {
		return this.classCoverages;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void addClassCoverages(Collection<IClassCoverage> classCoverages) {
		this.classCoverages.addAll(classCoverages);
	}

	@Override
	public Class[] getTargetClasses() {
		return new Class[] {};
	}

	@Override
	public int getScore() {
		return 0;
	}

	@Override
	public Collection<ICoverageModelComponent> getChildren() {
		return this.productCoverages;
	}

	@Override
	public void addChild(ICoverageModelComponent component) {
		productCoverages.add(component);
	}
	
}
