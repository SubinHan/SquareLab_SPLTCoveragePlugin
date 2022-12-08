package lab.square.spltclient;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;

import javax.management.MalformedObjectNameException;

import lab.square.spltcoverage.core.analysis.CoverageGenerator;
import lab.square.spltcoverage.core.analysis.IProductProvider;
import lab.square.spltcoverage.core.antenna.AntennaCoverageComparator;
import lab.square.spltcoverage.io.CoverageReader;
import lab.square.spltcoverage.model.ProductCoverage;

/**
 * NOTICE!
 * Here is the error, because of dependency management.
 * It can only generate single coverage per once.
 *
 */


public class ElevatorCoverageGen {
	private static final String CLASSPATH_1 = "D:\\workspace-featureide\\elevator-Antenna-v1.2-Both\\bin\\";
	private static final String[] TESTS_PATH_1 = new String[] {
			"D:\\workspace-featureide\\elevator-Antenna-v1.2-Both\\bin\\de\\ovgu\\featureide\\examples\\elevator\\test\\TestElevator.class" };
	private static final String OUTPUT_PATH_1 = "D:/directorypath/elevatorantenna/product03/";
	private static final String SOURCE_CLASSPATH_1 = "D:\\workspace-featureide\\elevator-Antenna-v1.2-Both\\src\\";

	private static final String CLASSPATH_2 = "D:\\workspace-featureide\\elevator-Antenna-v1.2-UndirectedCall\\bin\\";
	private static final String[] TESTS_PATH_2 = new String[] {
			"D:\\workspace-featureide\\elevator-Antenna-v1.2-UndirectedCall\\bin\\de\\ovgu\\featureide\\examples\\elevator\\test\\TestElevator.class" };
	private static final String OUTPUT_PATH_2 = "D:/directorypath/elevatorantenna/product02/";
	private static final String SOURCE_CLASSPATH_2 = "D:\\workspace-featureide\\elevator-Antenna-v1.2-UndirectedCall\\src\\";

	public static void main(String[] args) {
		CoverageGenerator generator = null;
		try {
			//addPath(CLASSPATH_1);
			addPath(CLASSPATH_2);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		generator = new CoverageGenerator();

		final Collection<String> testPaths1 = new ArrayList<String>();
		testPaths1.add("D:\\workspace-featureide\\Elevator-Antenna-v1.2-Both\\bin\\de\\ovgu\\featureide\\examples\\elevator\\test\\TestElevator.class");
		final Collection<String> testPaths2 = new ArrayList<String>();
		testPaths2.add("D:\\workspace-featureide\\Elevator-Antenna-v1.2-UndirectedCall\\bin\\de\\ovgu\\featureide\\examples\\elevator\\test\\TestElevator.class");
		
//		generator.generateCoverage(new IProductProvider() {
//			@Override
//			public Collection<String> getTestClassPaths() {
//				return testPaths1;
//			}
//
//			@Override
//			public String getClasspath() {
//				return CLASSPATH_1;
//			}
//
//			@Override
//			public String getOutputPath() {
//				return OUTPUT_PATH_1;
//			}
//		});

		generator.generateCoverage(new IProductProvider() {
			@Override
			public Collection<String> getTestClassPaths() {
				return testPaths2;
			}

			@Override
			public String getClasspath() {
				return CLASSPATH_2;
			}

			@Override
			public String getOutputPath() {
				return OUTPUT_PATH_2;
			}
		});

//		CoverageReader reader1 = new CoverageReader(OUTPUT_PATH_1, CLASSPATH_1);
//		CoverageReader reader2 = new CoverageReader(OUTPUT_PATH_2, CLASSPATH_2);
//		ProductCoverage coverage1 = null;
//		ProductCoverage coverage2 = null;
//		try {
//			coverage1 = reader1.read();
//			coverage2 = reader2.read();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
	}
	
	public static void addPath(String s) throws Exception {
	    File f = new File(s);
	    URL u = f.toURL();
	    URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
	    Class urlClass = URLClassLoader.class;
	    Method method = urlClass.getDeclaredMethod("addURL", new Class[]{URL.class});
	    method.setAccessible(true);
	    method.invoke(urlClassLoader, new Object[]{u});
	}
}
