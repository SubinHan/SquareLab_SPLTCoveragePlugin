package lab.square.spltcoverage.core.launch;

import java.io.IOException;

public class CoverageGeneratorLauncher {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		String separator = System.getProperty("file.separator");
		String classpath = System.getProperty("java.class.path");
		String path = System.getProperty("java.home") + separator + "bin" + separator + "java";
		
		System.out.println(separator);
		System.out.println(classpath);
		System.out.println(path);
		
		ProcessBuilder processBuilder = new ProcessBuilder(path, "-cp", classpath,
				CoverageGeneratorLauncher.class.getName());
		Process process = processBuilder.start();
		process.waitFor();

	}

}
