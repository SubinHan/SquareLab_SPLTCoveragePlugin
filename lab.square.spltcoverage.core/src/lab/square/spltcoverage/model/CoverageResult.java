package lab.square.spltcoverage.model;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;

/**
 * The CoverageResult class is containing the coverage data that read by CoverageReader.
 * @author SQUARELAB
 *
 */
public class CoverageResult {
	public final Analyzer analyzer;
	public final CoverageBuilder coverageBuilder;
	
	public CoverageResult(Analyzer analyzer, CoverageBuilder coverageBuilder) {
		this.analyzer = analyzer;
		this.coverageBuilder = coverageBuilder;
	}

	public Analyzer getAnalyzer() {
		return analyzer;
	}

	public CoverageBuilder getCoverageBuilder() {
		return coverageBuilder;
	}	
	
}
