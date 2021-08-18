package lab.square.spltcoverage.core.model;

import java.util.Collection;
import java.util.HashSet;

public class ProductGraph {
	private Collection<ProductGraph> parents;
	private Collection<ProductGraph> children;
	private ProductCoverage productCoverage;
	private int level;
	
	public ProductGraph() {
		this.parents = new HashSet<ProductGraph>();
		this.children = new HashSet<ProductGraph>();
	}
	
	public ProductGraph(ProductCoverage product) {
		this();
		this.productCoverage = product;
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
	
}
