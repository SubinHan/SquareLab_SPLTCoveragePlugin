package lab.square.spltcoverage.test;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

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
import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.LoggerRuntime;
import org.jacoco.core.runtime.RuntimeData;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.RunListener;

import lab.square.spltcoverage.model.IProxy;

public class CoverageGeneratorTest {
	
	private static final String BASE_DIRECTORY_URL = "file:///D:/workspace-featureide/Elevator-Antenna-v1.2/bin/";
	private static final String CLASS_PATH = "D:\\workspace-featureide\\Elevator-Antenna-v1.2\\bin\\de\\ovgu\\featureide\\examples\\elevator\\test\\TestElevator.class";
	private static final String CLASS_NAME = "de.ovgu.featureide.examples.elevator.test.TestElevator";
	
	private static final String SERVICE_URL = "service:jmx:rmi:///jndi/rmi://localhost:7777/jmxrmi";
	
	@Test
	public void testJUnit() throws IOException, MalformedObjectNameException {
		final JMXServiceURL url = new JMXServiceURL(SERVICE_URL);
		final JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
		final MBeanServerConnection connection = jmxc.getMBeanServerConnection();

		IProxy proxy = MBeanServerInvocationHandler.newProxyInstance(connection,
				new ObjectName("org.jacoco:type=Runtime"), IProxy.class, false);
		
		final IRuntime runtime = new LoggerRuntime();
		try {
			Class instrumented = loadInstrumentedClass(CLASS_PATH, runtime);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final RuntimeData data = new RuntimeData();
		try {
			runtime.startup(data);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		JUnitCore junit = new JUnitCore();
		Class forTest = null;
		try {
			forTest = loadTestClassByPath(BASE_DIRECTORY_URL, CLASS_NAME);
		} catch (MalformedURLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
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
		
		runtime.shutdown();

		// Together with the original class definition we can calculate coverage
		// information:
		final CoverageBuilder coverageBuilder = new CoverageBuilder();
		final Analyzer analyzer = new Analyzer(executionData, coverageBuilder);
		try {
			analyzer.analyzeClass(new FileInputStream(new File(CLASS_PATH)), convertPathToClassName(CLASS_PATH));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Let's dump some metrics and line coverage information:
		for (final IClassCoverage cc : coverageBuilder.getClasses()) {
			System.out.printf("Coverage of class %s%n", cc.getName());

			for (int i = cc.getFirstLine(); i <= cc.getLastLine(); i++) {
				System.out.printf("Line %s: %s%n", Integer.valueOf(i),
						getColor(cc.getLine(i).getStatus()));
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
		
		if(filtered.endsWith(".class"))
			filtered = filtered.substring(0, filtered.length() - 6);
		
		return filtered;
	}

	/**
	 * A class loader that loads classes from in-memory data.
	 */
	public static class MemoryClassLoader extends ClassLoader {

		private final Map<String, byte[]> definitions = new HashMap<String, byte[]>();

		/**
		 * Add a in-memory representation of a class.
		 *
		 * @param name
		 *            name of the class
		 * @param bytes
		 *            class definition
		 */
		public void addDefinition(final String name, final byte[] bytes) {
			definitions.put(name, bytes);
		}

		@Override
		protected Class<?> loadClass(final String name, final boolean resolve)
				throws ClassNotFoundException {
			final byte[] bytes = definitions.get(name);
			if (bytes != null) {
				return defineClass(name, bytes, 0, bytes.length);
			}
			return super.loadClass(name, resolve);
		}
	}

	
	private Class loadInstrumentedClass(String path, IRuntime runtime) throws ClassNotFoundException, IOException {
		InputStream inputStream = new FileInputStream(new File(path));
		
		String name = convertPathToClassName(path);
		
		Instrumenter instrumenter = new Instrumenter(runtime);
		final byte[] instrumented = instrumenter.instrument(inputStream, name);
		MemoryClassLoader memoryClassLoader = new MemoryClassLoader();
		memoryClassLoader.addDefinition(name, instrumented);

		return memoryClassLoader.loadClass(name);
	}
	
	private Class loadTestClassByPath(String path, String name) throws MalformedURLException, ClassNotFoundException {		
		URLClassLoader loader = URLClassLoader.newInstance(new URL[] {
				new URL(path)
		});
		
		return loader.loadClass(name);
	}
	
}
