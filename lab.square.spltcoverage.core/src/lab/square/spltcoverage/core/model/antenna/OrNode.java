package lab.square.spltcoverage.core.model.antenna;

import java.util.Map;

public class OrNode extends ExpressionNode {

	public OrNode(ExpressionNode leftExp, ExpressionNode rightExp) {
		this.left = leftExp;
		this.right = rightExp;
		this.value = "|";
	}
	
	@Override
	public boolean evaluate(Map<String, Boolean> featureSet) {
		return this.left.evaluate(featureSet) || this.right.evaluate(featureSet);
	}
}

