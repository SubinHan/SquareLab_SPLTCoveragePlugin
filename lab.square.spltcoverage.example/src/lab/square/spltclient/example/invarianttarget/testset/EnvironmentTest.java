package lab.square.spltclient.example.invarianttarget.testset;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import lab.square.spltclient.example.invarianttarget.elevatorsystem.Environment;
import lab.square.spltclient.example.invarianttarget.elevatorsystem.Floor;

public class EnvironmentTest extends TestCase {

	Environment env;
	@Before
	public void setUp(){
		
	}
	
	@Test
	public void testElevatorFloors () {
		env= new Environment(10);
		assertTrue(env.getFloors().length==10);
	}
	
	@Test
	public void testFloors () {
		env= new Environment(10);
		boolean control =true;
		for (Floor floor : env.getFloors()) {
			if(!(floor instanceof Floor)){
				control=false;
			}
		}
		assertTrue(control);
	}

	@Test
	public void testIsTopFloor () {
		env= new Environment(10);
		
		assertTrue(env.isTopFloor(9));
	}
	
	@Test
	public void testGetFloor () {
		env= new Environment(10);
		boolean control =true;
		for (int i = 0; i < env.getFloors().length; i++) {
			
			if(env.getFloor(i).getFloorID()!=i){
				control =false;
			}
		}
		assertTrue(control);
	}
}
