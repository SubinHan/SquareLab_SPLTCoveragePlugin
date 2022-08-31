package lab.square.spltcoverage.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import lab.square.spltcoverage.core.antenna.model.AntennaSourceFile;

public class AntennaModelTest {

	boolean[] expectedActivatedLines;
	boolean[] expectedActivatedLinesAfterSubtraction;
	AntennaSourceFile sourceFile;
	AntennaSourceFile anotherSourceFile;
	AntennaSourceFile differentSourceFile;
	
	@Before
	public void setUp() {
		this.sourceFile = new AntennaSourceFile("testResources/AntennaSourceFile/ClassA.java");
		this.anotherSourceFile = new AntennaSourceFile("testResources/AntennaSourceFile/ClassA2.java");
		this.differentSourceFile = new AntennaSourceFile("testResources/AntennaSourceFile/ClassB.java");
		initExpectedActivatedLines();
		initExpectedActivatedLinesAfterSubtraction();
	}
	
	private void initExpectedActivatedLinesAfterSubtraction() {
		this.expectedActivatedLinesAfterSubtraction =
				new boolean[] {
						false,
						false,
						false,
						false,
						false,
						
						false,
						false,
						false,
						false,
						true,
						
						false,
						false,
						false,
						false,
						false,
						
						false,
						false,
						false,
						false,
						false,
						
						false,
						true,
						false,
						false,
						false,
						
						false,
						false,
						false
				};
		
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
		assertEquals(27, sourceFile.getNumberOfLine());
	}
	
	@Test
	public void testActivated() {
		assertActivationExpectedAndActualLineByLine(expectedActivatedLines, sourceFile);
	}
	
	private void assertActivationExpectedAndActualLineByLine(boolean[] expected, AntennaSourceFile actual) {
		for(int i = 1; i <= actual.getNumberOfLine(); i++) {
			assertEquals(expected[i-1], actual.isActivatedAt(i));
		}
	}
	
	@Test(expected = AntennaSourceModelException.class)
	public void testSubtractByDifferentSourceFile() {
		sourceFile.subtract(differentSourceFile);
	}
	
	@Test
	public void testSubtract() {
		AntennaSourceFile subtracted = sourceFile.subtract(anotherSourceFile);
		assertActivationExpectedAndActualLineByLine(expectedActivatedLinesAfterSubtraction, subtracted);;
	}
	
}
