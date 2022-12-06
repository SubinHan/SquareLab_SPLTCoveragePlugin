package lab.square.spltcoverage.test.target;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ClassATest {
	
	private ClassA a;
	
	@Before
	public void setUp() {
		a = new ClassA();
	}
	
	@Test
	public void testMethodA1() {		
		if(Configuration.configA) {
			assertTrue(a.methodA());
		}
	}
	
	@Test
	public void testMethodA2() {		
		if(Configuration.configB) {
			assertTrue(a.methodB(3, 5) == 8);
		}
	}
	
	@Test
	public void testMethodA3() {		
		if(!Configuration.configB) {
			assertTrue(a.methodB(3, 5) == -2);
		}
	}
}
