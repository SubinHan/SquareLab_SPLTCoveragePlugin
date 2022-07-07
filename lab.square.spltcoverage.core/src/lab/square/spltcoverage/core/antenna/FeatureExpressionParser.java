package lab.square.spltcoverage.core.antenna;

import java.util.ArrayList;
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
		String[] postfix = shuntingYard(tokens);
		return parseExp0(postfix, new WrapInt(postfix.length-1));
		
		//return parseExp(tokens, new WrapInt(0));
	}
	
	private static String[] shuntingYard(String[] tokens) {
		List<String> result = new ArrayList<>();
		Stack<String> expressions = new Stack<>();
		
		for(String token : tokens) {
			if(token.equals("!")) {
				expressions.push(token);
			} 
			else if(token.equals("&")) {
				while(!expressions.empty()) {
					String expression = expressions.peek();
					if(expression.equals("(") || expression.equals("|")) {
						break;
					}
					result.add(expressions.pop());
				}
				expressions.push(token);
			} 
			else if(token.equals("|")) {
				while(!expressions.empty()) {
					if(expressions.peek().equals("(")) {
						break;
					}
					result.add(expressions.pop());
				}
				expressions.push(token);
			} 
			else if(token.equals("(")) {
				expressions.push(token);
			} 
			else if(token.equals(")")) {
				while(!expressions.peek().equals("(")) {
					assert !expressions.empty();
					result.add(expressions.pop());
				}
				expressions.pop();
			}
			else {
				result.add(token);
			}
		}
		
		while(!expressions.empty()) {
			result.add(expressions.pop());
		}
		
		String[] strArr = new String[result.size()];
		return result.toArray(strArr);
	}
	
	private static ExpressionNode parseExp0(String[] tokens, WrapInt index) throws Exception{
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
