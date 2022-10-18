package lab.square.spltcoverage.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.tools.ExecFileLoader;

import lab.square.spltcoverage.core.analysis.SplCoverageGenerator;
import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.TestCaseCoverage;
import lab.square.spltcoverage.model.TestMethodCoverage;
import lab.square.spltcoverage.utils.Tools;

public class CoverageReader {

	private static String classpath;
	
	protected CoverageReader() {
		
	}

	public static ProductCoverage read(String productPath, String classpath) throws IOException {
		File folder = new File(productPath);
		if (!folder.exists())
			return null;

		File[] testCaseFolders = folder.listFiles();
		ProductCoverage productCoverage = new ProductCoverage(findFeatureSet(testCaseFolders));

		readInto(productCoverage, productPath, classpath);

		return productCoverage;
	}

	public static void readInto(ProductCoverage productCoverage, String productPath, String classpath) throws IOException {
		File folder = new File(productPath);
		if (!folder.exists())
			return;
		
		CoverageReader.classpath = classpath;

		File[] testCaseFolders = folder.listFiles();
		insertTestCaseCoverages(productCoverage, testCaseFolders);
	}
	
	public static FeatureSet findFeatureSet(File... files) {
		FeatureSet featureSet = new FeatureSet();

		for (File file : files) {
			if (file.getName().equalsIgnoreCase(SplCoverageGenerator.FEATURESET_FILE_NAME)) {
				try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
					String content = br.readLine();
					featureSet = makeFeatureSet(content);
				} catch (IOException e) {
					e.printStackTrace();
					return new FeatureSet();
				}
			}
		}
		return featureSet;
	}

	public static FeatureSet makeFeatureSet(String given) {
		FeatureSet featureSet = new FeatureSet();

		given = given.replace("{", "");
		given = given.replace("}", "");
		String[] pairs = given.split(",");

		if (pairs[0].isEmpty())
			return featureSet;

		for (String pair : pairs) {
			String[] splitted = pair.split("=");
			String key = splitted[0].trim();
			String value = splitted[1].trim();

			featureSet.setFeature(key, value.equals("true"));
		}

		return featureSet;
	}


	private static void insertTestCaseCoverages(ProductCoverage productCoverage, File[] testCaseFolders)
			throws IOException {
		for (File testCaseFolder : testCaseFolders) {
			final String testCaseName = testCaseFolder.getName();

			TestCaseCoverage testCaseCoverage = new TestCaseCoverage(testCaseName);
			if (!testCaseFolder.isDirectory()) {
				if (Tools.isMergedCoverage(testCaseFolder.getName())) {
					productCoverage.addClassCoverages(load(testCaseFolder));
				}
				continue;
			}

			// load test method coverages.
			File[] testMethodCoverages = testCaseFolder.listFiles();
			insertTestMethodCoverages(testCaseCoverage, testMethodCoverages);

			productCoverage.addChild(testCaseCoverage);
		}
	}

	private static void insertTestMethodCoverages(TestCaseCoverage testCaseCoverage, File[] testMethodCoverages)
			throws IOException {
		for (File testMethodCoverageFile : testMethodCoverages) {
			final String testMethodName = testMethodCoverageFile.getName();

			if (Tools.isMergedCoverage(testMethodName)) {
				testCaseCoverage.addClassCoverages(load(testMethodCoverageFile));
				continue;
			}

			TestMethodCoverage testMethodCoverage = new TestMethodCoverage(testMethodName,
					load(testMethodCoverageFile));
			testCaseCoverage.addChild(testMethodCoverage);
		}
	}

	private static Collection<IClassCoverage> load(File testMethodCoverageFile) throws IOException {
		ExecFileLoader execFileLoader = new ExecFileLoader();

		execFileLoader.load(testMethodCoverageFile);

		final ExecutionDataStore execStore = execFileLoader.getExecutionDataStore();

		final CoverageBuilder coverageBuilder = new CoverageBuilder();
		final Analyzer analyzer = new Analyzer(execStore, coverageBuilder);

		analyzer.analyzeAll(new File(classpath));

		return new HashSet<>(coverageBuilder.getClasses());
	}

}
