package lab.square.spltcoverage.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import lab.square.spltcoverage.core.analysis.CoverageGenerator;
import lab.square.spltcoverage.core.launch.SplCoverageGeneratorLauncher;
import lab.square.spltcoverage.io.CoverageReader;
import lab.square.spltcoverage.model.ProductSourceInfo;
import lab.square.spltcoverage.utils.Tools;

public class SplCoverageGeneratorLauncherTest {
	private static final int P1_A_TESTMETHOD_COUNT = 2;
	private static final int P1_B_TESTMETHOD_COUNT = 3;
	private static final int P1_TESTCLASS_COUNT = 2;

	private static final String P1_TARGET_CLASSPATH = "target/classes/";
	private static final String P1_TARGET_TESTPATH1 = "lab/square/spltcoverage/test/antennatarget/ClassATest.class";
	private static final String P1_TARGET_TESTPATH2 = "lab/square/spltcoverage/test/antennatarget/ClassBTest.class";
	
	private static final String OUTPUT_PATH = "testResources/SplCoverageGeneratorLauncherTestOutput/";
	
	private static final int P2_A_TESTMETHOD_COUNT = 1;
	private static final int P2_B_TESTMETHOD_COUNT = 1;
	private static final int P2_TESTCLASS_COUNT = 2;

	private static final String P2_TARGET_CLASSPATH = "target/classes/";
	private static final String P2_TARGET_TESTPATH1 = "lab/square/spltcoverage/test/antennatarget2/ClassATest.class";
	private static final String P2_TARGET_TESTPATH2 = "lab/square/spltcoverage/test/antennatarget2/ClassBTest.class";
	
	@Test
	public void testLaunch() {
		try {
			Tools.deleteDirectoryRecursively(new File(OUTPUT_PATH));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		List<ProductSourceInfo> productSourceInfos;
		productSourceInfos = new ArrayList<>();
		
		Map<String, Boolean> featureSet1 = new HashMap<>();
		featureSet1.put("A", true);
		featureSet1.put("B", true);
		ProductSourceInfo info1 = new ProductSourceInfo(P1_TARGET_CLASSPATH, Arrays.asList(new String[] {P1_TARGET_TESTPATH1, P1_TARGET_TESTPATH2}), featureSet1);
		
		Map<String, Boolean> featureSet2 = new HashMap<>();
		featureSet1.put("A", false);
		featureSet1.put("B", false);
		ProductSourceInfo info2 = new ProductSourceInfo(P2_TARGET_CLASSPATH, Arrays.asList(new String[] {P2_TARGET_TESTPATH1, P2_TARGET_TESTPATH2}), featureSet2);
		
		productSourceInfos.add(info1);
		productSourceInfos.add(info2);
		
		SplCoverageGeneratorLauncher.launch(OUTPUT_PATH, productSourceInfos);

		assertEquals(getNumTestClasses(new File(OUTPUT_PATH + "/product1")), P1_TESTCLASS_COUNT);
		assertEquals(getNumTestClasses(new File(OUTPUT_PATH + "/product2")), P2_TESTCLASS_COUNT);
		assertEquals(getNumTestMethods(new File(OUTPUT_PATH + "/product1/ClassATest")), P1_A_TESTMETHOD_COUNT);
		assertEquals(getNumTestMethods(new File(OUTPUT_PATH + "/product1/ClassBTest")), P1_B_TESTMETHOD_COUNT);
		assertEquals(getNumTestMethods(new File(OUTPUT_PATH + "/product2/ClassATest")), P2_A_TESTMETHOD_COUNT);
		assertEquals(getNumTestMethods(new File(OUTPUT_PATH + "/product2/ClassBTest")), P2_B_TESTMETHOD_COUNT);
		
		File readFeatureSet1 = new File(OUTPUT_PATH + "/product1/featureset.txt");
		FileReader fr = null;
		try {
			fr = new FileReader(readFeatureSet1);
			BufferedReader br = new BufferedReader(fr);
			assertTrue(Tools.featureSetEquals(featureSet1, CoverageReader.makeMap(br.readLine())));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		File readFeatureSet2 = new File(OUTPUT_PATH + "/product2/featureset.txt");
		try {
			fr = new FileReader(readFeatureSet2);
			BufferedReader br = new BufferedReader(fr);
			assertTrue(Tools.featureSetEquals(featureSet2, CoverageReader.makeMap(br.readLine())));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	private int getNumTestClasses(File product) {
		int classCount = 0;
		for(File klass : product.listFiles()) {
			if(klass.isDirectory()) {
				classCount++;
			}
			else {
				;
			}
		}
		
		return classCount;
	}
	
	private int getNumTestMethods(File klass) {
		int methodCount = 0;
		
		for(File method : klass.listFiles()) {
			if(method.getName().endsWith(CoverageGenerator.SUFFIX_MERGED)) {
				;
			}
			else {
				methodCount++;
			}
		}
		
		return methodCount;
	}

}
