package lab.square.spltclient.example;

import lab.square.spltcoverage.io.AbstractSplCoverageReader;
import lab.square.spltcoverage.io.SplCoverageReaderFactory;
import lab.square.spltcoverage.model.ISplCoverageVisitor;
import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.SplCoverage;
import lab.square.spltcoverage.model.TestCaseCoverage;
import lab.square.spltcoverage.model.TestMethodCoverage;

public class Ex6ReadInvariantPlCoverage {
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

	public static SplCoverage readSplCoverage() {
		// We should define PL coverages path and classpath.
		final String PL_COVERAGE_PATH = "spltoutput/invariantpl";
		final String CLASSPATH = "target/classes/lab/square/spltclient/example/invarianttarget/elevatorsystem";

		// Create AbstractSplCoverageReader by SplCoverageReaderFactory.
		AbstractSplCoverageReader reader = SplCoverageReaderFactory.createInvariableSplCoverageReader(CLASSPATH);

		// read and return SplCoverage object.
		return reader.readSplCoverage(PL_COVERAGE_PATH);
	}
}
