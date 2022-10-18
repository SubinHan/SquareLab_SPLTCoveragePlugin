package lab.square.spltcoverage.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import lab.square.spltcoverage.core.antenna.FeatureExpressionParser;
import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.model.antenna.ExpressionNode;

public class FeatureExpressionParserTest {
	
	@Test
	public void testAnd() {
		FeatureSet featureSet = new FeatureSet();
		
		featureSet.setFeature("CallButtons", false);
		featureSet.setFeature("DirectedCall", false);		
		assertFalse(FeatureExpressionParser.evaluate("CallButtons&DirectedCall", featureSet));
		
		featureSet.setFeature("CallButtons", true);
		featureSet.setFeature("DirectedCall", false);		
		assertFalse(FeatureExpressionParser.evaluate("CallButtons&DirectedCall", featureSet));
		
		featureSet.setFeature("CallButtons", false);
		featureSet.setFeature("DirectedCall", true);
		assertFalse(FeatureExpressionParser.evaluate("CallButtons&DirectedCall", featureSet));
		
		featureSet.setFeature("CallButtons", true);
		featureSet.setFeature("DirectedCall", true);
		assertTrue(FeatureExpressionParser.evaluate("CallButtons&DirectedCall", featureSet));
	}
	
	@Test
	public void testOr() {
		FeatureSet featureSet = new FeatureSet();
		
		featureSet.setFeature("CallButtons", false);
		featureSet.setFeature("DirectedCall", false);		
		assertFalse(FeatureExpressionParser.evaluate("CallButtons|DirectedCall", featureSet));
		
		featureSet.setFeature("CallButtons", true);
		featureSet.setFeature("DirectedCall", false);		
		assertTrue(FeatureExpressionParser.evaluate("CallButtons|DirectedCall", featureSet));
		
		featureSet.setFeature("CallButtons", false);
		featureSet.setFeature("DirectedCall", true);
		assertTrue(FeatureExpressionParser.evaluate("CallButtons|DirectedCall", featureSet));
		
		featureSet.setFeature("CallButtons", true);
		featureSet.setFeature("DirectedCall", true);
		assertTrue(FeatureExpressionParser.evaluate("CallButtons|DirectedCall", featureSet));
	}
	
	@Test
	public void testNot() {
		FeatureSet featureSet = new FeatureSet();
		
		featureSet.setFeature("CallButtons", false);
		assertTrue(FeatureExpressionParser.evaluate("!CallButtons", featureSet));
		
		featureSet.setFeature("CallButtons", true);
		assertFalse(FeatureExpressionParser.evaluate("!CallButtons", featureSet));
	}
	
	@Test
	public void testWeired() {
		FeatureSet featureSet = new FeatureSet();
		
		featureSet.setFeature("CallButtons", true);
		featureSet.setFeature("DirectedCall", true);		
		assertTrue(FeatureExpressionParser.evaluate("((((((CallButtons))&(DirectedCall)))))", featureSet));
		
		featureSet.setFeature("CallButtons", true);
		featureSet.setFeature("DirectedCall", false);	
		assertFalse(FeatureExpressionParser.evaluate("((((((CallButtons))&(DirectedCall)))))", featureSet));
	}
	
	@Test
	public void testComplex() {
		FeatureSet featureSet = new FeatureSet();
		
		featureSet.setFeature("CallButtons", true);
		featureSet.setFeature("DirectedCall", false);
		featureSet.setFeature("Sabbath", true);
		featureSet.setFeature("Replace", false);
		featureSet.setFeature("Another", true);
		featureSet.setFeature("Other", true);
		assertTrue(FeatureExpressionParser.evaluate("(!(CallButtons&DirectedCall)&Sabbath|(Replace|Another))&Other", featureSet));
		
		featureSet.setFeature("CallButtons", false);
		featureSet.setFeature("DirectedCall", false);
		featureSet.setFeature("Sabbath", true);
		featureSet.setFeature("Replace", true);
		featureSet.setFeature("Another", true);
		featureSet.setFeature("Other", false);
		assertFalse(FeatureExpressionParser.evaluate("(!(CallButtons&DirectedCall)&Sabbath|(Replace|Another))&Other", featureSet));
		
		featureSet.setFeature("CallButtons", true);
		featureSet.setFeature("DirectedCall", true);
		featureSet.setFeature("Sabbath", false);
		featureSet.setFeature("Replace", false);
		featureSet.setFeature("Another", true);
		featureSet.setFeature("Other", true);
		assertTrue(FeatureExpressionParser.evaluate("(!(CallButtons&DirectedCall)&Sabbath|(Replace|Another))&Other", featureSet));
		
	}
	
	private static void inorder(ExpressionNode node) {
		if (node.getLeft() != null)
			inorder(node.getLeft());
		System.out.println(node.getValue());
		if (node.getRight() != null)
			inorder(node.getRight());
	}

}
