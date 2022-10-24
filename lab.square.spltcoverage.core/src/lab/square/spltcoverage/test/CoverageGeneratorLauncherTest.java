package lab.square.spltcoverage.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import lab.square.spltcoverage.core.analysis.CoverageGenerator;
import lab.square.spltcoverage.core.launch.CoverageGeneratorLauncher;
import lab.square.spltcoverage.utils.Tools;

public class CoverageGeneratorLauncherTest {

	private static final int A_TESTMETHOD_COUNT = 3;
	private static final int B_TESTMETHOD_COUNT = 5;
	private static final int TESTCLASS_COUNT = 2;

	private static final String TARGET_CLASSPATH = "target/classes/";
	private static final String TARGET_TESTPATH1 = "lab/square/spltcoverage/test/target/ClassATest.class";
	private static final String TARGET_TESTPATH2 = "lab/square/spltcoverage/test/target/ClassBTest.class";
	private static final String OUTPUT_PATH = "testResources/testOutput/CoverageGeneratorLauncherTest/";

	@Test
	public void testLaunch() {
		try {
			Tools.deleteDirectoryRecursively(new File(OUTPUT_PATH));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		CoverageGeneratorLauncher.launch(TARGET_CLASSPATH,
				Arrays.asList(new String[] { TARGET_TESTPATH1, TARGET_TESTPATH2 }), OUTPUT_PATH);

		verifyTestClasses(new File(OUTPUT_PATH));
	}

	private void verifyTestClasses(File product) {
		int classCount = 0;
		for (File klass : product.listFiles()) {
			if (klass.isDirectory()) {
				classCount++;
				verifyTestMethods(klass);
			} else {
				;
			}
		}

		assertTrue(classCount == TESTCLASS_COUNT);
	}

	private void verifyTestMethods(File klass) {

		if (klass.getName().equalsIgnoreCase("ClassATest"))
			verifyTestMethodsA(klass);

		if (klass.getName().equalsIgnoreCase("ClassBTest"))
			verifyTestMethodsB(klass);
	}

	private void verifyTestMethodsA(File klass) {
		int methodCount = 0;

		for (File method : klass.listFiles()) {
			if (method.getName().endsWith(CoverageGenerator.SUFFIX_MERGED)) {
				;
			} else {
				methodCount++;
			}
		}

		assertTrue(methodCount == A_TESTMETHOD_COUNT);
	}

	private void verifyTestMethodsB(File klass) {
		int methodCount = 0;

		for (File method : klass.listFiles()) {
			if (method.getName().endsWith(CoverageGenerator.SUFFIX_MERGED)) {
				;
			} else {
				methodCount++;
			}
		}

		assertTrue(methodCount == B_TESTMETHOD_COUNT);
	}
}
