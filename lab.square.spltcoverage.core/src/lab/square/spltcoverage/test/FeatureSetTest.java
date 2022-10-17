package lab.square.spltcoverage.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import lab.square.spltcoverage.model.FeatureSet;

public class FeatureSetTest {
	
	private FeatureSet featureSetConstructedByMap;
	private FeatureSet featureSetConstructedByAdding;
	private FeatureSet featureSetHasOnlyTrue;
	
	@Before
	public void setUp() {
		Map<String, Boolean> featureMap = new HashMap<>();
		featureMap.put("A", true);
		featureMap.put("B", false);
		
		featureSetConstructedByMap = new FeatureSet(featureMap);
		
		featureSetConstructedByAdding = new FeatureSet();
		featureSetConstructedByAdding.addFeature("A");
		featureSetConstructedByAdding.addFeature("B", false);
		
		featureSetHasOnlyTrue = new FeatureSet();
		featureSetHasOnlyTrue.addFeature("A");
	}
	
	@Test
	public void testHasFeatureConstructedByMap() {
		assertTrue(featureSetConstructedByMap.hasFeature("A"));
		assertFalse(featureSetConstructedByMap.hasFeature("B"));
		assertFalse(featureSetConstructedByMap.hasFeature("aNoMaly"));
		
	}
	
	@Test
	public void testHasFeatureConstructedByAdding() {
		assertTrue(featureSetConstructedByAdding.hasFeature("A"));
		assertFalse(featureSetConstructedByAdding.hasFeature("B"));
		assertFalse(featureSetConstructedByAdding.hasFeature("aNoMaly"));
	}
	
	@Test
	public void testEquals() {
		assertEquals(featureSetConstructedByMap, featureSetConstructedByAdding);
	}
	
	@Test
	public void testHashCode1() {
		Set<FeatureSet> featureSets = new HashSet<>();
		featureSets.add(featureSetConstructedByAdding);
		assertTrue(featureSets.contains(featureSetConstructedByMap));
		assertTrue(featureSets.contains(featureSetHasOnlyTrue));
	}
	
	@Test
	public void testHashCode2() {
		Set<FeatureSet> featureSets = new HashSet<>();
		featureSets.add(featureSetHasOnlyTrue);
		assertTrue(featureSets.contains(featureSetConstructedByMap));
		assertTrue(featureSets.contains(featureSetConstructedByAdding));
	}
}
