package lab.square.spltcoverage.test.antenna;

import java.io.IOException;

import lab.square.spltcoverage.io.antenna.AntennaCoverageReader;
import lab.square.spltcoverage.model.antenna.AntennaProductCoverage;

public class AntennaProductCoverageStub {
	
	private static final String PRODUCT_PATH_1 = "testResources/testInput/AntennaSplCoverage/product1";
	private static final String PRODUCT_PATH_2 = "testResources/testInput/AntennaSplCoverage/product2";
	
	private static final String JAVA_SOURCE_PATH_1 = "testResources/testInput/AntennaPlProjects/antenna.test.product1/src";
	private static final String CLASSPATH_1 = "testResources/testInput/AntennaPlProjects/antenna.test.product1/bin";

	private static final String JAVA_SOURCE_PATH_2 = "testResources/testInput/AntennaPlProjects/antenna.test.product2/src";
	private static final String CLASSPATH_2 = "testResources/testInput/AntennaPlProjects/antenna.test.product2/bin";
	
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
