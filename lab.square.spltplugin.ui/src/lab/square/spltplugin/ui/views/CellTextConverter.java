package lab.square.spltplugin.ui.views;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jacoco.core.analysis.IClassCoverage;

import lab.square.spltcoverage.model.ICoverageModelComponent;
import lab.square.spltcoverage.model.ProductCoverage;

public class CellTextConverter {
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##.##%");
	
	public String getElementName(Object element) {
		System.out.println(((ICoverageModelComponent)element).getName());
		return ((ICoverageModelComponent)element).getName();
	}
	
	public String getInstructionRatio(Collection<IClassCoverage> coverages) {

		int covered = 0;
		int missed = 0;

		for (IClassCoverage cc : coverages) {
			covered += cc.getInstructionCounter().getCoveredCount();
			missed += cc.getInstructionCounter().getMissedCount();
		}

		return DECIMAL_FORMAT.format((double) covered / (double) (covered + missed));
	}
	
	public String getLineRatio(Collection<IClassCoverage> coverages) {

		int covered = 0;
		int missed = 0;

		for (IClassCoverage cc : coverages) {
			covered += cc.getLineCounter().getCoveredCount();
			missed += cc.getLineCounter().getMissedCount();
		}

		return DECIMAL_FORMAT.format((double) covered / (double) (covered + missed));
	}
	
	public String getBranchRatio(Collection<IClassCoverage> coverages) {

		int covered = 0;
		int missed = 0;

		for (IClassCoverage cc : coverages) {
			covered += cc.getBranchCounter().getCoveredCount();
			missed += cc.getBranchCounter().getMissedCount();
		}

		return DECIMAL_FORMAT.format((double) covered / (double) (covered + missed));
	}
	
	public String getMethodRatio(Collection<IClassCoverage> coverages) {

		int covered = 0;
		int missed = 0;

		for (IClassCoverage cc : coverages) {
			covered += cc.getMethodCounter().getCoveredCount();
			missed += cc.getMethodCounter().getMissedCount();
		}

		return DECIMAL_FORMAT.format((double) covered / (double) (covered + missed));
	}

	public String getFeatureSet(Object element) {
		if(element instanceof ProductCoverage) {
			ProductCoverage pc = (ProductCoverage) element;
			return toString(pc.getFeatureSet());
		}
		return null;
	}
	
	private String toString(Map<String, Boolean> featureSet) {		
		List<String> keyList = new ArrayList<String>();
		
		for(String key : featureSet.keySet()) {
			if(featureSet.get(key))
				keyList.add(key);
		}
		
		keyList = keyList.stream().map(String::toLowerCase).collect(Collectors.toList());
		keyList.sort(Comparator.naturalOrder());
		
		StringBuilder builder = new StringBuilder();
		
		for(String key : keyList) {
			builder.append(key.toLowerCase() + " ");
		}
		
		return builder.toString();
	}

	public String getNumOfFeatures(Object element) {
		if(element instanceof ProductCoverage) {
			ProductCoverage pc = (ProductCoverage) element;
			
			int numOfFeatures = 0;
			
			for(String key : pc.getFeatureSet().keySet()) {
				if(pc.getFeatureSet().get(key))
					numOfFeatures++;
			}
			
			return String.valueOf(numOfFeatures);
		}
		return null;
	}
	
}
