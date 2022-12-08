package lab.square.spltclient.example;

import lab.square.spltcoverage.io.AbstractSplCoverageReader;
import lab.square.spltcoverage.io.SplCoverageReaderFactory;
import lab.square.spltcoverage.model.ISplCoverageVisitor;
import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.SplCoverage;
import lab.square.spltcoverage.model.TestCaseCoverage;
import lab.square.spltcoverage.model.TestMethodCoverage;

public class Ex2ReadAntennaPlCoverage {

	private static final int NUM_PRODUCTS = 16;

	public static void main(String[] args) {
		// read SplCoverage before we generated.
		SplCoverage splCoverage = readSplCoverage();

		// We can visit recursively coverage hierarchy by using the Visitor Patterns.
		splCoverage.accept(new ISplCoverageVisitor() {

			@Override
			public void visit(SplCoverage pcm) {
				for (ProductCoverage pc : pcm.getProductCoverages())
					this.visit(pc);
			}

			@Override
			public void visit(ProductCoverage pc) {
				System.out.println("Feature: " + pc.getFeatureSet().getFeatures());
				System.out.println("Score: " + pc.getScore());
				System.out.println();
			}

			@Override
			public void visit(TestCaseCoverage tcc) {

			}

			@Override
			public void visit(TestMethodCoverage tmc) {

			}
		});
	}

	private static String[] makeProductDirectoryArray() {
		// Set directories of product1~16.

		String[] productDirectories = new String[NUM_PRODUCTS];

		final String PL_BASE_DIRECTORY = "antennapl";
		final String PRODUCT_PREFIX = "Elevator-Antenna-v1.2-000";

		for (int i = 0; i < Math.min(9, NUM_PRODUCTS); i++) {
			productDirectories[i] = PL_BASE_DIRECTORY + "/" + PRODUCT_PREFIX + "0" + (i + 1);
		}

		for (int i = 9; i < NUM_PRODUCTS; i++) {
			productDirectories[i] = PL_BASE_DIRECTORY + "/" + PRODUCT_PREFIX + (i + 1);
		}

		return productDirectories;
	}

	public static SplCoverage readSplCoverage() {
		// We should define PL coverages path and
		// classpath of each product.
		final String PL_COVERAGE_PATH = "spltoutput/antennapl";

		String[] productDirectories = makeProductDirectoryArray();
		String[] productSourcePaths = new String[NUM_PRODUCTS];
		String[] classpathList = new String[NUM_PRODUCTS];

		final String CLASSPATH = "bin";
		final String SRCPATH = "src";

		for (int i = 0; i < NUM_PRODUCTS; i++) {
			classpathList[i] = productDirectories[i] + "/" + CLASSPATH;
			productSourcePaths[i] = productDirectories[i] + "/" + SRCPATH;
		}

		// Create AbstractSplCoverageReader by SplCoverageReaderFactory.
		// If your PL coverages have invariant source code base, then create the other
		// SplCoverageReader.
		AbstractSplCoverageReader reader = SplCoverageReaderFactory.createAntennaSplCoverageReader(classpathList,
				productSourcePaths);

		// read and return SplCoverage object.
		return reader.readSplCoverage(PL_COVERAGE_PATH);
	}
}
