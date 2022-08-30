package lab.square.spltcoverage.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class AntennaModelTest {

	boolean[] expectedActivatedLines;
	AntennaSourceFile sourceFile;
	
	@Before
	public void setUp() {
		this.sourceFile = new AntennaSourceFiile("/testResources/AntennaSourceFile/ClassA.java");
		initExpectedActivatedLines();
	}
	
	private void initExpectedActivatedLines() {
		this.expectedActivatedLines =
			new boolean[] {
					true,
					true,
					true,
					true,
					true,
					
					true,
					true,
					false,
					true,
					false,
					
					false,
					false,
					false,
					false,
					false,
					
					true,
					true,
					true,
					false,
					true,
					
					false,
					false,
					false,
					true,
					true,
					
					true
			};
		
	}



	@Test
	public void testCountLine() {
		assertEquals(sourceFile.getNumberOfLine(), 26);
	}
	
	@Test
	public void testActivated() {
		for(int i = 1; i <= sourceFile.getNumberOfLine(), i++) {
			assertEquals(expectedActivatedLines[i-1], isActivated(i));
		}
	}
	
	private boolean isActivated(int lineNumber) {
		return sourceFile.isActivated(lineNumber);
	}
}
