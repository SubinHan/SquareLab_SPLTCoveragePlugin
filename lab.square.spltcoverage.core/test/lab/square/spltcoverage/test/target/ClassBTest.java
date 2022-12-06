package lab.square.spltcoverage.test.target;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ClassBTest {
	
	private ClassB b;
	
	@Before
	public void setUp() {
		b = new ClassB();
	}
	
	@Test
	public void testMethodB1() {		
		if(Configuration.configA && Configuration.configB) {
			assertTrue(b.methodA(3, 5) == 15);
		}
	}
	
	@Test
	public void testMethodB2() {		
		if(Configuration.configA && Configuration.configB) {
			assertTrue(b.methodB(3, 5) == 9);
		}
	}
	
	@Test
	public void testMethodB3() {		
		if(Configuration.configA && !Configuration.configB) {
			assertTrue(b.methodB(3, 5) == -3);
		}
	}
	
	@Test
	public void testMethodB4() {		
		if(!Configuration.configA && Configuration.configB) {
			assertTrue(b.methodB(3, 5) == 9);
		}
	}
	
	@Test
	public void testMethodB5() {		
		if(!Configuration.configA && !Configuration.configB) {
			assertTrue(b.methodB(3, 5) == -1);
		}
	}
	
}
