package lab.square.spltcoverage.core.antenna;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import lab.square.spltcoverage.core.antenna.model.AndNode;
import lab.square.spltcoverage.core.antenna.model.ExpressionNode;
import lab.square.spltcoverage.core.antenna.model.FeatureNode;
import lab.square.spltcoverage.core.antenna.model.NotNode;
import lab.square.spltcoverage.core.antenna.model.OrNode;
import lab.square.spltcoverage.utils.Tools;

public class FeatureExpressionParser {

	public static ExpressionNode parseByExpressionStack(Stack<String> featureExpressions) {
		String featureExpression = FeatureLocation.expressionToString(featureExpressions);
		List<String> tokens = FeatureExpressionTokenizer.tokenize(featureExpression);
		ExpressionNode result = new ExpressionNode();
		try {
			result = parseByTokens(tokens.toArray(new String[tokens.size()]));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public static ExpressionNode parseByTokens(String... tokens) throws Exception {
		return parseExp(tokens, new WrapInt(0));
	}

	private static ExpressionNode parseExp(String[] tokens, WrapInt index) throws Exception {

		ExpressionNode leftExp = parseSubExp(tokens, index);
		if (index.value >= tokens.length) {
			return leftExp;
		}

		String token = tokens[index.value];
		if (token.equals("&")) {
			index.value++;
			ExpressionNode rightExp = parseExp(tokens, index);
			return new AndNode(leftExp, rightExp);
		} else if (token.equals("|")) {
			index.value++;
			ExpressionNode rightExp = parseExp(tokens, index);
			return new OrNode(leftExp, rightExp);
		} else if (token.equals("!")) {
			return new NotNode(leftExp);
		} else if (token.equals(")")) {
			return leftExp;
		} else {
			throw new Exception("Expected '&', '|' or '!'");
		}
	}

	public static boolean evaluate(String expression, Map<String, Boolean> featureSet) {
		ExpressionNode root;
		boolean result = false;
		List<String> tokens = FeatureExpressionTokenizer.tokenize(expression);
		String[] stringTokens = new String[tokens.size()];
		tokens.toArray(stringTokens);
		try {
			root = parseByTokens(stringTokens);
			result = evaluate(root, featureSet);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static boolean evaluate(ExpressionNode node, Map<String, Boolean> featureSet) {
		if (node.getValue().equals("&")) {
			return evaluate(node.getLeft(), featureSet) && evaluate(node.getRight(), featureSet);
		} else if (node.getValue().equals("|")) {
			return evaluate(node.getLeft(), featureSet) || evaluate(node.getRight(), featureSet);
		} else if (node.getValue().equals("!")) {
			return !evaluate(node.getLeft(), featureSet);
		} else {
			return Tools.getBooleanValue(featureSet.get(node.getValue()));
		}
	}

	private static ExpressionNode parseSubExp(String[] tokens, WrapInt index) throws Exception {
		String token = tokens[index.value];
		if (token.equals("(")) {
			index.value++;
			ExpressionNode node = parseExp(tokens, index);
			if (!tokens[index.value].equals(")"))
				throw new Exception("Expected ')'");

			index.value++;
			return node;
		} else if (token.equals("!")) {
			index.value++;
			ExpressionNode leftExp = parseSubExp(tokens, index);
			return new NotNode(leftExp);
		} else {
			index.value++;
			return new FeatureNode(token);
		}
	}

	private static class WrapInt {
		int value;

		WrapInt(int value) {
			this.value = value;
		}
	}
}
