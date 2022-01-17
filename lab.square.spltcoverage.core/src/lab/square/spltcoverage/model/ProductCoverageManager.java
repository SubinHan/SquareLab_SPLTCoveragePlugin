package lab.square.spltcoverage.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.jacoco.core.analysis.IClassCoverage;

/**
 * The ProductCoverageManager class is a class containing the ProductCoverages.
 * @author SQUARELAB
 */
public class ProductCoverageManager {
	private List<IClassCoverage> classCoverages;
	private List<ProductCoverage> productCoverages;
	
	/**
	 * Create an empty ProductCoverageManager.
	 */
	public ProductCoverageManager() {
		productCoverages = new ArrayList<ProductCoverage>();
	}
	
	/**
	 * Get the ProductCoverage of the given feature set.
	 * @param featureSet
	 * @return
	 */
	public ProductCoverage getProductCoverage(Map<String, Boolean> featureSet) {
		for(ProductCoverage pc : this.productCoverages) {
			if(pc.getFeatureSet().equals(featureSet))
				return pc;
		}
		return null;
	}
	
	/**
	 * Get all the ProductCoverage.
	 * @return
	 */
	public List<ProductCoverage> getProductCoverages() {
		return productCoverages;
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
		HashSet<ProductCoverage> toCheck = new HashSet<ProductCoverage>();
		ArrayList<ProductCoverage> toReturn = new ArrayList<ProductCoverage>();
		toCheck.add(targetProduct);
		
		for(ProductCoverage pc : productCoverages) {
			if(toCheck.contains(pc))
				toReturn.add(pc);
		}
		
		if(toReturn.size() == 1)
			return null;
		
		return toReturn;
	}
	
	/**
	 * Visit to all subclasses with ISPLTCoverageVisitor.
	 * @param visitor		The ISPLTCoverageVisitor defined what to do for each model.
	 */
	public void accept(ISpltCoverageVisitor visitor) {
		visitor.visit(this);
	}
	
}
