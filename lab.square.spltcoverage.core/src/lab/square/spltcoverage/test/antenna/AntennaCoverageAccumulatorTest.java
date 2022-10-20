package lab.square.spltcoverage.test.antenna;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import lab.square.spltcoverage.core.antenna.AntennaCoverageAccumulator;
import lab.square.spltcoverage.model.antenna.AntennaProductCoverage;

public class AntennaCoverageAccumulatorTest {
	
	private static final String CLASS_A = "test.spltcoverage.antennaproduct.ClassA";
	private static final String CLASS_B = "test.spltcoverage.antennaproduct.ClassB";
	private static AntennaCoverageAccumulator accumulator;
	private static AntennaProductCoverage pc1;
	private static AntennaProductCoverage pc2;
	
	@Before
	public void setUp() {
		accumulator = new AntennaCoverageAccumulator();
		pc1 = AntennaProductCoverageStub.getStub2();
		pc2 = AntennaProductCoverageStub.getStub1();
	}
	
	@Test
	public void testIsCoverageChanged() {
		assertFalse(accumulator.isCoverageChanged());
		
		accumulator.accumulate(pc1);
		
		assertTrue(accumulator.isCoverageChanged());
			
		accumulator.accumulate(pc2);
		
		assertTrue(accumulator.isCoverageChanged());
	}
	
	@Test
	public void testNewlyVisitedFeatures() {
		accumulator.accumulate(pc1);
		accumulator.accumulate(pc2);
		
		Collection<String> newlyVisitedFeatures = accumulator.getNewlyVisitedFeatures();
		
		assertEquals(2, newlyVisitedFeatures.size());
		assertTrue(newlyVisitedFeatures.contains("A"));
		assertTrue(newlyVisitedFeatures.contains("B"));
	}
	
	@Test
	public void testAccumulatedSameCoverage() {
		accumulator.accumulate(pc1);
		accumulator.accumulate(pc1);
		
		assertFalse(accumulator.isCoverageChanged());
		
		Collection<String> newlyVisitedFeatures = accumulator.getNewlyVisitedFeatures();
		
		assertTrue(newlyVisitedFeatures.isEmpty());
		
		accumulator.accumulate(pc2);
		
		assertTrue(accumulator.isCoverageChanged());
		
		accumulator.accumulate(pc2);
		
		assertFalse(accumulator.isCoverageChanged());
	}
	
	@Test
	public void testNewlyCoveredLineCount() {
		accumulator.accumulate(pc1);
		accumulator.accumulate(pc2);
		
		assertEquals(2, accumulator.getNewlyCoveredLineCountOfClass(CLASS_A));
		assertEquals(2, accumulator.getNewlyCoveredLineCountOfClass(CLASS_B));
	}
	
	@Test
	public void testNewlyCoveredClasses() {
		accumulator.accumulate(pc1);
		accumulator.accumulate(pc2);
		
		assertTrue(accumulator.getNewlyCoveredClaases().contains(CLASS_A));
		assertTrue(accumulator.getNewlyCoveredClaases().contains(CLASS_B));
	}
	
	@Test
	public void testNewlyActivatedLineCount() {
		accumulator.accumulate(pc1);
		accumulator.accumulate(pc2);
		
		assertEquals(2, accumulator.getNewlyActivatedLineCountOfClass(CLASS_A));
		assertEquals(2, accumulator.getNewlyActivatedLineCountOfClass(CLASS_B));	
	}
	
}
