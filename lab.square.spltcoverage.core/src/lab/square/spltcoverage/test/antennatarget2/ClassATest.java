package lab.square.spltcoverage.test.antennatarget2;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ClassATest {
	
	private ClassA a;
	
	@Before
	public void setUp() {
		a = new ClassA();
	}
	
	//#if A
//@	@Test
//@	public void testMethodA1() {		
//@		assertTrue(a.methodA());
//@	}
	//#endif
	
	//#if B
//@	@Test
//@	public void testMethodA2() {	
//@		assertTrue(a.methodB(3, 5) == 8);
//@	}
	//#endif
	
	//#ifdef !B
	@Test
	public void testMethodA3() {		
		assertTrue(a.methodB(3, 5) == -2);
	}
	//#endif
}
