package lab.square.spltcoverage.test;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICounter;
import org.jacoco.core.data.ExecutionDataReader;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.SessionInfoStore;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.RunListener;

import lab.square.spltcoverage.core.analysis.CoverageGenerator;
import lab.square.spltcoverage.core.analysis.IProductProvider;
import lab.square.spltcoverage.core.analysis.ISpltProvider;
import lab.square.spltcoverage.core.analysis.SplCoverageGenerator;
import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.model.IProxy;
import lab.square.spltcoverage.model.ProductSourceInfo;

public class CoverageGeneratorTest {

	private static final String BASE_DIRECTORY = "D:/workspace-featureide/Elevator-Antenna-v1.2/bin/";
	private static final String CLASS_PATH = "D:\\workspace-featureide\\Elevator-Antenna-v1.2\\bin\\de\\ovgu\\featureide\\examples\\elevator\\test\\TestElevator.class";
	private static final String CLASS_NAME = "de.ovgu.featureide.examples.elevator.test.TestElevator";
	private static final String OUTPUT_PATH = "D:/test/";

	private static final String SERVICE_URL = "service:jmx:rmi:///jndi/rmi://localhost:7777/jmxrmi";

	private static final String CLASSPATH_1 = "D:\\workspace-featureide\\Elevator-Antenna-v1.2-home\\bin";
	private static final String CLASSPATH_2 = "D:\\workspace-featureide\\Elevator-Antenna-v1.2-enterprise\\bin";
	private static final String OUTPUT_PATH_SPLT = "D:/directorypath/elevatorantenna/";

	@Test
	public void testSpltCoverageGenerator() {
		List<ProductSourceInfo> productSourceInfos;
		productSourceInfos = new ArrayList<>();

		Collection<String> testsPath1 = new ArrayList<>();
		testsPath1.add(
				"D:\\workspace-featureide\\Elevator-Antenna-v1.2-home\\bin\\de\\ovgu\\featureide\\examples\\elevator\\test\\TestElevator.class");
		Collection<String> testsPath2 = new ArrayList<>();
		testsPath2.add(
				"D:\\workspace-featureide\\Elevator-Antenna-v1.2-enterprise\\bin\\de\\ovgu\\featureide\\examples\\elevator\\test\\TestElevator.class");

		FeatureSet featureSet1 = new FeatureSet();
		featureSet1.setFeature("Elevator", true);
		featureSet1.setFeature("Behavior", true);
		featureSet1.setFeature("Modes", true);
		featureSet1.setFeature("ShortestPaht", true);
		featureSet1.setFeature("CallButtons", true);
		featureSet1.setFeature("DirectedCall", true);
		featureSet1.setFeature("Service", true);
		featureSet1.setFeature("Priorities", true);
		featureSet1.setFeature("RushHour", true);
		featureSet1.setFeature("FloorPrioritiy", true);
		featureSet1.setFeature("PersonPriority", true);
		featureSet1.setFeature("VoiceOutput", true);
		featureSet1.setFeature("Security", true);
		featureSet1.setFeature("Permission", true);
		featureSet1.setFeature("FloorPermision", true);
		featureSet1.setFeature("PermissionControl", true);
		featureSet1.setFeature("Safety", true);
		featureSet1.setFeature("Overloaded", true);

		FeatureSet featureSet2 = new FeatureSet();
		featureSet1.setFeature("Elevator", true);
		featureSet1.setFeature("Behavior", true);
		featureSet1.setFeature("Modes", true);
		featureSet1.setFeature("FIFO", true);
		featureSet1.setFeature("CallButtons", true);
		featureSet1.setFeature("UndirectedCall", true);

		productSourceInfos.add(new ProductSourceInfo(CLASSPATH_1, testsPath1, featureSet1));
		productSourceInfos.add(new ProductSourceInfo(CLASSPATH_2, testsPath2, featureSet2));

		SplCoverageGenerator generator;
		generator = new SplCoverageGenerator();

		generator.generateCoverage(OUTPUT_PATH_SPLT, new ISpltProvider() {

			@Override
			public int getNumProducts() {
				return productSourceInfos.size();
			}

			@Override
			public ProductSourceInfo getProductInfo(int i) {
				return productSourceInfos.get(i);
			}
		});

	}

