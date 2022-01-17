package lab.square.spltcoverage.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import lab.square.spltcoverage.core.antenna.FeatureExpressionParser;
import lab.square.spltcoverage.core.antenna.model.ExpressionNode;

public class FeatureExpressionParserrTest {
	
	@Test
	public void print() {
		
	}
	
	@Test
	public void testAnd() {
		Map<String, Boolean> featureSet = new HashMap<String, Boolean>();
		
		featureSet.put("CallButtons", false);
		featureSet.put("DirectedCall", false);		
		assertFalse(FeatureExpressionParser.evaluate("CallButtons&DirectedCall", featureSet));
		
		featureSet.put("CallButtons", true);
		featureSet.put("DirectedCall", false);		
		assertFalse(FeatureExpressionParser.evaluate("CallButtons&DirectedCall", featureSet));
		
		featureSet.put("CallButtons", false);
		featureSet.put("DirectedCall", true);
		assertFalse(FeatureExpressionParser.evaluate("CallButtons&DirectedCall", featureSet));
		
		featureSet.put("CallButtons", true);
		featureSet.put("DirectedCall", true);
		assertTrue(FeatureExpressionParser.evaluate("CallButtons&DirectedCall", featureSet));
	}
	
	@Test
	public void testOr() {
		Map<String, Boolean> featureSet = new HashMap<String, Boolean>();
		
		featureSet.put("CallButtons", false);
		featureSet.put("DirectedCall", false);		
		assertFalse(FeatureExpressionParser.evaluate("CallButtons|DirectedCall", featureSet));
		
		featureSet.put("CallButtons", true);
		featureSet.put("DirectedCall", false);		
		assertTrue(FeatureExpressionParser.evaluate("CallButtons|DirectedCall", featureSet));
		
		featureSet.put("CallButtons", false);
		featureSet.put("DirectedCall", true);
		assertTrue(FeatureExpressionParser.evaluate("CallButtons|DirectedCall", featureSet));
		
		featureSet.put("CallButtons", true);
		featureSet.put("DirectedCall", true);
		assertTrue(FeatureExpressionParser.evaluate("CallButtons|DirectedCall", featureSet));
	}
	
	@Test
	public void testNot() {
		Map<String, Boolean> featureSet = new HashMap<String, Boolean>();
		
		featureSet.put("CallButtons", false);
		assertTrue(FeatureExpressionParser.evaluate("!CallButtons", featureSet));
		
		featureSet.put("CallButtons", true);
		assertFalse(FeatureExpressionParser.evaluate("!CallButtons", featureSet));
	}
	
	@Test
	public void testWeired() {
		Map<String, Boolean> featureSet = new HashMap<String, Boolean>();
		
		featureSet.put("CallButtons", true);
		featureSet.put("DirectedCall", true);		
		assertTrue(FeatureExpressionParser.evaluate("((((((CallButtons))&(DirectedCall)))))", featureSet));
		
		featureSet.put("CallButtons", true);
		featureSet.put("DirectedCall", false);	
		assertFalse(FeatureExpressionParser.evaluate("((((((CallButtons))&(DirectedCall)))))", featureSet));
	}
	
	@Test
	public void testComplex() {
		Map<String, Boolean> featureSet = new HashMap<String, Boolean>();
		
		featureSet.put("CallButtons", true);
		featureSet.put("DirectedCall", false);
		featureSet.put("Sabbath", true);
		featureSet.put("Replace", false);
		featureSet.put("Another", true);
		featureSet.put("Other", true);
		assertTrue(FeatureExpressionParser.evaluate("(!(CallButtons&DirectedCall)&Sabbath|(Replace|Another))&Other", featureSet));
		
		featureSet.put("CallButtons", false);
		featureSet.put("DirectedCall", false);
		featureSet.put("Sabbath", true);
		featureSet.put("Replace", true);
		featureSet.put("Another", true);;
		featureSet.put("Other", false);
		assertFalse(FeatureExpressionParser.evaluate("(!(CallButtons&DirectedCall)&Sabbath|(Replace|Another))&Other", featureSet));
		
		featureSet.put("CallButtons", true);
		featureSet.put("DirectedCall", true);
		featureSet.put("Sabbath", false);
		featureSet.put("Replace", false);
		featureSet.put("Another", true);
		featureSet.put("Other", true);
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
