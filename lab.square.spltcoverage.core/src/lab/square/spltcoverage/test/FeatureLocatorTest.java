package lab.square.spltcoverage.test;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import lab.square.spltcoverage.core.antenna.FeatureLocation;
import lab.square.spltcoverage.core.antenna.FeatureLocator;

public class FeatureLocatorTest {
	
	private static String SOURCE_CODE_PATH = "src/lab/square/spltcoverage/test/antennatarget/ClassA.java";
	
	Map<Integer, String> expectedFeatureLocation;
	
	@Before
	public void setUp() {
		expectedFeatureLocation = new HashMap<>();
		
		expectedFeatureLocation.put(9, "A");
		expectedFeatureLocation.put(11, "!(A)");
		expectedFeatureLocation.put(17, "B");
		expectedFeatureLocation.put(19, "!(B)");
	}
	
	@Test
	public void testLocator() throws IOException {
		Collection<FeatureLocation> featureLocations = new FeatureLocator().analyze(SOURCE_CODE_PATH);

		for (FeatureLocation featureLocation : featureLocations) {
			for (String feature : featureLocation.getFeatureExpression()) {
				System.out.println(feature);
			}
			System.out.println(FeatureLocation.expressionToString(featureLocation.getFeatureExpression()));
			System.out.println("line: " + featureLocation.getLineStart() + " ~ " + featureLocation.getLineEnd());
			verify(featureLocation.getLineStart(), FeatureLocation.expressionToString(featureLocation.getFeatureExpression()));
		}
		
		System.out.println("==========================");
		featureLocations = new FeatureLocator().analyze(
				"D:\\workspace-featureide\\Elevator-Antenna-v1.2-Both\\src\\de\\ovgu\\featureide\\examples\\elevator\\core\\controller\\Request.java");

		for (FeatureLocation featureLocation : featureLocations) {
			for (String feature : featureLocation.getFeatureExpression()) {
				System.out.println(feature);
			}
			System.out.println(FeatureLocation.expressionToString(featureLocation.getFeatureExpression()));
			System.out.println("line: " + featureLocation.getLineStart() + " ~ " + featureLocation.getLineEnd());
		}
	}
	
	private boolean verify(int startLine, String featureExpression) {
		if(!expectedFeatureLocation.containsKey(startLine))
			return false;
		
		if(!expectedFeatureLocation.get(startLine).equals(featureExpression))
			return false;
		
		expectedFeatureLocation.put(startLine, null);
		
		return true;
	}
}
