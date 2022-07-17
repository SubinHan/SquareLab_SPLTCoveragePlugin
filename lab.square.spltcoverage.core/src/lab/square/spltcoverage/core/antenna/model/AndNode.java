package lab.square.spltcoverage.core.antenna.model;

import java.util.Map;

public class AndNode extends ExpressionNode {

	public AndNode(ExpressionNode leftExp, ExpressionNode rightExp) {
		this.left = leftExp;
		this.right = rightExp;
		this.value = "&";
	}

	@Override
	public boolean evaluate(Map<String, Boolean> featureSet) {
		return this.left.evaluate(featureSet) && this.right.evaluate(featureSet);
	}
}

