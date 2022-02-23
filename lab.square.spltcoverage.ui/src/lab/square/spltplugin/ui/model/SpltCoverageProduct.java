package lab.square.spltplugin.ui.model;

import java.util.Collection;
import java.util.Map;

import org.jacoco.core.analysis.IClassCoverage;

import lab.square.spltcoverage.model.ProductCoverage;

public class SpltCoverageProduct extends SpltCoverageItem implements ISpltCoverageItem {

	private final ProductCoverage coverage;
	
	public SpltCoverageProduct(ProductCoverage pc) {
		this.coverage = pc;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public Map<String, Boolean> getFeatureSet() {
		return coverage.getFeatureSet();
	}

	@Override
	public boolean isProblem() {		
		return false;
	}

	@Override
	public Collection<IClassCoverage> getClassCoverages() {
		return coverage.getClassCoverages();
	}


}
