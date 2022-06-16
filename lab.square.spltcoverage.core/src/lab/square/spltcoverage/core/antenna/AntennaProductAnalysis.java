package lab.square.spltcoverage.core.antenna;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICounter;

import lab.square.spltcoverage.core.antenna.model.AntennaProductCoverage;
import lab.square.spltcoverage.core.antenna.model.ExpressionNode;
import lab.square.spltcoverage.utils.Tools;

public class AntennaProductAnalysis {
	private final AntennaProductCoverage pc;
	private Map<String, Collection<FeatureCoverage>> fcs;

	public AntennaProductAnalysis(AntennaProductCoverage pc) {
		this.pc = pc;
		this.fcs = new HashMap<>();
		initFeatureCoverage();
	}

	private void initFeatureCoverage() {
		for (IClassCoverage cc : pc.getClassCoverages()) {
			String name = Tools.convertClassNameByConvention(cc.getName());
			Collection<FeatureLocation> fls = pc.getFeatureLocationsOf(name);
			List<FeatureCoverageMut> fcms = new ArrayList<>();
			for (FeatureLocation fl : fls) {
				try {
					String featureExpression = FeatureLocation.expressionToString(fl.getFeatureExpression());
					ExpressionNode node = FeatureExpressionParser.parse(featureExpression);
					if(!fl.isFeatureLocationOf(pc.getFeatureSet()))
						continue;
					
					for (int i = fl.getLineStart(); i <= fl.getLineEnd(); i++) {
						if (cc.getLine(i).getStatus() == ICounter.EMPTY) {
							continue;
						}
						
						FeatureCoverageMut fc = findFeatureCoverage(fcms, node);
						if(fc == null) {
							fc = new FeatureCoverageMut(node);
							fcms.add(fc);
						}
						
						if (cc.getLine(i).getStatus() == ICounter.FULLY_COVERED) {
							fc.numCoveredLines++;
						}
						if (cc.getLine(i).getStatus() == ICounter.PARTLY_COVERED) {
							fc.numPartlyCoveredLines++;
						}
						fc.numLines++;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			List<FeatureCoverage> fcs = new ArrayList<>();
			for(FeatureCoverageMut fcm : fcms) {
				fcs.add(new FeatureCoverage(fcm.node, fcm.numLines, fcm.numPartlyCoveredLines, fcm.numCoveredLines));
			}
			
			this.fcs.put(name, fcs);
		}
	}
	
	private FeatureCoverageMut findFeatureCoverage(List<FeatureCoverageMut> fcs, ExpressionNode node) {
		for(FeatureCoverageMut fc : fcs) {
			if(fc.node.equals(node))
				return fc;
		}
		return null;
	}

	public final class FeatureCoverage {
		public final ExpressionNode node;
		public final int numLines;
		public final int numPartlyCoveredLines;
		public final int numCoveredLines;

		public FeatureCoverage(ExpressionNode node, int numLines, int numPartlyCoveredLines, int numCoveredLines) {
			super();
			this.node = node;
			this.numLines = numLines;
			this.numPartlyCoveredLines = numPartlyCoveredLines;
			this.numCoveredLines = numCoveredLines;
		}
	}

	private final class FeatureCoverageMut {
		ExpressionNode node;
		int numLines;
		int numPartlyCoveredLines;
		int numCoveredLines;
		
		public FeatureCoverageMut(ExpressionNode node) {
			this.node = node;
			numLines = 0;
			numPartlyCoveredLines = 0;
			numCoveredLines = 0;
		}
	}

	public Collection<FeatureCoverage> getFeatureCoverages(String className) {
		className = Tools.convertClassNameByConvention(className);
		
		if(!fcs.containsKey(className))
			return new ArrayList<FeatureCoverage>();
		
		return fcs.get(className);
	}

}
