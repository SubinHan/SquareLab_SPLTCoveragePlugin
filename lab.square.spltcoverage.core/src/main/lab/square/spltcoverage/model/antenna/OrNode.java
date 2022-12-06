package lab.square.spltcoverage.model.antenna;

import lab.square.spltcoverage.model.FeatureSet;

public class OrNode extends ExpressionNode {

	public OrNode(ExpressionNode leftExp, ExpressionNode rightExp) {
		this.left = leftExp;
		this.right = rightExp;
		this.value = "|";
	}
	
	@Override
	public boolean evaluate(FeatureSet featureSet) {
		return this.left.evaluate(featureSet) || this.right.evaluate(featureSet);
	}
}

