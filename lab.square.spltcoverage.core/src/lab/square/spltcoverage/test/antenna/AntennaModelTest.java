package lab.square.spltcoverage.test.antenna;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import lab.square.spltcoverage.model.antenna.AntennaSourceFile;
import lab.square.spltcoverage.model.antenna.AntennaSourceFile.AntennaSourceModelException;

public class AntennaModelTest {

	boolean[] expectedActivatedLines;
	boolean[] expectedActivatedLinesAfterSubtraction;
	boolean[] expectedActivatedLinesAfterIntersection;
	boolean[] expectedActivatedLinesAfterAddition;
	String expectedFileName = "ClassA.java";
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
		initExpectedActivatedLinesAfterIntersection();
		initExpectedActivatedLinesAfterAddition();
	}
	
	private void initExpectedActivatedLinesAfterAddition() {
		this.expectedActivatedLinesAfterAddition =
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
						true,
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
						true,
						false,
						
						true,
						true,
						true
				};
	}
	
	private void initExpectedActivatedLinesAfterIntersection() {
		this.expectedActivatedLinesAfterIntersection =
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
						false,
						
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
						false,
						false,
						false,
						false,
						
						true,
						true,
						true
				};
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
	
	private void assertActivationExpectedAndActualLineByLine(
			boolean[] expected, AntennaSourceFile actual) {
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
	
	@Test(expected = AntennaSourceModelException.class)
	public void testIntersectByDifferenceSourceFile() {
		sourceFile.intersect(differentSourceFile);
	}
	
	@Test
	public void testIntersect() {
		AntennaSourceFile intersected = sourceFile.intersect(anotherSourceFile);
		assertActivationExpectedAndActualLineByLine(expectedActivatedLinesAfterIntersection, intersected);
	}
	
	@Test(expected = AntennaSourceModelException.class)
	public void testAddByDifferenceSourceFile() {
		sourceFile.add(differentSourceFile);
	}
	
	@Test
	public void testAdd() {
		AntennaSourceFile added = sourceFile.add(anotherSourceFile);
		assertActivationExpectedAndActualLineByLine(expectedActivatedLinesAfterAddition, added);
	}
	
	@Test
	public void testGetName() {
		assertEquals(expectedFileName, sourceFile.getFileName());
	}
	
	@Test
	public void testComparingStructure() {
		assertTrue(sourceFile.hasSameStructure(anotherSourceFile));
		assertFalse(sourceFile.hasSameStructure(differentSourceFile));
	}
	
	@Test
	public void testFeatureLocation() {
		assertTrue(sourceFile.isLineFeatureLocationOf(10, "A"));
		assertFalse(sourceFile.isLineFeatureLocationOf(10, "B"));
		assertTrue(sourceFile.isLineFeatureLocationOf(13, "C"));
		assertFalse(sourceFile.isLineFeatureLocationOf(13, "A"));
		assertFalse(sourceFile.isLineFeatureLocationOf(13, Arrays.asList("A", "C")));
	}
	
}
