package lab.square.spltcoverage.io;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.SessionInfoStore;
import org.jacoco.core.tools.ExecFileLoader;

import lab.square.spltcoverage.core.analysis.SpltCoverageGenerator;
import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.ProductCoverageManager;
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

		ProductCoverage productCoverage = new ProductCoverage(null);		
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
				final String testMethodName = testMethodCoverageFile.getName().replaceAll("[.]exec", "");

				if (testMethodName.endsWith("Merged") || testMethodName.endsWith(SpltCoverageGenerator.SUFFIX_MERGED)) {
					testCaseCoverage.addClassCoverages(load(testMethodCoverageFile));
					continue;
				}

				TestMethodCoverage testMethodCoverage = new TestMethodCoverage(testMethodName,
						load(testMethodCoverageFile));
				testCaseCoverage.addTestMethodCoverage(testMethodCoverage);
			}
		}
		
		return productCoverage;
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
}
