package lab.square.spltcoverage.core.launch;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;

public class CoverageGeneratorLauncher {

	public static void launch(String classpath, Collection<String> testPath, String outputPath)
			throws IOException, InterruptedException {
		String separator = System.getProperty("file.separator");
		String cp = System.getProperty("java.class.path");
		String java_home = System.getProperty("java.home");
		String path = java_home + separator + "bin" + separator + "java";
		URL res = CoverageGeneratorLauncher.class.getClassLoader().getResource("jacocoagent.jar");
		File jacocoFile = null;
		try {
			jacocoFile = new File(res.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		String jacocoPath = jacocoFile.getPath();
		String javaagent = "-javaagent:" + jacocoPath + "=jmx=true";
		String jmxarg1 = "-Dcom.sun.management.jmxremote";
		String jmxarg2 = "-Dcom.sun.management.jmxremote.port=7777";
		String jmxarg3 = "-Dcom.sun.management.jmxremote.authenticate=false";
		String jmxarg4 = "-Dcom.sun.management.jmxremote.ssl=false";
		String rmiarg = "-Djava.rmi.server.hostname=localhost";

		if (!classpath.isEmpty()) {
			cp = cp.concat(";");
			cp = cp.concat(classpath);
		}
		if (testPath.isEmpty()) {
			// TODO: Throw exception or log
			return;
		}

		System.out.println(separator);
		System.out.println(cp);
		System.out.println(path);

		String convertedTestPath = convertToSingleLine(testPath);

		ProcessBuilder processBuilder = new ProcessBuilder(
				path, javaagent, jmxarg1, jmxarg2, jmxarg3, jmxarg4, rmiarg,
				"-cp", cp, LauncherMain.class.getName(), classpath, convertedTestPath, outputPath);
		processBuilder.directory(new File("./").getCanonicalFile());
		Process process = processBuilder.start();
		BufferedReader read = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		String line;
		while ((line = read.readLine()) != null) {
			System.out.println("launcher: " + line);
		}

	}

	private static String convertToSingleLine(Collection<String> testPath) {
		String result = "";
		for (String tp : testPath) {
			result += tp;
			result += ";";
		}

		return result;
	}
}
