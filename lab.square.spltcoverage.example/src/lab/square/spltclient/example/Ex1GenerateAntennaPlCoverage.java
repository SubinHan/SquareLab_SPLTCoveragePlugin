package lab.square.spltclient.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lab.square.spltcoverage.core.launch.SplCoverageGeneratorLauncher;
import lab.square.spltcoverage.io.FeatureIdeConfigReader;
import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.model.ProductSourceInfo;
import lab.square.spltcoverage.utils.Tools;

public class Ex1GenerateAntennaPlCoverage {

	private static List<ProductSourceInfo> info;
	
	public static void main(String[] args) {
		// First, we should create ProductSourceInfos to generate PL coverage.
		info = createProductSourceInfo();
		
		// We can generate coverages of variable PL by ProductSourceInfos.
		generateCoverages();
	}

	private static List<ProductSourceInfo> createProductSourceInfo() {
		
		// Set directories of product1~16.
		
		final int NUM_PRODUCTS = 16;
		String[] productDirectories = new String[NUM_PRODUCTS];
		String[] productSourcePaths = new String[NUM_PRODUCTS];
		
		final String PL_BASE_DIRECTORY = "antennapl";
		final String PRODUCT_PREFIX = "Elevator-Antenna-v1.2-000";
		
		for(int i = 0; i < Math.min(9, NUM_PRODUCTS); i++) {
			productDirectories[i] = PL_BASE_DIRECTORY + "/" + PRODUCT_PREFIX + "0" + (i + 1);
		}
		
		for(int i = 9; i < NUM_PRODUCTS; i++) {
			productDirectories[i] = PL_BASE_DIRECTORY + "/" + PRODUCT_PREFIX + (i + 1);
		}

		productSourcePaths = new String[NUM_PRODUCTS];
		for(int i = 0; i < NUM_PRODUCTS; i++) {
			productSourcePaths[i] = productDirectories[i] + "/src";
		}
		
		
		// Create ProductSourceInfos by using given directories.
		// classpath, test classe path, feature set, additional dependencies are needed
		// to create ProductSourceInfo.
		
		List<ProductSourceInfo> info = new ArrayList<>();
		
		final String CLASSPATH = "bin";
		final String DEPENDENCIES = "lib/*";
		final String TEST_CLASS_1 = "de/ovgu/featureide/examples/elevator/test/TestElevator.class";
		final String TEST_CLASS_2 = "de/ovgu/featureide/examples/elevator/test/TestFloorChooseDialog.class";
		
		for(int i = 0; i < NUM_PRODUCTS; i++) {
			String classpath = productDirectories[i] + "/" + CLASSPATH;
			String testClass1 = classpath + "/" + TEST_CLASS_1;
			String testClass2 = classpath + "/" + TEST_CLASS_2;
			String additionalDependencies = productDirectories[i] + "/" + DEPENDENCIES;
			FeatureSet featureSet = findFeatureSet(productDirectories[i], i+1);
			
			info.add(new ProductSourceInfo(classpath, Arrays.asList(testClass2, testClass1),
					featureSet, Arrays.asList(additionalDependencies)));
		}
		
		return info;
	}

	private static FeatureSet findFeatureSet(String productDirectory, int configNumber) {
		// findFetaureSet() finds the feature set of the given product directory.
		// It depends on featureIDE convention.
		// I named config files of products to 01~16.xml, 
		// so it can find the correspond config file.

		Map<String, Boolean> featureSet = new HashMap<>();
		
		String xmlName;
		if(configNumber < 10)
			xmlName = "0" + configNumber;
		else
			xmlName = String.valueOf(configNumber);
		
		String configDirectory = productDirectory + "/configs/" + xmlName + ".xml";
		
		return FeatureIdeConfigReader.readFeatureSet(configDirectory);
	}
	
	private static void generateCoverages() {
		final String OUTPUT_PATH = "spltoutput/antennapl";
		
		try {
			Tools.deleteDirectoryRecursively(new File(OUTPUT_PATH));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		SplCoverageGeneratorLauncher.launch(OUTPUT_PATH, info);		
	}
}
