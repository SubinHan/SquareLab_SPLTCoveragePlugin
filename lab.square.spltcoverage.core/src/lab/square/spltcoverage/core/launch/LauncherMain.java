package lab.square.spltcoverage.core.launch;

import java.util.Arrays;
import java.util.Collection;

import lab.square.spltcoverage.core.analysis.CoverageGenerator;
import lab.square.spltcoverage.core.analysis.IProductProvider;

public class LauncherMain {

	public static void main(String[] args) {
		if(args.length < 3) {
			System.out.println("Not enough arguments");
			return;
		}
		
		String classpath = args[0];
		Collection<String> testPath = convertToCollection(args[1]);
		String outputPath = args[2];
		
		for(int i = 0; i < args.length; i++) {
			System.out.println(args[i]);
		}
		System.out.println(testPath);
		
		IProductProvider provider = new IProductProvider() {
			@Override
			public Collection<String> getTestClassPaths() {
				return testPath;
			}

			@Override
			public String getClasspath() {
				return classpath;
			}

			@Override
			public String getOutputPath() {
				return outputPath;
			}
		};
		
		CoverageGenerator generator = null;
		generator = new CoverageGenerator();
		generator.generateCoverage(provider);		
	}

	private static Collection<String> convertToCollection(String args) {
		String[] splitted = args.split(";");		
		return Arrays.asList(splitted);
	}

}
