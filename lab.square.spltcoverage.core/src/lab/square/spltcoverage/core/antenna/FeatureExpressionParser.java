package lab.square.spltcoverage.core.antenna;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import lab.square.spltcoverage.core.antenna.model.AndNode;
import lab.square.spltcoverage.core.antenna.model.ExpressionNode;
import lab.square.spltcoverage.core.antenna.model.FeatureNode;
import lab.square.spltcoverage.core.antenna.model.NotNode;
import lab.square.spltcoverage.core.antenna.model.OrNode;

public class FeatureExpressionParser {

	private Stack<String> expressionStack;
	private String featureExpression;

	public FeatureExpressionParser(String featureExpressions) {
		this.featureExpression = featureExpressions;
		this.expressionStack = stringToStack(featureExpressions);
	}

	public FeatureExpressionParser(Stack<String> featureExpressions) {
		this.expressionStack = featureExpressions;
		this.featureExpression = stackToString(featureExpressions);
	}

	private Stack<String> stringToStack(String featureExpression) {
		// TODO : Complete this.
		List<String> tokens = FeatureExpressionTokenizer.tokenize(featureExpression);

		for (String token : tokens) {
			System.out.println(token);
		}

		return null;
	}

	private String stackToString(Stack<String> featureExpressions) {
		String toReturn = "";

		for (String feature : featureExpressions) {
			if (toReturn.trim().isEmpty())
				toReturn = feature;
			else
				toReturn = "(" + toReturn + ")&(" + feature + ")";
		}

		return toReturn;
	}

	public static ExpressionNode parse(String... tokens) throws Exception {
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
			root = parse(stringTokens);
			result = evaluate(root, featureSet);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	private static boolean evaluate(ExpressionNode node, Map<String, Boolean> featureSet) {
		if (node.getValue().equals("&")) {
			return evaluate(node.getLeft(), featureSet) && evaluate(node.getRight(), featureSet);
		} else if (node.getValue().equals("|")) {
			return evaluate(node.getLeft(), featureSet) || evaluate(node.getRight(), featureSet);
		} else if (node.getValue().equals("!")) {
			return !evaluate(node.getLeft(), featureSet);
		} else {
			return featureSet.get(node.getValue());
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
