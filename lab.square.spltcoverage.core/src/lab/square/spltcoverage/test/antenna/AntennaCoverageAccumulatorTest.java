package lab.square.spltcoverage.test.antenna;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import lab.square.spltcoverage.core.antenna.AntennaCoverageAccumulator;
import lab.square.spltcoverage.model.antenna.AntennaProductCoverage;

public class AntennaCoverageAccumulatorTest {
	
	private static AntennaCoverageAccumulator accumulator;
	private static AntennaProductCoverage pc1;
	private static AntennaProductCoverage pc2;
	
	@BeforeClass
	public static void setUp() {
		accumulator = new AntennaCoverageAccumulator();
		pc1 = AntennaProductCoverageStub.getStub1();
		pc2 = AntennaProductCoverageStub.getStub2();
	}
	
	@Test
	public void test() {
		assertFalse(accumulator.isCoverageChanged());
		
		accumulator.accumulate(pc1);
		
		assertTrue(accumulator.isCoverageChanged());
			
		accumulator.accumulate(pc2);
		
		assertTrue(accumulator.isCoverageChanged());
	}
}
