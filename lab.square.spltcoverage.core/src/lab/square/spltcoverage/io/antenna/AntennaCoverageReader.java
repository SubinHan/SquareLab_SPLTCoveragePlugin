package lab.square.spltcoverage.io.antenna;

import java.io.File;
import java.io.IOException;

import lab.square.spltcoverage.io.CoverageReader;
import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.model.antenna.AntennaProductCoverage;

public final class AntennaCoverageReader {

	private AntennaCoverageReader() {

	}

	public static AntennaProductCoverage read(String productPath, String classpath, String javaSourcePath) throws IOException {
		File root = new File(productPath);

		FeatureSet featureSet = CoverageReader.findFeatureSet(root.listFiles());

		AntennaProductCoverage productCoverage = new AntennaProductCoverage(featureSet, javaSourcePath);
		CoverageReader.readInto(productCoverage, productPath, classpath);
		
		return productCoverage;
	}

}
