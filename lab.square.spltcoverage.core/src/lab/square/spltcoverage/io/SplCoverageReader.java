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

	private static String execDirectoryPath;
	private static String classpath;
	private static SplCoverage splCoverage;

	public static void read() throws IOException {
		File folder = new File(execDirectoryPath);
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
			
			ProductCoverage productCoverage = CoverageReader.read(productFolder.getAbsolutePath(), classpath);

			splCoverage.addChild(productCoverage);
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

	public static void readInto(SplCoverage splCoverage, String execDirectoryPath, String classpath) throws IOException {
		SplCoverageReader.splCoverage = splCoverage;
		SplCoverageReader.execDirectoryPath = execDirectoryPath;
		SplCoverageReader.classpath = classpath;
		read();
	}
}
