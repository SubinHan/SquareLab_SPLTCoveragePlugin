package lab.square.spltclient.example.invarianttarget.testset;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import lab.square.spltclient.example.invarianttarget.elevatorsystem.Actions;
import lab.square.spltclient.example.invarianttarget.elevatorsystem.Elevator;
import lab.square.spltclient.example.invarianttarget.elevatorsystem.Environment;
import lab.square.spltclient.example.invarianttarget.elevatorsystem.Person;

public class ActionsTest {

	private Environment env;
	private Elevator e;
	private Actions a;

	@Before
	public void setUp() {
		env = new Environment(5);
		e = new Elevator(env, 4, false);
		a = new Actions(env, e);
	}

	@Test
	public void testPressInLift0() {
		Environment env = new Environment(5);
		Elevator e = new Elevator(env, 4, false);
		e.enterElevator(new Person("Paulo", 40, 1, 4, env));
		Actions a = new Actions(env, e);

		a.pressInLift0();
		assertTrue(e.floorButtons[0]);
	}

	@Test
	public void testPressInLift0EmptyElevator() {
		Environment env = new Environment(5);
		Elevator e = new Elevator(env, 4, false);

		Actions a = new Actions(env, e);

		a.pressInLift0();
		assertFalse(e.floorButtons[0]);
	}

	@Test
	public void aliceCallTest() {
		Person test = a.aliceCall();
		assertEquals(test.getName(), "alice");
		assertEquals(test.getWeight(), 40);
		assertEquals(test.getDestination(), 0);
		assertEquals(test.getOrigin(), 3);

	}
	@Test
	public void angelinaCallTest() {
		Person test = a.angelinaCall();
		assertEquals(test.getName(), "angelina");
		assertEquals(test.getWeight(), 40);
		assertEquals(test.getDestination(), 1);
		assertEquals(test.getOrigin(), 2);
	}
	@Test
	public void chuckCallTest() {
		Person test = a.chuckCall();
		assertEquals(test.getName(), "chuck");
		assertEquals(test.getWeight(), 40);
		assertEquals(test.getDestination(), 3);
		assertEquals(test.getOrigin(), 1);
	}
	
	@Test
	public void monicaCallTest() {
		Person test = a.monicaCall();
		assertEquals(test.getName(), "monica");
		assertEquals(test.getWeight(), 30);
		assertEquals(test.getDestination(), 1);
		assertEquals(test.getOrigin(), 0);
	}
	
	@Test
	public void bigMacCallTest() {
		Person test = a.bigMacCall();
		assertEquals(test.getName(), "BigMac");
		assertEquals(test.getWeight(), 150);
		assertEquals(test.getDestination(), 3);
		assertEquals(test.getOrigin(), 1);
	}
	
	@Test
	public void pressInLift1() {
		e.enterElevator(new Person("Paulo", 40, 1, 4, env));
		a.pressInLift1();
		assertTrue(e.floorButtons[1]);
	}
	
	@Test
	public void pressInLift2() {
		e.enterElevator(new Person("Paulo", 40, 1, 4, env));
		a.pressInLift2();
		assertTrue(e.floorButtons[2]);
	}

	@Test
	public void pressInLift3() {
		e.enterElevator(new Person("Paulo", 40, 1, 4, env));
		a.pressInLift3();
		assertTrue(e.floorButtons[3]);
	}

	@Test
	public void pressInLift4() {
		e.enterElevator(new Person("Paulo", 40, 1, 4, env));
		a.pressInLift4();
		assertTrue(e.floorButtons[4]);
	}

}
