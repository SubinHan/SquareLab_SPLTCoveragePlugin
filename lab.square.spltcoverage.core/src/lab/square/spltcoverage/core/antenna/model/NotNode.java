package lab.square.spltcoverage.core.antenna.model;

public class NotNode extends ExpressionNode {
	public NotNode(ExpressionNode expression) {
		this.left = expression;
		this.value = "!";
	}
}
