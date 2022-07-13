package lab.square.spltcoverage.core.antenna.model;

import java.util.Map;

public class NotNode extends ExpressionNode {
	
	public NotNode(ExpressionNode expression) {
		this.left = expression;
		this.value = "!";
	}
	
	@Override
	public boolean evaluate(Map<String, Boolean> featureSet) {
		return !this.left.evaluate(featureSet);
	}
}
