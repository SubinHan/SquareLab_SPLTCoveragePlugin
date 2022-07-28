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

import lab.square.spltcoverage.core.analysis.SplCoverageGenerator;
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
		
		read(productCoverage);
		
		return productCoverage;
	}
	
	public void read(ProductCoverage productCoverage) throws IOException {
		File folder = new File(productPath);
		if (!folder.exists())
			return;

		File[] testCaseFolders = folder.listFiles();
		for (File testCaseFolder : testCaseFolders) {
			final String testCaseName = testCaseFolder.getName();
			
			TestCaseCoverage testCaseCoverage = new TestCaseCoverage(testCaseName);
			if (!testCaseFolder.isDirectory()) {
				if (testCaseFolder.getName().endsWith("Merged.exec") || testCaseFolder.getName().endsWith(SplCoverageGenerator.SUFFIX_MERGED)) {
					productCoverage.addClassCoverages(load(testCaseFolder));
				}
				continue;
			}
			
			// load test method coverages.
			File[] testMethodCoverages = testCaseFolder.listFiles();
			for (File testMethodCoverageFile : testMethodCoverages) {
				final String testMethodName = testMethodCoverageFile.getName();

				if (testMethodName.endsWith("Merged.exec") || testMethodName.endsWith(SplCoverageGenerator.SUFFIX_MERGED)) {
					testCaseCoverage.addClassCoverages(load(testMethodCoverageFile));
					continue;
				}

				TestMethodCoverage testMethodCoverage = new TestMethodCoverage(testMethodName,
						load(testMethodCoverageFile));
				testCaseCoverage.addChild(testMethodCoverage);
			}
			productCoverage.addChild(testCaseCoverage);
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
	
	public static Map<String, Boolean> findFeatureSet(File... files) throws FileNotFoundException, IOException {
		Map<String, Boolean> featureSet = new HashMap<String, Boolean>();

		for (File file : files) {
			final String testCaseName = file.getName();
			if (file.getName().equalsIgnoreCase(SplCoverageGenerator.FEATURESET_FILE_NAME)) {

				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				featureSet = makeMap(br.readLine());
			}
		}
		return featureSet;
	}

	public static Map<String, Boolean> makeMap(String given) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		
		given = given.replace("{", "");
		given = given.replace("}", "");
		String[] pairs = given.split(",");

		if(pairs[0].isEmpty())
			return map;
		
		for (String pair : pairs) {
			String[] splitted = pair.split("=");
			String key = splitted[0].trim();
			String value = splitted[1].trim();

			map.put(key, value.equals("true") ? true : false);
		}
		
		return map;
	}
}
