package lab.square.spltcoverage.model.antenna;

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
