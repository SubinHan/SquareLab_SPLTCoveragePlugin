package lab.square.spltcoverage.test.antenna;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import lab.square.spltcoverage.core.antenna.AntennaCoverageAccumulator;
import lab.square.spltcoverage.model.antenna.AntennaProductCoverage;
import lab.square.spltcoverage.test.TestConfig;

public class AntennaCoverageAccumulatorTest {
	
	private static final String CLASS_A = TestConfig.ANTENNA_CLASS_A_CLASSNAME;
	private static final String CLASS_B = TestConfig.ANTENNA_CLASS_B_CLASSNAME;
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
	public void testNewlyActivatedClasses() {
		accumulator.accumulate(pc1);
		
		assertEquals(4, accumulator.getNewlyActivatedClasses().size());
		
		accumulator.accumulate(pc2);

		assertEquals(4, accumulator.getNewlyActivatedClasses().size());
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
		
		assertTrue(accumulator.getNewlyCoveredClasses().contains(CLASS_A));
		assertTrue(accumulator.getNewlyCoveredClasses().contains(CLASS_B));
	}
	
	@Test
	public void testNewlyActivatedLineCount() {
		accumulator.accumulate(pc1);
		accumulator.accumulate(pc2);
		
		assertEquals(2, accumulator.getNewlyActivatedLineCountOfClass(CLASS_A));
		assertEquals(2, accumulator.getNewlyActivatedLineCountOfClass(CLASS_B));	
	}

	@Test
	public void testTotalActivatedLineCountOfClass() {
		accumulator.accumulate(pc1);
		
		assertEquals(3, accumulator.getTotalActivatedLineCountOfClass(CLASS_A));
		assertEquals(8, accumulator.getTotalActivatedLineCountOfClass(CLASS_B));
		
		accumulator.accumulate(pc2);
		
		assertEquals(5, accumulator.getTotalActivatedLineCountOfClass(CLASS_A));
		assertEquals(9, accumulator.getTotalActivatedLineCountOfClass(CLASS_B));
	}
	
	@Test
	public void testTotalCoveredLineCountOfClass() {
		accumulator.accumulate(pc1);
		
		assertEquals(3, accumulator.getTotalCoveredLineCountOfClass(CLASS_A));
		assertEquals(3, accumulator.getTotalCoveredLineCountOfClass(CLASS_B));
		
		accumulator.accumulate(pc2);
		
		assertEquals(5, accumulator.getTotalCoveredLineCountOfClass(CLASS_A));
		assertEquals(5, accumulator.getTotalCoveredLineCountOfClass(CLASS_B));
	}
	
	@Test
	public void testTotalActivatedLineCount() {
		accumulator.accumulate(pc1);
		
		assertEquals(21, accumulator.getTotalActivatedLine());
		
		accumulator.accumulate(pc2);
		
		assertEquals(34, accumulator.getTotalActivatedLine());
	}	
	
	@Test
	public void testTotalCoveredLineCount() {
		accumulator.accumulate(pc1);
		
		assertEquals(14, accumulator.getTotalCoveredLine());
		
		accumulator.accumulate(pc2);
		
		assertEquals(24, accumulator.getTotalCoveredLine());
	}
	
	@Test
	public void testNewlyCoveredLineNumbers() {
		accumulator.accumulate(pc1);
		
		Set<Integer> newlyCoveredLineNumbersOfClassA = accumulator.getNewlyCoveredLineNumbersOfClass(CLASS_A);
		Set<Integer> newlyCoveredLineNumbersOfClassB = accumulator.getNewlyCoveredLineNumbersOfClass(CLASS_B);

		assertTrue(newlyCoveredLineNumbersOfClassA.contains(9));
		assertTrue(newlyCoveredLineNumbersOfClassA.contains(17));
		
		assertTrue(newlyCoveredLineNumbersOfClassB.contains(15));
		assertTrue(newlyCoveredLineNumbersOfClassB.contains(18));
		
		accumulator.accumulate(pc2);
		
		newlyCoveredLineNumbersOfClassA = accumulator.getNewlyCoveredLineNumbersOfClass(CLASS_A);
		newlyCoveredLineNumbersOfClassB = accumulator.getNewlyCoveredLineNumbersOfClass(CLASS_B);
		
		assertTrue(newlyCoveredLineNumbersOfClassA.contains(7));
		assertTrue(newlyCoveredLineNumbersOfClassA.contains(15));
		
		assertTrue(newlyCoveredLineNumbersOfClassB.contains(6));
		assertTrue(newlyCoveredLineNumbersOfClassB.contains(17));
	}
	
	
}
