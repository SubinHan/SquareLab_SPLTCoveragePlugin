package lab.square.spltcoverage.test;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;

import lab.square.spltcoverage.core.analysis.FeatureSetGroupReader;

public class FeatureSetGroupReaderTest {

	@Test
	public void testRead() {
		FeatureSetGroupReader reader = new FeatureSetGroupReader(
				"D:\\workspacechallenege\\challenge-master\\workspace_IncLing\\Tools\\All_valid_conf\\FeatureAMP2\\products");
		Collection<Map<String, Boolean>> products = reader.readAll();
		
		for(Map<String, Boolean> featureSet : products) {
			for(String feature : featureSet.keySet())
				System.out.println(feature);
			System.out.println("======================");
		}
		
	}
}
