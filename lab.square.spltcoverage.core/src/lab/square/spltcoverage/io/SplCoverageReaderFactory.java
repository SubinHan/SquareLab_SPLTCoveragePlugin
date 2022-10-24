package lab.square.spltcoverage.io;

import lab.square.spltcoverage.io.antenna.AntennaSplCoverageReader;

public class SplCoverageReaderFactory {
	public static AbstractSplCoverageReader createInvariableSplCoverageReader(String classpath) {
		return new SplCoverageReader(classpath);
	}
	
	public static AbstractSplCoverageReader createAntennaSplCoverageReader(String[] classpaths, String[] javaSourcePaths) {
		return new AntennaSplCoverageReader(classpaths, javaSourcePaths);
	}
}
