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
import lab.square.spltcoverage.utils.Tools;

/**
 * The CoverageReader class is the class reading the product coverages.
 * @author SQUARELAB
 *
 */
public final class SplCoverageReader {

	public static final String FEATURESET_FILENAME = "featureset.txt";

	private static String execDirectoryPath;
	private static String[] classpaths;
	private static SplCoverage splCoverage;
	
	private SplCoverageReader() {
		
	}

	private static void read() throws IOException {
		File folder = new File(execDirectoryPath);
		if (!folder.exists())
			return;

		File[] productFolders = folder.listFiles();

		int productCount = 0;
		for (File productFolder : productFolders) {
			readProductIntoSplCoverage(productFolder, classpaths[productCount++]);
		}
	}

	private static void readProductIntoSplCoverage(File productFolder, String classpath) throws IOException {
		if (!productFolder.isDirectory()) {
			if (Tools.isMergedCoverage(productFolder.getName())) {
				splCoverage.addClassCoverages(load(productFolder, classpaths[0]));
			}
			return;
		}
		
		ProductCoverage productCoverage = CoverageReader.read(productFolder.getAbsolutePath(), classpath);
		productCoverage.setName(productFolder.getName());
		
		splCoverage.addChild(productCoverage);
	}

	private static Collection<IClassCoverage> load(File testMethodCoverageFile, String classpath) throws IOException {
		ExecFileLoader execFileLoader = new ExecFileLoader();

		execFileLoader.load(testMethodCoverageFile);

		final ExecutionDataStore execStore = execFileLoader.getExecutionDataStore();

		final CoverageBuilder coverageBuilder = new CoverageBuilder();
		final Analyzer analyzer = new Analyzer(execStore, coverageBuilder);

		analyzer.analyzeAll(new File(classpath));

		return new HashSet<>(coverageBuilder.getClasses());
	}
	
	public static void readInvariablePlCoverageInto(SplCoverage dest, String execDirectoryPath, String classpath) throws IOException {
		SplCoverageReader.splCoverage = dest;
		SplCoverageReader.execDirectoryPath = execDirectoryPath;
		File folder = new File(execDirectoryPath);
		SplCoverageReader.classpaths = new String[folder.listFiles().length];
		for(int i = 0; i < classpaths.length; i++) {
			classpaths[i] = classpath;
		}
		read();
	}

	public static void readVariablePlCoverageInto(SplCoverage dest, String execDirectoryPath, String[] classpaths) throws IOException {
		SplCoverageReader.splCoverage = dest;
		SplCoverageReader.execDirectoryPath = execDirectoryPath;
		SplCoverageReader.classpaths = classpaths;
		read();
	}
}
