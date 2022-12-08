package lab.square.spltclient.example;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import lab.square.spltclient.example.invarianttarget.Combinations.Combination;
import lab.square.spltclient.example.invarianttarget.Combinations.FeatureCombination;
import lab.square.spltclient.example.invarianttarget.Main.ProductGeneration;
import lab.square.spltclient.example.invarianttarget.Main.TargetSystem;
import lab.square.spltclient.example.invarianttarget.elevatorsystem.Actions;
import lab.square.spltclient.example.invarianttarget.elevatorsystem.Elevator;
import lab.square.spltclient.example.invarianttarget.elevatorsystem.Environment;
import lab.square.spltclient.example.invarianttarget.elevatorsystem.Floor;
import lab.square.spltclient.example.invarianttarget.elevatorsystem.Person;
import lab.square.spltclient.example.invarianttarget.specifications.Configuration;
import lab.square.spltclient.example.invarianttarget.testset.ActionsTest;
import lab.square.spltclient.example.invarianttarget.testset.ElevatorTest;
import lab.square.spltclient.example.invarianttarget.testset.EnvironmentTest;
import lab.square.spltclient.example.invarianttarget.testset.FloorTest;
import lab.square.spltclient.example.invarianttarget.testset.MainTest;
import lab.square.spltclient.example.invarianttarget.testset.PersonTest;
import lab.square.spltcoverage.core.analysis.IIterableSpltProvider;
import lab.square.spltcoverage.core.analysis.SplCoverageGenerator;
import lab.square.spltcoverage.model.FeatureSet;

public class Ex5GenerateInvariantPlCoverage {

	public static void main(String[] args) {

		// This is an invariant PL that the configuration depends on runtime variables
		// (lab.square.spltclient.example.invarianttarget.specifications.Configuration)
		//
		// In the variable PL, we would just pass the source code directories of each product,
		// but this case is more complex because It's hard to pass the mechanism
		// to change configurations, to SpltCoverage library.
		// So we should insert code that creates coverages in source code base directly.
		//
		// This PL is from "Testing Configurable Software Systems: The Failure Observation Challenge"
		
		ProductGeneration products = new ProductGeneration();
		products.run(TargetSystem.ELEVATOR, "valid_configs");

		// We should create IIterableSpltProvider to generate coverages.
		
		// !!IMPORTANT!!
		// We need the JaCoCo Java agent and set the VM arguments:
		// -javaagent:jacocoagent.jar=jmx=true 
		// -Dcom.sun.management.jmxremote 
		// -Dcom.sun.management.jmxremote.port=7777
		// -Dcom.sun.management.jmxremote.authenticate=false
		// -Dcom.sun.management.jmxremote.ssl=false
		// -Djava.rmi.server.hostname=localhost
		
		SplCoverageGenerator generator = new SplCoverageGenerator();
		generator.generateCoverage(new SpltProvider(products));
	}
	
	private static class SpltProvider implements IIterableSpltProvider {

		private Iterator<Combination> iter;

		public SpltProvider(ProductGeneration products) {

			iter = products.getCombsForTest().iterator();
		}
		
		@Override
		public boolean makeNextProduct() {
			do {
				if (iter.hasNext()) {
					Combination combination = iter.next();
					for (FeatureCombination f : combination.getListFeatures()) {
						if (f.getName().equals("Base")) {
							Configuration.base = (f.getState().equals("0") ? false : true);
						}
						if (f.getName().equals("Weight")) {
							Configuration.weight = (f.getState().equals("0") ? false : true);
						}
						if (f.getName().equals("Empty")) {
							Configuration.empty = (f.getState().equals("0") ? false : true);
						}
						if (f.getName().equals("TwoThirdsFull")) {
							Configuration.twothirdsfull = (f.getState().equals("0") ? false : true);
						}
						if (f.getName().equals("ExecutiveFloor")) {
							Configuration.executivefloor = (f.getState().equals("0") ? false : true);
						}
						if (f.getName().equals("Overloaded")) {
							Configuration.overloaded = (f.getState().equals("0") ? false : true);
						}
					}
				} else {
					return false;
				}
			} while (!Configuration.validProduct());

			return true;
		}

		@Override
		public Class[] getTargetClasses() {
			Class[] toReturn = new Class[] { Actions.class, Elevator.class, Environment.class, Floor.class,
					Person.class };
			return toReturn;
		}

		@Override
		public Class[] getTestClasses() {
			Class[] toReturn = new Class[] { ActionsTest.class, ElevatorTest.class, EnvironmentTest.class,
					FloorTest.class, MainTest.class, PersonTest.class };
			return toReturn;
		}

		@Override
		public String getBaseDirectory() {
			return "spltoutput/invariantpl";
		}

		@Override
		public FeatureSet getFeatureSet() {
			Map<String, Boolean> configurationSnapshot = new HashMap<String, Boolean>();
			configurationSnapshot.put("base", Configuration.base);
			configurationSnapshot.put("weight", Configuration.weight);
			configurationSnapshot.put("empty", Configuration.empty);
			configurationSnapshot.put("twothirdsfull", Configuration.twothirdsfull);
			configurationSnapshot.put("executivefloor", Configuration.executivefloor);
			configurationSnapshot.put("overloaded", Configuration.overloaded);

			return new FeatureSet(configurationSnapshot);
		}
	}
}