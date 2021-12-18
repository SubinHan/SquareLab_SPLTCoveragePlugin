package lab.square.spltcoverage.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.jacoco.core.analysis.IClassCoverage;

public class ProductGraph {
	private Collection<ProductGraph> parents;
	private Collection<ProductGraph> children;
	private Map<String, Boolean> featureSet;
	private ProductCoverage productCoverage;
	private int level;
	
	public ProductGraph() {
		this.parents = new HashSet<ProductGraph>();
		this.children = new HashSet<ProductGraph>();
		this.featureSet = new HashMap<String, Boolean>();
	}
	
	public ProductGraph(Map<String, Boolean> featureSet) {
		this();
		this.featureSet = featureSet;
	}
	
	public ProductGraph(ProductCoverage product) {
		this();
		this.productCoverage = product;
		if(product != null)
			this.featureSet = product.getFeatureSet();
	}
	
	public void addParent(ProductGraph parent) {
		if(!parents.contains(parent))
			parents.add(parent);
	}
	
	public void addChild(ProductGraph child) {
		if(!children.contains(child))
			children.add(child);
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public ProductCoverage getProductCoverage() {
		return this.productCoverage;
	}
	
	public Collection<ProductGraph> getChildren(){
		return this.children;
	}
	
	public Collection<ProductGraph> getParents(){
		return this.parents;
	}
	
	public int getLevel() {
		return this.level;
	}	
	
	public Map<String, Boolean> getFeatureSet(){
		return this.featureSet;
	}
	
	public boolean isCoveredMoreThanParent() {
		double different = 0;
		
		if(this.parents == null)
			return false;
		if(this.parents.isEmpty())
			return false;
		
		for(ProductGraph parent : this.parents) {
			if(parent == null)
				continue;
			different = 0.f;
			for(IClassCoverage cc : this.productCoverage.getClassCoverages()) {
				different += cc.getLineCounter().getCoveredRatio() - findLineRatioWtihClassName(parent, cc);
			}
			if (different <= 0.01f && different >= -0.01f)
				return false;
		}		
		
		return true;
	}

	private double findLineRatioWtihClassName(ProductGraph parent, IClassCoverage cc) {
		Collection<IClassCoverage> parentClassCoverages = parent.getProductCoverage().getClassCoverages();
		
		for(IClassCoverage pcc : parentClassCoverages) {
			if(pcc.getName().equals(cc.getName()))
				return pcc.getLineCounter().getCoveredRatio();
		}
		
		return 0.f;
	}
	

}
