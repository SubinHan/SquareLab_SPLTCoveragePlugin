package lab.square.spltcoverage.core.antenna;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class FeatureLocation {
	private final File sourceFile;
	private final Stack<String> featureExpressions;
	private final int lineStart;
	private final int lineEnd;

	/**
	 * 
	 * @param sourceFile
	 * @param featureExpressions The stack will be cloned.
	 * @param lineStart
	 * @param lineEnd
	 */
	public FeatureLocation(File sourceFile, Stack<String> featureExpressions, int lineStart, int lineEnd) {
		this.sourceFile = sourceFile;
		this.featureExpressions = (Stack<String>) featureExpressions.clone();
		this.lineStart = lineStart;
		this.lineEnd = lineEnd;
	}

	public File getSourceFile() {
		return sourceFile;
	}

	public Stack<String> getFeatureExpression() {
		return featureExpressions;
	}

	public int getLineStart() {
		return lineStart;
	}

	public int getLineEnd() {
		return lineEnd;
	}

	public boolean isFeatureLocationOf(String feature) {
		return isFeatureLocationOf(feature, expressionToString(this.featureExpressions));
	}
	
	public boolean isFeatureLocationOf(Map<String, Boolean> featureSet) {
		return isFeatureLocationOf(featureSet, expressionToString(this.featureExpressions));
	}
	
	public static boolean isFeatureLocationOf(String feature, String expression) {
		Map<String, Boolean> featureSet = new HashMap<String, Boolean>();
		featureSet.put(feature, true);
		return isFeatureLocationOf(featureSet, expression);
	}
	
	public static boolean isFeatureLocationOf(Map<String, Boolean> featureSet, String expression) {
		return FeatureExpressionParser.evaluate(expression, featureSet);		
	}
	

	public static String expressionToString(Stack<String> featureExpressions) {
		String toReturn = "";
		Stack<String> featureExpressions0 = (Stack<String>)featureExpressions.clone();
		
		
		while (!featureExpressions0.isEmpty()) {
			String popped = featureExpressions0.pop();
			if (toReturn.trim().isEmpty()) {
				toReturn = popped;
			} else {
				toReturn = and(popped, toReturn);
			}
		}

		return toReturn;
	}

	private static String and(String expression1, String expression2) {
		if (expression1.trim().isEmpty())
			return expression2;
		if (expression2.trim().isEmpty())
			return expression1;
		return "(" + expression1 + ")&(" + expression2 + ")";
	}

	public static Stack<String> calculateFeatureExpressionOfLine(Collection<FeatureLocation> featureLocations, int lineNumber) {
		List<Stack<String>> featureExpressions = new ArrayList<Stack<String>>();
		
		for(FeatureLocation fl : featureLocations) {
			if(fl.getLineStart() <= lineNumber && lineNumber <= fl.getLineEnd())
				featureExpressions.add(fl.getFeatureExpression());
		}
		
		Stack<String> result = null;
		int max = 0;
		for(Stack<String> featureExpression : featureExpressions) {
			if(featureExpression.size() > max) {
				max = featureExpression.size();
				result = featureExpression;
			}
		}
		
		return result;
	}
}
