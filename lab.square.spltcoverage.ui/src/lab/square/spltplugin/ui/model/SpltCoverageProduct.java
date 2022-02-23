package lab.square.spltplugin.ui.model;

import java.util.Collection;
import java.util.Map;

import org.jacoco.core.analysis.IClassCoverage;

import lab.square.spltcoverage.model.ProductCoverage;

public class SpltCoverageItem implements ISpltCoverageItem {

	ProductCoverage pc;

	public SpltCoverageItem(ProductCoverage pc) {
		this.pc = pc;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public double getInstructionRatio() {
		Collection<IClassCoverage> coverages = pc.getClassCoverages();

		int covered = 0;
		int missed = 0;

		for (IClassCoverage cc : coverages) {
			covered += cc.getInstructionCounter().getCoveredCount();
			missed += cc.getInstructionCounter().getMissedCount();
		}

		return (double) covered / (double) (covered + missed);
	}

	@Override
	public double getLineRatio() {
		Collection<IClassCoverage> coverages = pc.getClassCoverages();

		int covered = 0;
		int missed = 0;

		for (IClassCoverage cc : coverages) {
			covered += cc.getLineCounter().getCoveredCount();
			missed += cc.getLineCounter().getMissedCount();
		}

		return (double) covered / (double) (covered + missed);
	}

	@Override
	public double getBranchRatio() {
		Collection<IClassCoverage> coverages = pc.getClassCoverages();

		int covered = 0;
		int missed = 0;

		for (IClassCoverage cc : coverages) {
			covered += cc.getBranchCounter().getCoveredCount();
			missed += cc.getBranchCounter().getMissedCount();
		}

		return (double) covered / (double) (covered + missed);
	}

	@Override
	public double getMethodRatio() {
		Collection<IClassCoverage> coverages = pc.getClassCoverages();

		int covered = 0;
		int missed = 0;

		for (IClassCoverage cc : coverages) {
			covered += cc.getMethodCounter().getCoveredCount();
			missed += cc.getMethodCounter().getMissedCount();
		}

		return (double) covered / (double) (covered + missed);
	}

	@Override
	public Map<String, Boolean> getFeatureSet() {
		return pc.getFeatureSet();
	}

	@Override
	public boolean isProblem() {		
		return false;
	}

}
