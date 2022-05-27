package lab.square.spltcoverage.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.SessionInfoStore;
import org.jacoco.core.tools.ExecFileLoader;

import lab.square.spltcoverage.core.analysis.SpltCoverageGenerator;
import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.TestCaseCoverage;
import lab.square.spltcoverage.model.TestMethodCoverage;

public class CoverageReader {
	
	private String productPath;
	private String classPath;
	
	public CoverageReader(String productPath, String classPath) {
		this.productPath = productPath;
		this.classPath = classPath;
	}
	
	public ProductCoverage read() throws IOException {
		
		File folder = new File(productPath);
		if (!folder.exists())
			return null;
	
		File[] testCaseFolders = folder.listFiles();
		ProductCoverage productCoverage = new ProductCoverage(findFeatureSet(testCaseFolders));
		for (File testCaseFolder : testCaseFolders) {
			final String testCaseName = testCaseFolder.getName();
			
			TestCaseCoverage testCaseCoverage = new TestCaseCoverage(testCaseName);
			if (!testCaseFolder.isDirectory()) {
				if (testCaseFolder.getName().endsWith("Merged.exec") || testCaseFolder.getName().endsWith(SpltCoverageGenerator.SUFFIX_MERGED)) {
					productCoverage.addClassCoverages(load(testCaseFolder));
				}
				continue;
			}
			
			// load test method coverages.
			File[] testMethodCoverages = testCaseFolder.listFiles();
			for (File testMethodCoverageFile : testMethodCoverages) {
				final String testMethodName = testMethodCoverageFile.getName();

				if (testMethodName.endsWith("Merged.exec") || testMethodName.endsWith(SpltCoverageGenerator.SUFFIX_MERGED)) {
					testCaseCoverage.addClassCoverages(load(testMethodCoverageFile));
					continue;
				}

				TestMethodCoverage testMethodCoverage = new TestMethodCoverage(testMethodName,
						load(testMethodCoverageFile));
				testCaseCoverage.addChild(testMethodCoverage);
			}
			productCoverage.addChild(testCaseCoverage);
		}
		
		return productCoverage;
	}
	
	public void read(ProductCoverage productCoverage) throws IOException {
		// TODO: Merge duplicated methods..
		File folder = new File(productPath);
		if (!folder.exists())
			return;

		File[] testCaseFolders = folder.listFiles();
		for (File testCaseFolder : testCaseFolders) {
			final String testCaseName = testCaseFolder.getName();
			
			TestCaseCoverage testCaseCoverage = new TestCaseCoverage(testCaseName);
			if (!testCaseFolder.isDirectory()) {
				if (testCaseFolder.getName().endsWith("Merged.exec") || testCaseFolder.getName().endsWith(SpltCoverageGenerator.SUFFIX_MERGED)) {
					productCoverage.addClassCoverages(load(testCaseFolder));
				}
				continue;
			}
			
			// load test method coverages.
			File[] testMethodCoverages = testCaseFolder.listFiles();
			for (File testMethodCoverageFile : testMethodCoverages) {
				final String testMethodName = testMethodCoverageFile.getName();

				if (testMethodName.endsWith("Merged.exec") || testMethodName.endsWith(SpltCoverageGenerator.SUFFIX_MERGED)) {
					testCaseCoverage.addClassCoverages(load(testMethodCoverageFile));
					continue;
				}

				TestMethodCoverage testMethodCoverage = new TestMethodCoverage(testMethodName,
						load(testMethodCoverageFile));
				testCaseCoverage.addChild(testMethodCoverage);
			}
		}
		
		return;
	}
	
	private Collection<IClassCoverage> load(File testMethodCoverageFile) throws IOException {
		ExecFileLoader execFileLoader = new ExecFileLoader();

		execFileLoader.load(testMethodCoverageFile);

		final ExecutionDataStore execStore = execFileLoader.getExecutionDataStore();
		final SessionInfoStore sessionStore = execFileLoader.getSessionInfoStore();

		final CoverageBuilder coverageBuilder = new CoverageBuilder();
		final Analyzer analyzer = new Analyzer(execStore, coverageBuilder);

		analyzer.analyzeAll(new File(classPath));

		return new HashSet<IClassCoverage>(coverageBuilder.getClasses());
	}
	
	private Map<String, Boolean> findFeatureSet(File... testCaseFolders) throws FileNotFoundException, IOException {
		// TODO: Remove duplicated methods in SpltCoverageGenerator
		Map<String, Boolean> featureSet = new HashMap<String, Boolean>();

		for (File isFeatureSet : testCaseFolders) {
			final String testCaseName = isFeatureSet.getName();
			if (isFeatureSet.getName().equalsIgnoreCase(SplCoverageReader.FEATURESET_FILENAME)) {

				FileReader fr = new FileReader(isFeatureSet);
				BufferedReader br = new BufferedReader(fr);
				String given = br.readLine();
				given = given.replace("{", "");
				given = given.replace("}", "");
				String[] pairs = given.split(",");

				for (String pair : pairs) {
					String[] splitted = pair.split("=");
					String key = splitted[0].trim();
					String value = splitted[1].trim();

					featureSet.put(key, value.equals("true") ? true : false);
				}
			}
		}
		return featureSet;
	}
}
