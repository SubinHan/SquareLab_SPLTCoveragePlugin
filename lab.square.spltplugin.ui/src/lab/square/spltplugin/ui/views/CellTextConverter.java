package lab.square.spltplugin.ui.views;

import java.text.DecimalFormat;
import java.util.Collection;

import org.jacoco.core.analysis.IClassCoverage;

import lab.square.spltcoverage.model.ICoverageModelComponent;

public class CellTextConverter {
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##.##%");
	
	public String getElementName(Object element) {
		
		return ((ICoverageModelComponent)element).getName();
	}
	
	public double getInstructionRatio(Collection<IClassCoverage> coverages) {

		int covered = 0;
		int missed = 0;

		for (IClassCoverage cc : coverages) {
			covered += cc.getInstructionCounter().getCoveredCount();
			missed += cc.getInstructionCounter().getMissedCount();
		}

		return (double) covered / (double) (covered + missed);
	}
	
	public double getLineRatio(Collection<IClassCoverage> coverages) {

		int covered = 0;
		int missed = 0;

		for (IClassCoverage cc : coverages) {
			covered += cc.getLineCounter().getCoveredCount();
			missed += cc.getLineCounter().getMissedCount();
		}

		return (double) covered / (double) (covered + missed);
	}
	
	public double getBranchRatio(Collection<IClassCoverage> coverages) {

		int covered = 0;
		int missed = 0;

		for (IClassCoverage cc : coverages) {
			covered += cc.getBranchCounter().getCoveredCount();
			missed += cc.getBranchCounter().getMissedCount();
		}

		return (double) covered / (double) (covered + missed);
	}
	
	public double getMethodRatio(Collection<IClassCoverage> coverages) {

		int covered = 0;
		int missed = 0;

		for (IClassCoverage cc : coverages) {
			covered += cc.getMethodCounter().getCoveredCount();
			missed += cc.getMethodCounter().getMissedCount();
		}

		return (double) covered / (double) (covered + missed);
	}
	
}
