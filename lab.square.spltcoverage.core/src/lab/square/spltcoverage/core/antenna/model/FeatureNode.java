package lab.square.spltcoverage.core.antenna.model;

import java.util.Map;

import lab.square.spltcoverage.utils.Tools;

public class FeatureNode extends ExpressionNode {
	
	public FeatureNode(String expression) {
		this.value = expression;
	}
	
	@Override
	public boolean evaluate(Map<String, Boolean> featureSet) {
		return Tools.getBooleanValue(featureSet.get(this.value));
	}
}

