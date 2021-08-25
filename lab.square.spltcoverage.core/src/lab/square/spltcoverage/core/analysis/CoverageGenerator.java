package lab.square.spltcoverage.core.analysis;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
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
import org.jacoco.core.analysis.ICounter;
import org.jacoco.core.data.ExecutionDataReader;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.SessionInfoStore;
import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.LoggerRuntime;
import org.jacoco.core.runtime.RuntimeData;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import lab.square.spltcoverage.core.model.CoverageResult;
import lab.square.spltcoverage.core.model.IProxy;

public class CoverageGenerator {

	/**
	 * A class loader that loads classes from in-memory data.
	 */
	public static class MemoryClassLoader extends ClassLoader {

		private final Map<String, byte[]> definitions = new HashMap<String, byte[]>();

		/**
		 * Add a in-memory representation of a class.
		 * 
		 * @param name  name of the class
		 * @param bytes class definition
		 */
		public void addDefinition(final String name, final byte[] bytes) {
			definitions.put(name, bytes);
		}

		@Override
		protected Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {

			final byte[] bytes = definitions.get(name);
			if (bytes != null) {
				return defineClass(name, bytes, 0, bytes.length);
			}
//			InstrumentationImpl impl = new InstrumentationImpl(0, resolve, resolve);
//			impl.redefineClasses(new ClassDefinition(Class.forName(name), bytes));
			return super.loadClass(name, resolve);
		}

	}

	private static final String DESTFILE = "mydata.exec";

	private static final String SERVICE_URL = "service:jmx:rmi:///jndi/rmi://localhost:7777/jmxrmi";

	private final PrintStream out;
	private Class[] targetClasses;
	private Class[] instrumentedClasses;
	private int count;

	final IRuntime runtime;
	final Instrumenter instr;
	final RuntimeData data;

	/**
	 * Creates a new example instance printing to the given stream.
	 * 
	 * @param out stream for outputs
	 */
	public CoverageGenerator(final PrintStream out) {
		this.out = out;
		runtime = new LoggerRuntime();
		instr = new Instrumenter(runtime);
		data = new RuntimeData();
	}

	public CoverageGenerator(final PrintStream out, Class... targetClasses) {
		this(out);
		this.targetClasses = targetClasses;
	}

	public Class[] getInstrumentedClasses() throws Exception {
		if (instrumentedClasses != null)
			return instrumentedClasses;
		runtime.startup(data);
		instrumentedClasses = new Class[this.targetClasses.length];
		System.arraycopy(this.targetClasses, 0, instrumentedClasses, 0, this.targetClasses.length);
		for (int i = 0; i < instrumentedClasses.length; i++) {
			final String targetName = instrumentedClasses[i].getName();
			final byte[] instrumented = instr.instrument(getTargetClass(targetName), targetName);

			final MemoryClassLoader memoryClassLoader = new MemoryClassLoader();
			memoryClassLoader.addDefinition(targetName, instrumented);
			final Class<?> targetClass = memoryClassLoader.loadClass(targetName);
			instrumentedClasses[i] = targetClass;
		}
		return instrumentedClasses;
	}

	public CoverageResult analyze() throws Exception {
		// Open connection to the coverage agent:
		final JMXServiceURL url = new JMXServiceURL(SERVICE_URL);
		final JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
		final MBeanServerConnection connection = jmxc.getMBeanServerConnection();

		final IProxy proxy = MBeanServerInvocationHandler.newProxyInstance(connection,
				new ObjectName("org.jacoco:type=Runtime"), IProxy.class, false);

		// Retrieve JaCoCo version and session id:
		System.out.println("Version: " + proxy.getVersion());
		System.out.println("Session: " + proxy.getSessionId());

		final ExecutionDataStore execStore = new ExecutionDataStore();
		final SessionInfoStore sessionStore = new SessionInfoStore();

		final ExecutionDataReader reader = new ExecutionDataReader(
				new ByteArrayInputStream(proxy.getExecutionData(false)));
		reader.setExecutionDataVisitor(execStore);
		reader.setSessionInfoVisitor(sessionStore);
		reader.read();

		final CoverageBuilder coverageBuilder = new CoverageBuilder();
		final Analyzer analyzer = new Analyzer(execStore, coverageBuilder);

		for (int i = 0; i < targetClasses.length; i++) {
			final String targetName = targetClasses[i].getName();
			analyzer.analyzeClass(getTargetClass(targetName), targetName);
		}
		
		final CoverageResult result = new CoverageResult(analyzer, coverageBuilder, proxy);
		return result; 
	}

	
	
	public CoverageResult analyze(String directory) throws Exception {

		final CoverageResult result = analyze();
		final byte[] exeData = result.proxy.getExecutionData(false);

		makeDirectory(directory);
		File execFile = new File(directory + ".exec");
		execFile.createNewFile();
		final FileOutputStream localFile = new FileOutputStream(execFile, false);
		localFile.write(exeData);
		localFile.close();
		
		return result;
	}

