package lab.square.spltcoverage.test.antenna;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;

import lab.square.spltcoverage.model.antenna.FeatureLocation;
import lab.square.spltcoverage.model.antenna.ProductFeatureLocation;

public class ProductFeatureLocationTest {

	private static final String JAVA_SOURCE_PATH = "src/";
	private static final String TARGET_CLASS_A = "lab.square.spltcoverage.test.antennatarget.ClassA";
	
	private static final String JAVA_SOURCE_PATH2 = "testResources/AntennaSourceFile";
	private static final String TARGET_CLASS_A2 = "ClassA";
	
	private static ProductFeatureLocation fl1;
	private static ProductFeatureLocation fl2;
	
	@BeforeClass
	public static void setUp() {
		fl1 = ProductFeatureLocation.createAndLogIfException(JAVA_SOURCE_PATH);
		fl2 = ProductFeatureLocation.createAndLogIfException(JAVA_SOURCE_PATH2);
	}
	
	@Test
	public void testGetFeatureLocations1() {
		Collection<FeatureLocation> featureLocations = fl1.getFeatureLocationsOfClass(TARGET_CLASS_A);
		assertNotNull(featureLocations);
		
		assertEquals(4, featureLocations.size());
	}
	
	@Test
	public void testGetFeatureLocations2() {
		Collection<FeatureLocation> featureLocations = fl2.getFeatureLocationsOfClass(TARGET_CLASS_A2);
		assertNotNull(featureLocations);
		
		assertEquals(5, featureLocations.size());
	}
}
