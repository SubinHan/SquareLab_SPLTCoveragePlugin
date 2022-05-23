package lab.square.spltcoverage.test;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;

import lab.square.spltcoverage.io.FeatureSetGroupReader;
import lab.square.spltcoverage.test.target.Configuration;

public class FeatureSetGroupReaderTest {

	@Test
	public void testReadOlder() {
		FeatureSetGroupReader reader = new FeatureSetGroupReader(
				"D:\\workspace_experiment_challenge\\lab.square.spltcoverage.core\\src\\lab\\square\\spltcoverage\\test\\target\\productsOlder");
		Collection<Map<String, Boolean>> products = reader.readAll();
		
		int configSum = 0;
		int numConfigA = 0;
		int numConfigB = 0;
		int numConfigC = 0;
		
		for(Map<String, Boolean> featureSet : products) {
			for(String feature : featureSet.keySet()) {
				if(!featureSet.get(feature))
					continue;
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
				"D:\\workspace_experiment_challenge\\lab.square.spltcoverage.core\\src\\lab\\square\\spltcoverage\\test\\target\\productsNewer");
		Collection<Map<String, Boolean>> products = reader.readAll();
		
		int configSum = 0;
		int numConfigA = 0;
		int numConfigB = 0;
		int numConfigC = 0;
		
		for(Map<String, Boolean> featureSet : products) {
			for(String feature : featureSet.keySet()) {
				if(!featureSet.get(feature))
					continue;
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
