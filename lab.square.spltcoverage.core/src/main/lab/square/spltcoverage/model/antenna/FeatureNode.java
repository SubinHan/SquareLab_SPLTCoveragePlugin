package lab.square.spltcoverage.model.antenna;

import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.utils.Tools;

public class FeatureNode extends ExpressionNode {
	
	public FeatureNode(String expression) {
		this.value = expression;
	}
	
	@Override
	public boolean evaluate(FeatureSet featureSet) {
		return featureSet.hasFeature(value);
	}
}

