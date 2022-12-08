package lab.square.spltclient;

import lab.square.spltcoverage.core.antenna.FeatureExpressionTokenizer;

public class FeatureExpressionTokenizerExample {
	public static void main(String[] args) {
		String featureExpression = "A&Bb|Cccc&!D|(E|F)";
		for (String token : FeatureExpressionTokenizer.tokenize(featureExpression)) {
			System.out.println(token);
		}
	}
}
