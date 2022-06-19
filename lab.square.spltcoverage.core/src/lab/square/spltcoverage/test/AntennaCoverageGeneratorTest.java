package lab.square.spltcoverage.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import javax.management.MalformedObjectNameException;

import org.junit.Before;
import org.junit.Test;

import lab.square.spltcoverage.core.analysis.CoverageGenerator;
import lab.square.spltcoverage.test.antennatarget.TestProductProvider;
import lab.square.spltcoverage.utils.Tools;

/**
 * jacoco agent required.
 * @author selab
 *
 */

public class AntennaCoverageGeneratorTest {
	
	private static final int A_TESTMETHOD_COUNT = 2;
	private static final int B_TESTMETHOD_COUNT = 3;
	private static final int TESTCLASS_COUNT = 2;

	private TestProductProvider provider;
	
	@Before
	public void setUp() {
		this.provider = new TestProductProvider();
		
		deleteOutputDirectory(new File(provider.getOutputPath()));
	}

	private void deleteOutputDirectory(File dir) {
		if(dir.exists())
			try {
				Tools.deleteDirectoryRecursively(dir);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
	}
	
	@Test
	public void testGenerate() {
		CoverageGenerator generator = null;
		try {
			generator = new CoverageGenerator();
		} catch (MalformedObjectNameException | IOException e) {
			e.printStackTrace();
		}
		
		if(generator == null)
			fail();		
		
		generator.generateCoverage(provider);
		
		File product = new File(provider.getOutputPath());
		
		verifyTestClasses(product);		
		
	}
	
	private void verifyTestClasses(File product) {
		int classCount = 0;
		for(File klass : product.listFiles()) {
			if(klass.isDirectory()) {
				classCount++;
				verifyTestMethods(klass);
			}
			else {
				;
			}
		}
		
		assertTrue(classCount == TESTCLASS_COUNT);
	}

	private void verifyTestMethods(File klass) {
		
		if(klass.getName().equalsIgnoreCase("ClassATest"))
			verifyTestMethodsA(klass);
		
		if(klass.getName().equalsIgnoreCase("ClassBTest"))
			verifyTestMethodsB(klass);
	}

	private void verifyTestMethodsA(File klass) {
		int methodCount = 0;
		
		for(File method : klass.listFiles()) {
			if(method.getName().endsWith(CoverageGenerator.SUFFIX_MERGED)) {
				;
			}
			else {
				methodCount++;
			}
		}
		
		assertTrue(methodCount == A_TESTMETHOD_COUNT);
	}
	
	private void verifyTestMethodsB(File klass) {
		int methodCount = 0;
		
		for(File method : klass.listFiles()) {
			if(method.getName().endsWith(CoverageGenerator.SUFFIX_MERGED)) {
				;
			}
			else {
				methodCount++;
			}
		}
		
		assertTrue(methodCount == B_TESTMETHOD_COUNT);
	}
}