	@Test
	public void testCoverageGenerator() {
		CoverageGenerator generator = null;
		generator = new CoverageGenerator();

		generator.generateCoverage(new IProductProvider() {

			@Override
			public Collection<String> getTestClassPaths() {
				Collection<String> testClassPaths = new ArrayList<>();
				testClassPaths.add(CLASS_PATH);
				return testClassPaths;
			}

			@Override
			public String getClasspath() {
				return BASE_DIRECTORY;
			}

			@Override
			public String getOutputPath() {
				return OUTPUT_PATH;
			}

		});
	}

	@Test
	public void testJUnit() throws IOException, MalformedObjectNameException {
		final JMXServiceURL url = new JMXServiceURL(SERVICE_URL);
		final JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
		final MBeanServerConnection connection = jmxc.getMBeanServerConnection();

		IProxy proxy = MBeanServerInvocationHandler.newProxyInstance(connection,
				new ObjectName("org.jacoco:type=Runtime"), IProxy.class, false);

		JUnitCore junit = new JUnitCore();
		Class forTest = null;
		try {
			forTest = loadClassByPath(BASE_DIRECTORY, convertPathToClassName(CLASS_PATH));
		} catch (MalformedURLException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		junit.addListener(new RunListener() {

			@Override
			public void testFinished(Description description) throws Exception {
				System.out.println(description.getMethodName());
			}

		});
		junit.run(forTest);

		final ExecutionDataStore executionData = new ExecutionDataStore();
		final SessionInfoStore sessionInfos = new SessionInfoStore();

		final ExecutionDataReader reader = new ExecutionDataReader(
				new ByteArrayInputStream(proxy.getExecutionData(false)));

		reader.setExecutionDataVisitor(executionData);
		reader.setSessionInfoVisitor(sessionInfos);
		reader.read();

		// Together with the original class definition we can calculate coverage
		// information:
		final CoverageBuilder coverageBuilder = new CoverageBuilder();
		final Analyzer analyzer = new Analyzer(executionData, coverageBuilder);
		try {
			analyzer.analyzeAll(BASE_DIRECTORY, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Let's dump some metrics and line coverage information:
		for (final IClassCoverage cc : coverageBuilder.getClasses()) {
			System.out.printf("Coverage of class %s%n", cc.getName());

			for (int i = cc.getFirstLine(); i <= cc.getLastLine(); i++) {
				System.out.printf("Line %s: %s%n", Integer.valueOf(i), getColor(cc.getLine(i).getStatus()));
			}
		}

	}

	private String getColor(final int status) {
		switch (status) {
		case ICounter.NOT_COVERED:
			return "red";
		case ICounter.PARTLY_COVERED:
			return "yellow";
		case ICounter.FULLY_COVERED:
			return "green";
		}
		return "";
	}

	@Test
	public void testConvertPathToClassName() {
		String className = convertPathToClassName(CLASS_PATH);
		System.out.println(className);
		assertTrue(className.equals(CLASS_NAME));
	}

	private String convertPathToClassName(String classPath) {
		String filtered = classPath.replace('\\', '/').replace('/', '.').replaceFirst(".*bin.", "");

		if (filtered.endsWith(".class"))
			filtered = filtered.substring(0, filtered.length() - 6);

		return filtered;
	}

	private Class loadClassByPath(String binPath, String name) throws MalformedURLException, ClassNotFoundException {
		URLClassLoader loader = URLClassLoader.newInstance(new URL[] { new File(binPath).toURI().toURL() });

		return loader.loadClass(name);
	}

}
