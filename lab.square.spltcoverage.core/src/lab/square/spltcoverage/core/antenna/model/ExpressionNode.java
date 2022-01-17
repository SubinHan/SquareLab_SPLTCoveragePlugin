package lab.square.spltcoverage.core.antenna.model;

public class ExpressionNode {
	protected ExpressionNode left;
	protected ExpressionNode right;
	protected String value;
	
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
	
	
}
