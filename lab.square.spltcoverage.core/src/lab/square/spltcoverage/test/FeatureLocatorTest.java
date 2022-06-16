package lab.square.spltcoverage.test;

import java.io.IOException;
import java.util.Collection;

import org.junit.Test;

import lab.square.spltcoverage.core.antenna.FeatureLocation;
import lab.square.spltcoverage.core.antenna.FeatureLocator;

public class FeatureLocatorTest {
	
	@Test
	public void testLocator() throws IOException {
		Collection<FeatureLocation> featureLocations = new FeatureLocator().analyze(
				"D:\\workspace-featureide\\Elevator-Antenna-v1.2-Both\\src\\de\\ovgu\\featureide\\examples\\elevator\\core\\controller\\Request.java");

		for (FeatureLocation featureLocation : featureLocations) {
			for (String feature : featureLocation.getFeatureExpression()) {
				System.out.println(feature);
			}
			System.out.println(FeatureLocation.expressionToString(featureLocation.getFeatureExpression()));
			System.out.println("line: " + featureLocation.getLineStart() + " ~ " + featureLocation.getLineEnd());
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
}
