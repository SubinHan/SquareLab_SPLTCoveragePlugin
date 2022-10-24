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

import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.SplCoverage;
import lab.square.spltcoverage.utils.Tools;

/**
 * The CoverageReader class is the class reading the product coverages.
 * 
 * @author SQUARELAB
 *
 */
public final class SplCoverageReader extends AbstractSplCoverageReader {

	public static final String FEATURESET_FILENAME = "featureset.txt";

	private static String execDirectoryPath;
	private static String[] classpaths;
	private static SplCoverage splCoverage0;
	
	private String classpath;

	public SplCoverageReader(String classpath) {
		this.classpath = classpath;
	}

	@Override
	protected SplCoverage createSplCoverage() {
		return new SplCoverage("SPL");
	}

	@Override
	protected ProductCoverage read(File productDirectory) {
		ProductCoverage productCoverage = tryRead(productDirectory);
		if(productCoverage == null)
			return null;
		
		productCoverage.setName(productDirectory.getName());
		return productCoverage;
	}

	private ProductCoverage tryRead(File productDirectory) {
		ProductCoverage productCoverage = null;
		try {
			productCoverage = CoverageReader.read(productDirectory.getAbsolutePath(), classpath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return productCoverage;
	}

	@Override
	protected void hook(File product) {
		if (product.isDirectory())
			return;

		if (!Tools.isMergedCoverage(product.getName())) 
			return;
		
		loadMergedCoverage(product);
	}

	private void loadMergedCoverage(File product) {
		try {
			splCoverage.addClassCoverages(load(product, classpath));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
}
