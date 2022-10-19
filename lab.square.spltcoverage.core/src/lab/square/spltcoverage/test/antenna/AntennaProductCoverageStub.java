package lab.square.spltcoverage.test.antenna;

import java.io.IOException;

import lab.square.spltcoverage.core.antenna.AntennaCoverageReader;
import lab.square.spltcoverage.model.antenna.AntennaProductCoverage;

public class AntennaProductCoverageStub {
	
	private static final String PRODUCT_PATH1 = "D:/workspace_experiment_challenge/lab.square.spltcoverage.core/testResources/AntennaSplCoverage/product1";
	private static final String PRODUCT_PATH2 = "D:/workspace_experiment_challenge/lab.square.spltcoverage.core/testResources/AntennaSplCoverage/product2";
	
	private static final String JAVA_SOURCE_PATH = "D:/workspace_experiment_challenge/lab.square.spltcoverage.core/src";
	private static final String CLASSPATH = "D:/workspace_experiment_challenge/lab.square.spltcoverage.core/target/classes";
	
	
	public static AntennaProductCoverage getStub1() {
		try {
			return AntennaCoverageReader.read(PRODUCT_PATH1, CLASSPATH, JAVA_SOURCE_PATH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static AntennaProductCoverage getStub2() {
		try {
			return AntennaCoverageReader.read(PRODUCT_PATH2, CLASSPATH, JAVA_SOURCE_PATH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
