package lab.square.spltcoverage.test.antenna;

import java.io.IOException;

import lab.square.spltcoverage.core.antenna.AntennaCoverageReader;
import lab.square.spltcoverage.model.antenna.AntennaProductCoverage;

public class AntennaProductCoverageStub {
	
	private static final String PRODUCT_PATH_1 = "D:/workspace_experiment_challenge/lab.square.spltcoverage.core/testResources/AntennaSplCoverage2/product1";
	private static final String PRODUCT_PATH_2 = "D:/workspace_experiment_challenge/lab.square.spltcoverage.core/testResources/AntennaSplCoverage2/product2";
	
	private static final String JAVA_SOURCE_PATH_1 = "D:/workspace_experiment_challenge/lab.square.spltcoverage.core/testResources/AntennaPlProjects/antenna.test.product1/src";
	private static final String CLASSPATH_1 = "D:/workspace_experiment_challenge/lab.square.spltcoverage.core/testResources/AntennaPlProjects/antenna.test.product1/bin";

	private static final String JAVA_SOURCE_PATH_2 = "D:/workspace_experiment_challenge/lab.square.spltcoverage.core/testResources/AntennaPlProjects/antenna.test.product2/src";
	private static final String CLASSPATH_2 = "D:/workspace_experiment_challenge/lab.square.spltcoverage.core/testResources/AntennaPlProjects/antenna.test.product2/bin";
	
	public static AntennaProductCoverage getStub1() {
		try {
			return AntennaCoverageReader.read(PRODUCT_PATH_1, CLASSPATH_1, JAVA_SOURCE_PATH_1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static AntennaProductCoverage getStub2() {
		try {
			return AntennaCoverageReader.read(PRODUCT_PATH_2, CLASSPATH_2, JAVA_SOURCE_PATH_2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
