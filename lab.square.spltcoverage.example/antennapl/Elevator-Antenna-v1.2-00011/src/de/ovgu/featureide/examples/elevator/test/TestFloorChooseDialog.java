package de.ovgu.featureide.examples.elevator.test;

import static org.junit.Assert.assertTrue;

import java.nio.channels.ClosedByInterruptException;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JToggleButton;

import org.fest.swing.core.BasicRobot;
import org.fest.swing.core.GenericTypeMatcher;
import org.fest.swing.core.Robot;
import org.fest.swing.core.matcher.FrameMatcher;
import org.fest.swing.finder.WindowFinder;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import de.ovgu.featureide.examples.elevator.core.controller.ControlUnit;
import de.ovgu.featureide.examples.elevator.sim.SimulationUnit;
//#if FloorPermission
import de.ovgu.featureide.examples.elevator.ui.FloorChooseDialog;
//#endif
import de.ovgu.featureide.examples.elevator.ui.FloorComposite;
import de.ovgu.featureide.examples.elevator.ui.MainWindow;

public class TestFloorChooseDialog {

	SimulationUnit sim;
	MainWindow simulationWindow;
	FrameFixture demo;
	Robot robot;
	ControlUnit controlUnit;
	Thread t;
	boolean a = false;

	@Before
	public void setUp() throws IllegalArgumentException, IllegalAccessException {
		//#ifdef FloorPermission
		robot = BasicRobot.robotWithCurrentAwtHierarchy();
		t = new Thread(new Runnable() {
			public void run() {
				sim = new SimulationUnit();
				simulationWindow = new MainWindow(
						//#if CallButtons | FloorPermission | Service
						sim
						//#endif
						);
				SimulationUnit.simulationWindow = simulationWindow;
				
				sim.start(5);
				
//				FrameFixture fixture = new FrameFinder("Elevator Sample").withTimeout(10000).using(BasicRobot.robotWithCurrentAwtHierarchy());
				Logger logger = Logger.getLogger(TestFloorChooseDialog.class.getName());
			}
		});
		
		t.start();
		//#endif
	}

	//#if FloorPermission
	@Test
	public void testDisableFloor() {
		DialogFixture dialog;

		dialog = WindowFinder.findDialog(FloorChooseDialog.class).withTimeout(10000).using(robot);

		dialog.toggleButton(new GenericTypeMatcher<JToggleButton>(JToggleButton.class) {

			@Override
			protected boolean isMatching(JToggleButton jButton) {
				return "1".equals(jButton.getText());
			}
		}).click();
		dialog.button(new GenericTypeMatcher<JButton>(JButton.class) {

			@Override
			protected boolean isMatching(JButton jButton) {
				return "Submit".equals(jButton.getText());
			}
		}).click();
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		FrameFixture fixture = WindowFinder.findFrame(FrameMatcher.withTitle("Elevator Sample")).withTimeout(1000).using(robot);
		fixture.cleanUp();
		
		Logger logger = Logger.getLogger(TestFloorChooseDialog.class.getName());
		assertTrue(sim.getDisabledFloors().contains(1));
		robot.cleanUp();

		controlUnit = Whitebox.getInternalState(sim, "controller");
		controlUnit.run = false;
	}
	//#else
//@	@Test
//@	public void success() {
//@		;
//@	}
	//#endif

	@After
	public void tearDown() {
	}

}
