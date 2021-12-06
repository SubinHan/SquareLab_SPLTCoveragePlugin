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
	public final IProxy proxy;
	
	public CoverageResult(Analyzer analyzer, CoverageBuilder coverageBuilder, IProxy proxy) {
		this.analyzer = analyzer;
		this.coverageBuilder = coverageBuilder;
		this.proxy = proxy;
	}

	public Analyzer getAnalyzer() {
		return analyzer;
	}

	public CoverageBuilder getCoverageBuilder() {
		return coverageBuilder;
	}

	public IProxy getProxy() {
		return proxy;
	}
	
	
}
