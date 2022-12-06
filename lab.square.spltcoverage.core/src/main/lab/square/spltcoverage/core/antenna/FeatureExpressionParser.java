package lab.square.spltcoverage.core.antenna;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.model.antenna.AndNode;
import lab.square.spltcoverage.model.antenna.ExpressionNode;
import lab.square.spltcoverage.model.antenna.FeatureLocation;
import lab.square.spltcoverage.model.antenna.FeatureNode;
import lab.square.spltcoverage.model.antenna.NotNode;
import lab.square.spltcoverage.model.antenna.OrNode;

public final class FeatureExpressionParser {
	
	private FeatureExpressionParser() {
	}

	public static ExpressionNode parseByExpressionStack(Stack<String> featureExpressions) {
		String featureExpression = FeatureLocation.expressionToString(featureExpressions);
		List<String> tokens = FeatureExpressionTokenizer.tokenize(featureExpression);
		ExpressionNode result = null;
		result = parseByTokens(tokens.toArray(new String[tokens.size()]));
		
		return result;
	}

	public static ExpressionNode parseByTokens(String... tokens) {
		String[] postfix = shuntingYard(tokens);
		return parseExp0(postfix, new WrapInt(postfix.length-1));
	}
	
	private static String[] shuntingYard(String[] tokens) {
		// Create output queue, operator stack
		List<String> result = new ArrayList<>();
		Stack<String> expressions = new Stack<>();
		
		for(String token : tokens) {
			// Precedence: ! > & > |
			if(token.equals("!")) {
				expressions.push(token);
				continue;
			} 
			
			if(token.equals("&")) {
				while(!expressions.empty()) {
					String expression = expressions.peek();
					if(expression.equals("(") || expression.equals("|")) {
						break;
					}
					result.add(expressions.pop());
				}
				expressions.push(token);
				continue;
			} 
			
			if(token.equals("|")) {
				while(!expressions.empty()) {
					if(expressions.peek().equals("(")) {
						break;
					}
					result.add(expressions.pop());
				}
				expressions.push(token);
				continue;
			} 
			
			if(token.equals("(")) {
				expressions.push(token);
				continue;
			} 
			
			if(token.equals(")")) {
				while(!expressions.peek().equals("(")) {
					assert !expressions.empty();
					result.add(expressions.pop());
				}
				expressions.pop();
				continue;
			}
			
			result.add(token);
		}
		
		while(!expressions.empty()) {
			result.add(expressions.pop());
		}
		
		String[] strArr = new String[result.size()];
		return result.toArray(strArr);
	}
	
	private static ExpressionNode parseExp0(String[] tokens, WrapInt index) {
		String exp = tokens[index.value];
		
		if(exp.equals("!")) {
			index.value--;
			ExpressionNode rightExp = parseExp0(tokens, index);
			return new NotNode(rightExp);
		} 
		else if(exp.equals("&")) {
			index.value--;
			ExpressionNode rightExp = parseExp0(tokens, index);
			index.value--;
			ExpressionNode leftExp = parseExp0(tokens, index);
			return new AndNode(leftExp, rightExp);
		} 
		else if(exp.equals("|")) {
			index.value--;
			ExpressionNode rightExp = parseExp0(tokens, index);
			index.value--;
			ExpressionNode leftExp = parseExp0(tokens, index);
			return new OrNode(leftExp, rightExp);
		}
		else {
			return new FeatureNode(exp);
		}
	}

	public static boolean evaluate(String expression, FeatureSet featureSet) {
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

	public static boolean evaluate(ExpressionNode node, FeatureSet featureSet) {
		return node.evaluate(featureSet);
	}

	private static class WrapInt {
		int value;

		WrapInt(int value) {
			this.value = value;
		}
	}
}
