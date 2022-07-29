package lab.square.spltcoverage.core.launch;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.logging.Logger;

public final class CoverageGeneratorLauncher {
	
	private static final Logger logger = Logger.getLogger(CoverageGeneratorLauncher.class.getName());
	
	private CoverageGeneratorLauncher() {
		
	}

	public static void launch(String classpath, Collection<String> testPath, String outputPath)
			throws IOException {
		String separator = System.getProperty("file.separator");
		String cp = System.getProperty("java.class.path");
		String javaHome = System.getProperty("java.home");
		String path = javaHome + separator + "bin" + separator + "java";
		URL res = CoverageGeneratorLauncher.class.getClassLoader().getResource("jacocoagent.jar");
		File jacocoFile = null;
		try {
			jacocoFile = new File(res.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return;
		}
		String jacocoPath = jacocoFile.getPath();
		String javaagent = "-javaagent:" + jacocoPath + "=jmx=true";
		String jmxarg1 = "-Dcom.sun.management.jmxremote";
		String jmxarg2 = "-Dcom.sun.management.jmxremote.port=7777";
		String jmxarg3 = "-Dcom.sun.management.jmxremote.authenticate=false";
		String jmxarg4 = "-Dcom.sun.management.jmxremote.ssl=false";
		String rmiarg = "-Djava.rmi.server.hostname=localhost";

		StringBuilder classpathBuilder = new StringBuilder(cp);
		if (!classpath.isEmpty()) {
			classpathBuilder.append(";");
			classpathBuilder.append(classpath);
		}
		if (testPath.isEmpty()) {
			// TODO: Throw exception or log
			return;
		}

		logger.info(() -> "Separator is: " + separator);
		logger.info(() -> "Classpath is: " + classpathBuilder.toString());
		logger.info(() -> "Bin path is: " + path);

		String convertedTestPath = convertToSingleLine(testPath);

		ProcessBuilder processBuilder = new ProcessBuilder(
				path, javaagent, jmxarg1, jmxarg2, jmxarg3, jmxarg4, rmiarg,
				"-cp", cp, LauncherMain.class.getName(), classpath, convertedTestPath, outputPath);
		processBuilder.directory(new File("./").getCanonicalFile());
		Process process = processBuilder.start();
		BufferedReader read = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		String line;
		logger.info("error msg from the launcher: ");
		while ((line = read.readLine()) != null) {
			logger.severe(line);
		}

	}

	private static String convertToSingleLine(Collection<String> testPath) {
		StringBuilder builder = new StringBuilder();
		
		for (String tp : testPath) {
			builder.append(tp);
			builder.append(";");
		}

		return builder.toString();
	}
}
