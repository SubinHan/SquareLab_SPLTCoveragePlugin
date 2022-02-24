package lab.square.spltplugin.ui.model;

import java.util.Collection;
import java.util.Map;

import org.jacoco.core.analysis.IClassCoverage;

import lab.square.spltcoverage.model.TestMethodCoverage;

public class SpltCoverageTestMethod extends SpltCoverageItem {

	private TestMethodCoverage coverage;

	public SpltCoverageTestMethod(TestMethodCoverage coverage) {
		this.coverage = coverage;
	}
	
	@Override
	public Map<String, Boolean> getFeatureSet() {
		return null;
	}

	@Override
	public boolean isProblem() {
		return false;
	}

	@Override
	public Collection<IClassCoverage> getClassCoverages() {
		return coverage.getClassCoverages();
	}

	@Override
	public String getName() {
		return coverage.getName();
	}

}
