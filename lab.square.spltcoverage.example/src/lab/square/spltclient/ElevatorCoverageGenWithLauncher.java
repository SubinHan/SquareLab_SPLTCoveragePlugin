package lab.square.spltclient;

import java.io.IOException;
import java.util.Arrays;

import lab.square.spltcoverage.core.launch.CoverageGeneratorLauncher;

public class ElevatorCoverageGenWithLauncher {
	private static final String CLASSPATH = "D:\\workspace-featureide\\elevator-Antenna-v1.2-Both\\bin\\";
	private static final String[] TESTS_PATH = new String[] {
			"de\\ovgu\\featureide\\examples\\elevator\\test\\TestElevator.class" };
	private static final String OUTPUT_PATH = "D:/directorypath/elevatorantenna/product03_2/";
	private static final String SOURCE_CLASSPATH_1 = "D:\\workspace-featureide\\elevator-Antenna-v1.2-Both\\src\\";

	public static void main(String[] args) {
		CoverageGeneratorLauncher.launch(CLASSPATH, Arrays.asList(TESTS_PATH), OUTPUT_PATH);
	}
}
