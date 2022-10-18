package lab.square.spltcoverage.test;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Map.Entry;

import org.junit.Test;

import lab.square.spltcoverage.io.FeatureSetGroupReader;
import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.test.target.Configuration;

public class FeatureSetGroupReaderTest {

	@Test
	public void testReadOlder() {
		FeatureSetGroupReader reader = new FeatureSetGroupReader(
				"D:\\workspace_experiment_challenge\\lab.square.spltcoverage.core\\testResources\\productsOlder");
		Collection<FeatureSet> products = reader.readAll();
		
		int configSum = 0;
		int numConfigA = 0;
		int numConfigB = 0;
		int numConfigC = 0;
		
		for(FeatureSet featureSet : products) {
			for(String feature : featureSet.getFeatures()) {
				if(feature.equalsIgnoreCase(Configuration.CONFIG_A)) {
					numConfigA++;
				}
				if(feature.equalsIgnoreCase(Configuration.CONFIG_B)) {
					numConfigB++;
				}
				if(feature.equalsIgnoreCase(Configuration.CONFIG_C)) {
					numConfigC++;
				}
				configSum++;
				System.out.println(feature);
			}
			System.out.println("======================");
		}
		
		assertEquals(5, products.size());
		assertEquals(7, configSum);
		assertEquals(3, numConfigA);
		assertEquals(3, numConfigB);
		assertEquals(1, numConfigC);
	}
	
	@Test
	public void testReadNewer() {
		FeatureSetGroupReader reader = new FeatureSetGroupReader(
				"D:\\workspace_experiment_challenge\\lab.square.spltcoverage.core\\testResources\\productsNewer");
		Collection<FeatureSet> products = reader.readAll();
		
		int configSum = 0;
		int numConfigA = 0;
		int numConfigB = 0;
		int numConfigC = 0;
		
		for(FeatureSet featureSet : products) {
			for(String feature : featureSet.getFeatures()) {
				if(feature.equalsIgnoreCase(Configuration.CONFIG_A)) {
					numConfigA++;
				}
				if(feature.equalsIgnoreCase(Configuration.CONFIG_B)) {
					numConfigB++;
				}
				if(feature.equalsIgnoreCase(Configuration.CONFIG_C)) {
					numConfigC++;
				}
				configSum++;
				System.out.println(feature);
			}
			System.out.println("======================");
		}
		
		assertEquals(5, products.size());
		assertEquals(7, configSum);
		assertEquals(3, numConfigA);
		assertEquals(3, numConfigB);
		assertEquals(1, numConfigC);
	}
}
