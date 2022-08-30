package lab.square.spltcoverage.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import lab.square.spltcoverage.core.antenna.model.AntennaSourceFile;

public class AntennaModelTest {

	boolean[] expectedActivatedLines;
	AntennaSourceFile sourceFile;
	
	@Before
	public void setUp() {
		this.sourceFile = new AntennaSourceFile("testResources/AntennaSourceFile/ClassA.java");
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
		assertEquals(25, sourceFile.getNumberOfLine());
	}
	
	@Test
	public void testActivated() {		
		for(int i = 1; i <= sourceFile.getNumberOfLine(); i++) {
			assertEquals(expectedActivatedLines[i-1], isActivated(i));
		}
	}
	
	private boolean isActivated(int lineNumber) {
		return sourceFile.isActivatedAt(lineNumber);
	}
}
