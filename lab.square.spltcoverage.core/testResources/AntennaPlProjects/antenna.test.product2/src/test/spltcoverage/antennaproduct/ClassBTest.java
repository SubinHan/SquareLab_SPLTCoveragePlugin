package test.spltcoverage.antennaproduct;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ClassBTest {
	
	private ClassB b;
	
	@Before
	public void setUp() {
		b = new ClassB();
	}
	
	//#if A & B
//@	@Test
//@	public void testMethodB1() {		
//@		assertTrue(b.methodA(3, 5) == 15);
//@	}
	//#endif
	
	//#if A & B
//@	@Test
//@	public void testMethodB2() {		
//@		assertTrue(b.methodB(3, 5) == 9);
//@	}
	//#endif
	
	//#if A & !B
//@	@Test
//@	public void testMethodB3() {		
//@		assertTrue(b.methodB(3, 5) == -3);
//@	}
	//#endif
	
	//#if A & B
//@	@Test
//@	public void testMethodB4() {		
//@		assertTrue(b.methodB(3, 5) == 9);
//@	}
	//#endif
	
	//#if !A & !B
	@Test
	public void testMethodB5() {		
		assertTrue(b.methodB(3, 5) == -1);
	}
	//#endif
	
}
