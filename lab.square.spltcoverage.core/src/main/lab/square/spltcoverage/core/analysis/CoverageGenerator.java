package lab.square.spltcoverage.core.analysis;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.logging.Logger;

import javax.management.MalformedObjectNameException;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.data.ExecutionDataReader;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.SessionInfoStore;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import lab.square.spltcoverage.io.CoverageWriter;
import lab.square.spltcoverage.model.CoverageResult;

/**
 * It generates coverage execution data of a if-parameterization product line.
 * It uses JaCoCo, and the port should be opened to communicate with JaCoCo
 * agent. Use the CoverageGeneratorLauncher class to generate coverage, but do
 * not use this directly to generate coverage.
 * 
 * The 7777 port is used to communicate to the JaCoCo by using RMI. If you want
 * to use the CoverageGenerator directly without launcher, then you should set
 * VM arguments following:
 * 
 * -javaagent:[JACOCO_AGENT_PATH]=jmx=true -Dcom.sun.management.jmxremote
 * -Dcom.sun.management.jmxremote.port=7777
 * -Dcom.sun.management.jmxremote.authenticate=false
 * -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=localhost
 * 
 * @author selab
 *
 */
public class CoverageGenerator {

	static final Logger logger = Logger.getLogger(CoverageGenerator.class.getName());

	public static final String SUFFIX_MERGED = "__merged__.exec";

	private Class[] targetClasses;
	private final JacocoConnection jacocoConnection;

	/**
	 * Create CoverageGenerator without target classes. here may no differences
	 * between the target classes are null or not, but only that use analyze()
	 * method.
	 * 
	 */
	public CoverageGenerator() {
		this.jacocoConnection = JacocoConnection.getInstance();
	}

	/**
	 * Create CoverageGenerator with target classes. There may no differences
	 * between the target classes are null or not, but only that use analyze()
	 * method.
	 * 
	 * @param targetClasses
	 */
	public CoverageGenerator(Class... targetClasses) {
		this();
		this.targetClasses = targetClasses;
	}

	/**
	 * It returns CoverageResult about the program ran until now.
	 * 
	 * @return
	 * @throws IOException
	 */
	public CoverageResult analyze() throws IOException {

		logger.info("Version: " + jacocoConnection.getVersion());
		logger.info("Session: " + jacocoConnection.getSessionId());

		final ExecutionDataStore execStore = new ExecutionDataStore();
		final SessionInfoStore sessionStore = new SessionInfoStore();

		final ExecutionDataReader reader = new ExecutionDataReader(
				new ByteArrayInputStream(jacocoConnection.getExecutionData(false)));
		reader.setExecutionDataVisitor(execStore);
		reader.setSessionInfoVisitor(sessionStore);
		reader.read();

		final CoverageBuilder coverageBuilder = new CoverageBuilder();
		final Analyzer analyzer = new Analyzer(execStore, coverageBuilder);

		for (int i = 0; i < targetClasses.length; i++) {
			final String targetName = targetClasses[i].getName();
			analyzer.analyzeClass(getTargetClass(targetName), targetName);
		}

		return new CoverageResult(analyzer, coverageBuilder);
	}

	private InputStream getTargetClass(final String name) {
		final String resource = '/' + name.replace('.', '/') + ".class";
		if (targetClasses.length == 0)
			return null;
		return getClass().getResourceAsStream(resource);
	}

	/**
	 * Reset all coverage data collected until now.
	 */
	public void resetData() {
		jacocoConnection.resetData();
	}

	/**
	 * Generate coverage of the if-parameterization PL given by the provider.
	 * @param provider. Please refer the IProductProvider.
	 */
	public void generateCoverage(IProductProvider provider) {
		try {
			Files.createDirectories(Paths.get(provider.getOutputPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		runTestInPath(provider.getClasspath(), provider.getTestClassPaths(), provider);
		mergeExecs(provider.getOutputPath());
	}

	/**
	 * It is not implemented.
	 * @param provider
	 */
	@Deprecated
	public void generateCoverage2(IProductProvider provider) {
		// TODO

	}

	private void runTestInPath(String classpath, Collection<String> testClassesPath, IProductProvider provider) {
		JUnitCore junit = new JUnitCore();
		junit.addListener(new TestListener(provider));

		for (String path : testClassesPath) {
			Class forTest = null;

			forTest = loadClassByPath(classpath, convertPathToClassName(path));
			junit.run(forTest);
		}
	}

	private String convertPathToClassName(String classPath) {
		String filtered = classPath.replace('\\', '/').replace('/', '.').replaceFirst(".*bin.", "");

		if (filtered.endsWith(".class"))
			filtered = filtered.substring(0, filtered.length() - 6);

		return filtered;
	}

	private Class loadClassByPath(String binPath, String name) {
		Class klass = null;

		logger.info("loading Classes by path: binPath=" + binPath + " className=" + name);
		try (URLClassLoader loader = URLClassLoader.newInstance(new URL[] { new File(binPath).toURI().toURL() });) {
			klass = loader.loadClass(name);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

		return klass;
	}

	private void mergeExecs(String productDirectory) {
		File productFolder = new File(productDirectory);
		final FilenameFilter filter = (current, name) -> new File(current, name).isDirectory();
		File[] testCaseExecs = new File[productFolder.list(filter).length];

		int index = 0;
		CoverageMerger merger = new CoverageMerger();
		for (File testCaseFolder : productFolder.listFiles()) {
			if (!testCaseFolder.isDirectory())
				continue;
			File testCaseExec = new File(testCaseFolder, testCaseFolder.getName() + SUFFIX_MERGED);
			try {
				merger.mergeExecs(testCaseExec, testCaseFolder.listFiles());
			} catch (IOException e) {
				e.printStackTrace();
			}
			testCaseExecs[index++] = testCaseExec;
		}
		try {
			merger.mergeExecs(new File(productFolder, productFolder.getName() + SUFFIX_MERGED), testCaseExecs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class TestListener extends RunListener {
		IProductProvider provider;

		public TestListener(IProductProvider provider) {
			this.provider = provider;
		}

		@Override
		public void testStarted(Description description) throws Exception {
			// Do nothing when the test started.
		}

		@Override
		public void testFinished(Description description) throws Exception {
			logger.info(description.getTestClass().getSimpleName() + "." + description.getMethodName() + " finished.");

			String testCaseDirectory;
			String testMethodDirectory;
			testCaseDirectory = description.getTestClass().getSimpleName() + "/";
			testMethodDirectory = description.getMethodName();

			String directory = Paths.get(provider.getOutputPath()).resolve(testCaseDirectory + testMethodDirectory)
					.toString();
			CoverageWriter.makeExecFile(directory, jacocoConnection.getExecutionData(false));
			resetData();
		}

		@Override
		public void testFailure(Failure failure) throws Exception {
			logger.severe("Test Failed: " + failure.getTestHeader());
			logger.severe(failure.getTrace());
			logger.severe(failure.getMessage());
		}
	}

}
