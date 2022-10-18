package lab.square.spltcoverage.model.antenna;

import lab.square.spltcoverage.model.FeatureSet;

public class NotNode extends ExpressionNode {
	
	public NotNode(ExpressionNode expression) {
		this.left = expression;
		this.value = "!";
	}
	
	@Override
	public boolean evaluate(FeatureSet featureSet) {
		return !this.left.evaluate(featureSet);
	}
}
