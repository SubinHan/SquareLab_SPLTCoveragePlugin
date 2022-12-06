package lab.square.spltcoverage.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import lab.square.spltcoverage.model.FeatureSet;

public class FeatureSetTest {
	
	private static FeatureSet featureSetConstructedByMap;
	private static FeatureSet featureSetConstructedByAdding;
	private static FeatureSet featureSetHasOnlyTrue;
	
	private static FeatureSet featureSetHasAAndB;
	private static FeatureSet featureSetHasBAndC;
	
	@BeforeClass
	public static void setUp() {
		Map<String, Boolean> featureMap = new HashMap<>();
		featureMap.put("A", true);
		featureMap.put("B", false);
		
		featureSetConstructedByMap = new FeatureSet(featureMap);
		
		featureSetConstructedByAdding = new FeatureSet();
		featureSetConstructedByAdding.addFeature("A");
		featureSetConstructedByAdding.setFeature("B", false);
		
		featureSetHasOnlyTrue = new FeatureSet();
		featureSetHasOnlyTrue.addFeature("A");
		
		featureSetHasAAndB = new FeatureSet();
		featureSetHasAAndB.addFeature("A");
		featureSetHasAAndB.addFeature("B");
		
		featureSetHasBAndC = new FeatureSet();
		featureSetHasBAndC.addFeature("B");
		featureSetHasBAndC.addFeature("C");
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
	
	@Test
	public void testGetFeaturesConstructedByAdding() {
		Collection<String> features = featureSetConstructedByAdding.getFeatures();
		
		assertTrue(features.contains("A"));
		assertFalse(features.contains("B"));
	}
	
	@Test
	public void testGetFeaturesConstructedByMap() {
		Collection<String> features = featureSetConstructedByMap.getFeatures();
		
		assertTrue(features.contains("A"));
		assertFalse(features.contains("B"));
	}
	
	@Test
	public void testGetFeaturesHasOnlyTrue() {
		Collection<String> features = featureSetHasOnlyTrue.getFeatures();
		
		assertTrue(features.contains("A"));
		assertFalse(features.contains("B"));
	}
	
	@Test
	public void testAdd() {
		FeatureSet added = featureSetHasAAndB.add(featureSetHasBAndC);
		
		assertTrue(added.hasFeature("A"));
		assertTrue(added.hasFeature("B"));
		assertTrue(added.hasFeature("C"));
	}
	
	@Test
	public void testSubtract1() {
		FeatureSet subtracted = featureSetHasAAndB.subtract(featureSetHasBAndC);
		
		assertTrue(subtracted.hasFeature("A"));
		assertFalse(subtracted.hasFeature("B"));
		assertFalse(subtracted.hasFeature("C"));
	}

	@Test
	public void testSubtract2() {
		FeatureSet subtracted = featureSetHasBAndC.subtract(featureSetHasAAndB);
		
		assertFalse(subtracted.hasFeature("A"));
		assertFalse(subtracted.hasFeature("B"));
		assertTrue(subtracted.hasFeature("C"));
	}
	
	@Test
	public void testIntersect() {
		FeatureSet intersected = featureSetHasAAndB.intersect(featureSetHasBAndC);
		
		assertFalse(intersected.hasFeature("A"));
		assertTrue(intersected.hasFeature("B"));
		assertFalse(intersected.hasFeature("C"));
	}
	
	@Test
	public void testNumFeatures() {
		assertEquals(1, featureSetConstructedByAdding.getNumFeatures());
		assertEquals(2, featureSetHasAAndB.getNumFeatures());
	}
	
	@Test
	public void testGetHowManyDifferences() {
		assertEquals(2, featureSetHasAAndB.getHowManyDifferences(featureSetHasBAndC));
		assertEquals(0, featureSetConstructedByAdding.getHowManyDifferences(featureSetHasOnlyTrue));
		assertEquals(1, featureSetHasAAndB.getHowManyDifferences(featureSetConstructedByAdding));
		assertEquals(0, featureSetConstructedByAdding.getHowManyDifferences(featureSetConstructedByAdding));
	}
	
	@Test
	public void testToString() {
		Map<String, Boolean> map = new HashMap<>();
		map.put("A", true);
		map.put("B", true);
		
		assertTrue(map.toString().equals(featureSetHasAAndB.toString()));
	}
}
