package lab.square.spltcoverage.core.antenna.model;

public class AndNode extends ExpressionNode {

	public AndNode(ExpressionNode leftExp, ExpressionNode rightExp) {
		this.left = leftExp;
		this.right = rightExp;
		this.value = "&";
	}
}
