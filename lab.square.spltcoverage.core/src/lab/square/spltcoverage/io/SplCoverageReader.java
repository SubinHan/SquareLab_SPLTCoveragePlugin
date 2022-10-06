package lab.square.spltcoverage.io;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.tools.ExecFileLoader;

import lab.square.spltcoverage.core.analysis.SplCoverageGenerator;
import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.SplCoverage;

/**
 * The CoverageReader class is the class reading the product coverages.
 * @author SQUARELAB
 *
 */
public class SplCoverageReader {

	public static final String FEATURESET_FILENAME = "featureset.txt";

	private String execFilesPath;
	private String classPath;
	private SplCoverage splCoverage;

	public SplCoverageReader(SplCoverage splCoverage, String classPath) {
		this.splCoverage = splCoverage;
		this.classPath = classPath;
	}
	
	/**
	 * 
	 * @param splCoverage
	 * @param execFilesPath
	 * @param classPath
	 */
	public SplCoverageReader(SplCoverage splCoverage, String execFilesPath, String classPath) {
		this(splCoverage, classPath);
		this.execFilesPath = execFilesPath;
	}

	public SplCoverage getSplCoverage() {
		return splCoverage;
	}
	
	public void setExecFilesPath(String path) {
		this.execFilesPath = path;
	}

	public void read() throws IOException {

		File folder = new File(execFilesPath);
		if (!folder.exists())
			return;

		File[] productFolders = folder.listFiles();

		for (File productFolder : productFolders) {
			if (!productFolder.isDirectory()) {
				if (productFolder.getName().endsWith("Merged.exec") || productFolder.getName().endsWith(SplCoverageGenerator.SUFFIX_MERGED)) {
					splCoverage.addClassCoverages(load(productFolder));
				}
				continue;
			}
			
			ProductCoverage productCoverage = CoverageReader.read(productFolder.getAbsolutePath(), classPath);
			
//			final String productName = productFolder.getName();
//
//			File[] testCaseFolders = productFolder.listFiles();
//
//			ProductCoverage productCoverage = null;
//
//			// find feature set and init productCoverage.
//			Map<String, Boolean> featureSet = findFeatureSet(testCaseFolders);
//			if (featureSet == null)
//				continue;
//			productCoverage = new ProductCoverage(featureSet, productName);
//
//			// collect execution data.
//			for (File testCaseFolder : testCaseFolders) {
//				final String testCaseName = testCaseFolder.getName();
//
//				TestCaseCoverage testCaseCoverage = new TestCaseCoverage(testCaseName);
//
//				// the file is not a directory.
//				if (!testCaseFolder.isDirectory()) {
//					if (testCaseFolder.getName().endsWith("Merged.exec") || testCaseFolder.getName().endsWith(SpltCoverageGenerator.SUFFIX_MERGED)) {
//						productCoverage.addClassCoverages(load(testCaseFolder));
//					}
//					continue;
//				}
//
//				// load test method coverages.
//				File[] testMethodCoverages = testCaseFolder.listFiles();
//				for (File testMethodCoverageFile : testMethodCoverages) {
//					final String testMethodName = testMethodCoverageFile.getName();
//
//					if (testMethodName.endsWith("Merged.exec") || testMethodName.endsWith(SpltCoverageGenerator.SUFFIX_MERGED)) {
//						testCaseCoverage.addClassCoverages(load(testMethodCoverageFile));
//						continue;
//					}
//
//					TestMethodCoverage testMethodCoverage = new TestMethodCoverage(testMethodName,
//							load(testMethodCoverageFile));
//					testCaseCoverage.addChild(testMethodCoverage);
//				}
//
//				productCoverage.addChild(testCaseCoverage);
//			}
			splCoverage.addChild(productCoverage);
		}
	}

	private Collection<IClassCoverage> load(File testMethodCoverageFile) throws IOException {
		ExecFileLoader execFileLoader = new ExecFileLoader();

		execFileLoader.load(testMethodCoverageFile);

		final ExecutionDataStore execStore = execFileLoader.getExecutionDataStore();

		final CoverageBuilder coverageBuilder = new CoverageBuilder();
		final Analyzer analyzer = new Analyzer(execStore, coverageBuilder);

		analyzer.analyzeAll(new File(classPath));

		return new HashSet<>(coverageBuilder.getClasses());
	}
}
