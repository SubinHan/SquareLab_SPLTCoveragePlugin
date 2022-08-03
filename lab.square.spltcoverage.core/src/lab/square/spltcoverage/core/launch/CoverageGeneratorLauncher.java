package lab.square.spltcoverage.core.launch;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Logger;

public final class CoverageGeneratorLauncher {
	
	private static final Logger logger = Logger.getLogger(CoverageGeneratorLauncher.class.getName());
	
	private static final String SEPARATOR = System.getProperty("file.separator");
	private static final String JAVA_HOME = System.getProperty("java.home");
	private static final String JAVA_BIN_PATH = JAVA_HOME + SEPARATOR + "bin" + SEPARATOR + "java";

	private static final String JMX_ARG1 = "-Dcom.sun.management.jmxremote";
	private static final String JMX_ARG2 = "-Dcom.sun.management.jmxremote.port=7777";
	private static final String JMX_ARG3 = "-Dcom.sun.management.jmxremote.authenticate=false";
	private static final String JMX_ARG4 = "-Dcom.sun.management.jmxremote.ssl=false";
	private static final String RMI_ARG = "-Djava.rmi.server.hostname=localhost";
	
	
	private CoverageGeneratorLauncher() {
		
	}
	
	public static void launch(String testClasspathRoot, Collection<String> testClassPath, String outputPath) {
		launch(testClasspathRoot, testClassPath, outputPath, Collections.emptyList());
	}
	
	public static void launch(String testClasspathRoot, Collection<String> testClassPath, String outputPath, Collection<String> additionalDependencies) {
		String javaAgentArg = createJavaAgentArgument();
		String convertedAdditionalDependencies = createAdditionalDependencies(testClasspathRoot, additionalDependencies);
		String cp = appendClasspath(convertedAdditionalDependencies);
		
		if (testClassPath.isEmpty()) {
			// TODO: Throw exception or log
			return;
		}

		logger.info(() -> "Separator is: " + SEPARATOR);
		logger.info(() -> "Classpath is: " + cp);
		logger.info(() -> "Bin path is: " + JAVA_BIN_PATH);

		String convertedTestPath = convertToSingleLine(testClassPath);

		ProcessBuilder processBuilder = createProcessBuilder(testClasspathRoot, outputPath, javaAgentArg, cp, convertedTestPath);
		Process process = startProcess(processBuilder);
		logProcessErrors(process);
	}

	private static String createAdditionalDependencies(String classpath, Collection<String> additionalDependencies) {
		ArrayList<String> additionalDependenciesCopy = new ArrayList<>(additionalDependencies);
		additionalDependenciesCopy.add(classpath);
		return convertToSingleLine(additionalDependenciesCopy);
	}

	private static Process startProcess(ProcessBuilder processBuilder) {
		Process process = null;
		try {
			process = processBuilder.start();
		} catch (IOException e) {
			logger.severe("Starting process failed.");
			logger.severe(e.getMessage());
		}
		return process;
	}

	

	private static void logProcessErrors(Process process) {
		String line;
		logger.info("error msg from the launcher: ");
		BufferedReader read = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		readLineAndLogTillEnd(read);
	}

	private static void readLineAndLogTillEnd(BufferedReader read) {
		String line;
		try {
			while ((line = read.readLine()) != null) {
				logger.severe(line);
			}
		} catch (IOException e) {
			logger.severe("Process logging failed.");
			logger.severe(e.getMessage());
		}
	}

	private static ProcessBuilder createProcessBuilder(String classpath, String outputPath, String javaagent, String cp,
			String convertedTestPath) {
		ProcessBuilder processBuilder = new ProcessBuilder(
				JAVA_BIN_PATH, javaagent, JMX_ARG1, JMX_ARG2, JMX_ARG3, JMX_ARG4, RMI_ARG,
				"-cp", cp, LauncherMain.class.getName(), classpath, convertedTestPath, outputPath);
		setProcessBuilderDirectory(processBuilder);
		logger.info(() -> "arguments: " + processBuilder.command().toString());
		return processBuilder;
	}

	private static void setProcessBuilderDirectory(ProcessBuilder processBuilder) {
		try {
			processBuilder.directory(new File("./").getCanonicalFile());
		} catch (IOException e) {
			logger.severe("Process building failed: the relative directory caused an error");
			logger.severe(e.getMessage());
		}
	}

	private static String appendClasspath(String classpath) {
		StringBuilder classpathBuilder = new StringBuilder(System.getProperty("java.class.path"));
		if (!classpath.isEmpty()) {
			classpathBuilder.append(";");
			String convertedClasspath = convertClasspath(classpath, SEPARATOR);
			classpathBuilder.append(convertedClasspath);
		}
		return classpathBuilder.toString();
	}

	private static String createJavaAgentArgument() {
		URL res = CoverageGeneratorLauncher.class.getClassLoader().getResource("jacocoagent.jar");
		File jacocoFile = null;
		try {
			jacocoFile = new File(res.toURI());
		} catch (URISyntaxException e) {
			logger.severe(e.getMessage());
			return "";
		}
		String jacocoPath = jacocoFile.getPath();
		String javaagent = "-javaagent:" + jacocoPath + "=jmx=true";
		return javaagent;
	}

	private static String convertClasspath(String classpath, String separator) {
		return classpath.replace("/", separator).replace("\\", separator);
	}

	private static String convertToSingleLine(Collection<String> paths) {
		StringBuilder builder = new StringBuilder();
		
		for (String path : paths) {
			builder.append(path);
			builder.append(";");
		}

		return builder.substring(0, builder.length()-1);
	}
}
