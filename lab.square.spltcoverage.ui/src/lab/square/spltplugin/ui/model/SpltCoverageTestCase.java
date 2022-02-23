package lab.square.spltplugin.ui.model;

import java.util.Collection;
import java.util.Map;

import org.jacoco.core.analysis.IClassCoverage;

import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.TestCaseCoverage;

public class SpltCoverageTestCase extends SpltCoverageItem implements ISpltCoverageItem {
	
	private TestCaseCoverage coverage;

	public SpltCoverageTestCase(TestCaseCoverage coverage) {
		this.coverage = coverage;
	}
	
	@Override
	public String getName() {
		return coverage.getName();
	}

	@Override
	public Map<String, Boolean> getFeatureSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isProblem() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public Collection<IClassCoverage> getClassCoverages() {
		return coverage.getClassCoverages();
	}

}
