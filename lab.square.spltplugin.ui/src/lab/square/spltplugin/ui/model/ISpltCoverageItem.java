package lab.square.spltplugin.ui.model;

import java.util.Map;

public interface ISpltCoverageItem {
	public String getName();
	public double getInstructionRatio();
	public double getLineRatio();
	public double getBranchRatio();
	public double getMethodRatio();
	public Map<String, Boolean> getFeatureSet();
	public boolean isProblem();
}
