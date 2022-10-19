package lab.square.spltcoverage.core.antenna;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;

import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICounter;

import lab.square.spltcoverage.io.CoverageReader;
import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.antenna.FeatureLocation;

public class AntennaCoverageComparator {

	static final Logger logger = Logger.getLogger(AntennaCoverageComparator.class.getName());
	
	public void printResult(String productCoveragePath, String classpath, String srcPath) {
		ProductCoverage coverage = null;
		try {
			coverage = CoverageReader.read(productCoveragePath, classpath);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		for (IClassCoverage cc : coverage.getClassCoverages()) {
			printStatus(cc, srcPath);
		}
	}

	private void printStatus(IClassCoverage cc, String srcPath) {
		logger.info("===========" + cc.getName() + "==========");
		String path = findSourceFileInPath(cc.getSourceFileName(), srcPath);
		if(path == null)
			return;
		Collection<FeatureLocation> featureLocations = getFeature(path);
		
		for (int i = cc.getFirstLine(); i <= cc.getLastLine(); i++) {
			String lineInfo = "Line " + i + " " + getColor(cc.getLine(i).getStatus()) + ", " + FeatureLocation.calculateFeatureExpressionOfLine(featureLocations, i);
			logger.info(lineInfo);
		}
	}

	private Collection<FeatureLocation> getFeature(String javaSourcePath) {
		FeatureLocator locator = new FeatureLocator();
		return locator.analyze(javaSourcePath);
	}

	private String findSourceFileInPath(String sourceFileName, String srcPath) {
		File src = new File(srcPath);
		if(src.isDirectory())
			return findSourceFileRecur(sourceFileName, src);
		
		return null;
	}

	private String findSourceFileRecur(String sourceFileName, File directory) {
		File[] files = directory.listFiles();
		for(File file : files) {
			if(file.isDirectory()) {
				String result = findSourceFileRecur(sourceFileName, file);
				if(result != null)
					return result;
			}
					
			if(file.getName().equals(sourceFileName))
				return file.getPath();
		}
		return null;
	}

	private String getColor(int status) {
		switch (status) {
		case ICounter.FULLY_COVERED:
			return "GREEN";
		case ICounter.NOT_COVERED:
			return "RED";
		case ICounter.PARTLY_COVERED:
			return "YELLOW";
		default:
			return "";
		}
	}
}
