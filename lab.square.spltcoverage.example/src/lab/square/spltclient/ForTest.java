package lab.square.spltclient;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lab.square.spltcoverage.core.launch.SplCoverageGeneratorLauncher;
import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.model.ProductSourceInfo;
import lab.square.spltcoverage.utils.Tools;

public class ForTest {

	private static final String PRODUCT1_ROOT = "D:/workspace_experiment_challenge/lab.square.spltcoverage.core/testResources/AntennaPlProjects/antenna.test.product1/";
	private static final String PRODUCT2_ROOT = "D:/workspace_experiment_challenge/lab.square.spltcoverage.core/testResources/AntennaPlProjects/antenna.test.product2/";
	
	private static final String[] TESTS_PATH = new String[] {
		"test/spltcoverage/antennaproduct/ClassATest.class",
		"test/spltcoverage/antennaproduct/ClassBTest.class" };

	private static final String OUTPUT_PATH = "D:/workspace_experiment_challenge/lab.square.spltcoverage.core/testResources/AntennaSplCoverage2";
	private static final String CLASSPATH = "bin";
	private static final String SRC_PATH = "src";
	
	public static void main(String[] args) {
		deleteOutputDirectory();
		generateAntennaPlCoverage();
	}

	private static void deleteOutputDirectory() {
		try {
			Tools.deleteDirectoryRecursively(new File(OUTPUT_PATH));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void generateAntennaPlCoverage() {
		FeatureSet fs1 = new FeatureSet();
		fs1.addFeature("A");
		fs1.addFeature("B");
		
		FeatureSet fs2 = new FeatureSet();

		ProductSourceInfo info1 = new ProductSourceInfo(PRODUCT1_ROOT + CLASSPATH, makeTestPaths("", TESTS_PATH), fs1);
		ProductSourceInfo info2 = new ProductSourceInfo(PRODUCT2_ROOT + CLASSPATH, makeTestPaths("", TESTS_PATH), fs2);
				
		List<ProductSourceInfo> infoList = new ArrayList<>();
		infoList.add(info1);
		infoList.add(info2);
		
		SplCoverageGeneratorLauncher.launch(OUTPUT_PATH, infoList);
	}
	
	private static Collection<String> makeTestPaths(String root, String[] testPaths) {
		Collection<String> result = new ArrayList<>();
		for(String testPath : testPaths) {
			result.add(root + testPath);
		}
		return result;
	}

}
