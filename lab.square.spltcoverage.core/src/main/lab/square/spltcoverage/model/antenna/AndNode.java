package lab.square.spltcoverage.model.antenna;

import lab.square.spltcoverage.model.FeatureSet;

public class AndNode extends ExpressionNode {

	public AndNode(ExpressionNode leftExp, ExpressionNode rightExp) {
		this.left = leftExp;
		this.right = rightExp;
		this.value = "&";
	}

	@Override
	public boolean evaluate(FeatureSet featureSet) {
		return this.left.evaluate(featureSet) && this.right.evaluate(featureSet);
	}
}

