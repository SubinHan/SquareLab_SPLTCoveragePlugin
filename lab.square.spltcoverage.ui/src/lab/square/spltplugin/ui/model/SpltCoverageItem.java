package lab.square.spltplugin.ui.model;

import java.util.Collection;
import java.util.Map;

import org.jacoco.core.analysis.IClassCoverage;

public abstract class SpltCoverageItem implements ISpltCoverageItem {
	
	abstract public Collection<IClassCoverage> getClassCoverages();
	abstract public String getName();
	public double getInstructionRatio() {
		Collection<IClassCoverage> coverages = getClassCoverages();

		int covered = 0;
		int missed = 0;

		for (IClassCoverage cc : coverages) {
			covered += cc.getInstructionCounter().getCoveredCount();
			missed += cc.getInstructionCounter().getMissedCount();
		}

		return (double) covered / (double) (covered + missed);
	}
	public double getLineRatio() {
		Collection<IClassCoverage> coverages = getClassCoverages();

		int covered = 0;
		int missed = 0;

		for (IClassCoverage cc : coverages) {
			covered += cc.getLineCounter().getCoveredCount();
			missed += cc.getLineCounter().getMissedCount();
		}

		return (double) covered / (double) (covered + missed);
	}
	public double getBranchRatio() {
		Collection<IClassCoverage> coverages = getClassCoverages();

		int covered = 0;
		int missed = 0;

		for (IClassCoverage cc : coverages) {
			covered += cc.getBranchCounter().getCoveredCount();
			missed += cc.getBranchCounter().getMissedCount();
		}

		return (double) covered / (double) (covered + missed);
	}
	public double getMethodRatio() {
		Collection<IClassCoverage> coverages = getClassCoverages();

		int covered = 0;
		int missed = 0;

		for (IClassCoverage cc : coverages) {
			covered += cc.getMethodCounter().getCoveredCount();
			missed += cc.getMethodCounter().getMissedCount();
		}

		return (double) covered / (double) (covered + missed);
	}
}
