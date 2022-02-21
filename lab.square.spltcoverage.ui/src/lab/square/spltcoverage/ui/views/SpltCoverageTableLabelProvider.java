package lab.square.spltcoverage.ui.views;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import lab.square.spltplugin.ui.model.ISpltCoverageItem;

public class SpltCoverageTableLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {

		if (!(element instanceof ISpltCoverageItem))
			return "";

		ISpltCoverageItem item = (ISpltCoverageItem) element;

		switch (columnIndex) {
		case 0:
			return item.getName();
		case 1:
			return toDecimalFormat(item.getInstructionRatio());
		case 2:
			return toDecimalFormat(item.getLineRatio());
		case 3:
			return toDecimalFormat(item.getBranchRatio());
		case 4:
			return toDecimalFormat(item.getMethodRatio());
		case 5:
			return toString(item.getFeatureSet());
		case 6:
			return String.valueOf(item.isProblem());
		default:
			return "";
		}
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
			builder.append(key.toLowerCase());
		}
		
		return builder.toString();
	}
	
	private String toDecimalFormat(double number) {
		DecimalFormat df = new DecimalFormat("##.##%");
		return df.format(number);
	}

}