	private void makeDirectory(String directory) {
		String[] splitted = directory.split("/");
		String checkDirectory = "";
		for (int i = 0; i < splitted.length - 1; i++) {
			checkDirectory = checkDirectory + splitted[i] + "/";
			File file = new File(checkDirectory);
			if (!file.exists())
				file.mkdir();
		}
	}

	private InputStream getTargetClass(final String name) {
		final String resource = '/' + name.replace('.', '/') + ".class";
		if(targetClasses.length == 0)
			return null;
		return getClass().getResourceAsStream(resource);
	}

	private void printCounter(final String unit, final ICounter counter) {
		final Integer missed = Integer.valueOf(counter.getMissedCount());
		final Integer total = Integer.valueOf(counter.getTotalCount());
		out.printf("%s of %s %s missed%n", missed, total, unit);
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

	public void resetData() throws IOException, MalformedObjectNameException {
		// Open connection to the coverage agent:
		final JMXServiceURL url = new JMXServiceURL(SERVICE_URL);
		final JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
		final MBeanServerConnection connection = jmxc.getMBeanServerConnection();

		final IProxy proxy = MBeanServerInvocationHandler.newProxyInstance(connection,
				new ObjectName("org.jacoco:type=Runtime"), IProxy.class, false);

		proxy.reset();
	}
	
	public void generateCoverage(ICoverageRunner runner) {
		count = 0;
		targetClasses = runner.onGetTargetClasses();
		while(runner.onMakeNextProduct()) {
			count++;
			
			String productDirectory;
			productDirectory = runner.onGetProductDirectory() + count;
			String pathOfFeatureSet = runner.onGetBaseDirectory() + productDirectory + "/featureset.txt";
			File featureSet = new File(pathOfFeatureSet);
			makeDirectory(pathOfFeatureSet);
			try {
				// write file containing the current featureSet in the product folder.
				BufferedWriter localFile = new BufferedWriter(new FileWriter(featureSet));
				localFile.write(runner.onGetFeatureSet().toString());
				localFile.close();
			} catch (Exception e) {
				;
			}
			
			JUnitCore junit = new JUnitCore();
			junit.addListener(new TestListener(runner));
			org.junit.runner.Result result = junit.run(runner.onGetTestClasses());
			
			File productFolder = new File(runner.onGetBaseDirectory() + productDirectory);
			File[] testCaseExecs = new File[productFolder.list(new FilenameFilter() {
				@Override
				public boolean accept(File current, String name) {
						return new File(current, name).isDirectory();
				}
			}).length];
			
			int index = 0;
			CoverageMerger merger = new CoverageMerger();
			for (File testCaseFolder : productFolder.listFiles()) {
				if(!testCaseFolder.isDirectory())
					continue;
				File testCaseExec = new File(testCaseFolder, testCaseFolder.getName() + "Merged.exec");
				try {
					merger.mergeExecs(testCaseExec, testCaseFolder.listFiles());
				} catch (IOException e) {
					e.printStackTrace();
				}
				testCaseExecs[index++] = testCaseExec;
			}
			try {
				merger.mergeExecs(new File(productFolder, productFolder.getName() + "Merged.exec"), testCaseExecs);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			
		}
	}
	
	private class TestListener extends RunListener {
		ICoverageRunner runner;
		
		public TestListener(ICoverageRunner runner) {
			this.runner = runner;
		}
		
		@Override
		public void testStarted(Description description) throws Exception {
		}

		@Override
		public void testFinished(Description description) throws Exception {
			System.out.println(description.getTestClass().getSimpleName());
			System.out.println(description.getMethodName());
			System.out.println("//==============finished===========//");
			String testCaseDirectory;
			String testMethodDirectory;
			testCaseDirectory = runner.onGetTestCaseDirectory() + description.getTestClass().getSimpleName();
			testMethodDirectory = runner.onGetTestMethodDirectory() + description.getMethodName();
			String directory = runner.onGetBaseDirectory() + runner.onGetProductDirectory() + count + testCaseDirectory + testMethodDirectory;
			CoverageResult result = analyze(directory);
			
			final byte[] exeData = result.getProxy().getExecutionData(false);

			makeDirectory(directory);
			File execFile = new File(directory + ".exec");
			execFile.createNewFile();
			final FileOutputStream localFile = new FileOutputStream(execFile, false);
			localFile.write(exeData);
			localFile.close();
			
			resetData();
		}

		@Override
		public void testFailure(Failure failure) throws Exception {
			System.out.println(failure.getTestHeader());
			System.out.println(failure.getTrace());
			System.out.println(failure.getMessage());
			
		}
	}

}
