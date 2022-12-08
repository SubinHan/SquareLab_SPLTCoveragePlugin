package de.ovgu.featureide.examples.elevator.test;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JToggleButton;

import org.fest.swing.core.BasicRobot;
import org.fest.swing.core.Robot;
import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import de.ovgu.featureide.examples.elevator.sim.SimulationUnit;
import de.ovgu.featureide.examples.elevator.ui.FloorComposite;
import de.ovgu.featureide.examples.elevator.ui.MainWindow;

public class TestRequest {
	SimulationUnit sim;
	MainWindow simulationWindow;
	FrameFixture demo;
	Robot robot;
	List<FloorComposite> listFloorComposites;

	@Before
	public void setUp() throws IllegalArgumentException, IllegalAccessException {

		robot = BasicRobot.robotWithCurrentAwtHierarchy();
		new Thread(new Runnable() {
			public void run() {
				sim = new SimulationUnit();
				simulationWindow = new MainWindow(
						//#if CallButtons | FloorPermission | Service
//@						sim
						//#endif
						);
				SimulationUnit.simulationWindow = simulationWindow;
				sim.start(5);

				JFrame g = null;
				try {
					g = Whitebox.getInternalState(simulationWindow, "frmElevatorSample");
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}

				demo = new FrameFixture(g);
			}
		}).start();

	}

	@Test
	public void testRequest() {
		listFloorComposites = Whitebox.getInternalState(simulationWindow, "listFloorComposites");

		FloorComposite floorComposite = listFloorComposites.get(4);

		JToggleButton upButton = Whitebox.getInternalState(floorComposite, "btnFloorUp");
	}

	@After
	public void tearDown() {
		if (demo != null)
			demo.cleanUp();
	}

}
