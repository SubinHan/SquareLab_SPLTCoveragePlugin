package lab.square.spltcoverage.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import javax.management.MalformedObjectNameException;

import org.junit.Test;

import lab.square.spltcoverage.core.analysis.CoverageGenerator;
import lab.square.spltcoverage.core.analysis.IIterableSpltProvider;
import lab.square.spltcoverage.core.analysis.SpltCoverageGenerator;
import lab.square.spltcoverage.test.target.TestSpltProvider;

/*
 * Set the VM arguments to enable the RMI connection!
 * 
 * -javaagent:C:\Users\SELAB\Downloads\jacoco-0.8.6\lib\jacocoagent.jar=jmx=true -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=7777 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=localhost
 */

public class CoverageGeneratorTest2 {

	private static final int PRODUCT_COUNT = 4;
	private static final int TESTCLASS_COUNT=2;
	private static final int A_TESTMETHOD_COUNT=3;
	private static final int B_TESTMETHOD_COUNT=5;
	
	@Test
	public void testCoverageGenerator() {
		SpltCoverageGenerator generator = new SpltCoverageGenerator();
		IIterableSpltProvider provider = new TestSpltProvider();
		try {
			generator.generateCoverage(provider);
		} catch (MalformedObjectNameException | IOException e) {
			e.printStackTrace();
		}
		
		
		File base = new File(provider.getBaseDirectory());
		assertTrue(base.exists());
		
		verifyProducts(base);
	}

	private void verifyProducts(File base) {
		int productCount = 0;
		for(File product : base.listFiles()) {
			if(product.isDirectory()) {
				productCount++;
				verifyTestClasses(product);
			}
			else {
				;
			}
		}
		
		assertTrue(productCount == PRODUCT_COUNT);
	}

	private void verifyTestClasses(File product) {
		int classCount = 0;
		for(File klass : product.listFiles()) {
			if(klass.isDirectory()) {
				classCount++;
				verifyTestMethods(klass);
			}
			else if(klass.getName().equalsIgnoreCase("featureset.txt")) {
				;
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
