package lab.square.spltcoverage.model.antenna;

import java.util.Objects;

import lab.square.spltcoverage.model.FeatureSet;

public abstract class ExpressionNode {
	protected ExpressionNode left;
	protected ExpressionNode right;
	protected String value;
	
	public abstract boolean evaluate(FeatureSet featureSet);
	
	public String getValue() {
		return this.value;
	}
	
	public ExpressionNode getLeft() {
		return left;
	}
	
	public void setLeft(ExpressionNode left) {
		this.left = left;
	}
	
	public ExpressionNode getRight() {
		return right;
	}
	
	public void setRight(ExpressionNode right) {
		this.right = right;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null)
			return false;
		
		if(!(o instanceof ExpressionNode))
			return false;
		
		ExpressionNode node;
		node = (ExpressionNode)o;
		
		if(!this.value.equals(node.value))
			return false;
		
		if(this.left != null) {
			if(node.left == null)
				return false;
			
			if(!this.left.equals(node.left))
				return false;
		}
		
		if(this.right != null) {
			if(node.right == null)
				return false;
			
			if(!this.right.equals(node.right))
				return false;
		}
		
		return true;
	}

	@Override
	public int hashCode() {
		if(isEndNode()) {
			return Objects.hash(value);
		}
		
		if(this.left == null)
			return this.right.hashCode();
		
		if(this.right == null)
			return this.left.hashCode();
		
		return Objects.hash(this.left.hashCode(), this.right.hashCode());
	}
	
	@Override
	public String toString() {
		String left = "";
		String right = "";
		
		if(this.left != null)
			left = this.left.toString();
		if(this.right != null)
			right = this.right.toString();
		
		return "(" + left + this.value + right + ")";
	}
	
	private boolean isEndNode() {
		return this.left == null && this.right == null;
	}
	
}
