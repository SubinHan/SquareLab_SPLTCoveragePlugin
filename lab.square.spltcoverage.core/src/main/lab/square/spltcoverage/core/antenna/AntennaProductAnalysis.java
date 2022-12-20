package lab.square.spltcoverage.core.antenna;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICounter;

import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.model.antenna.AntennaProductCoverage;
import lab.square.spltcoverage.model.antenna.ExpressionNode;
import lab.square.spltcoverage.model.antenna.FeatureLocation;
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
			String name = Tools.convertClassNameByConventionAndRemoveDollar(cc.getName());
			Collection<FeatureLocation> fls = pc.getFeatureLocationsOfClass(name);
			List<FeatureCoverageMut> fcms = createFeatureCoveragesOfClass(cc, fls);

			this.fcs.put(name, convertFeatureCoverageMutToImmut(fcms));
		}
	}

	private List<FeatureCoverageMut> createFeatureCoveragesOfClass(IClassCoverage cc, Collection<FeatureLocation> fls) {
		List<FeatureCoverageMut> fcms = new ArrayList<>();
		for (FeatureLocation fl : fls) {
			try {
				String featureExpression = FeatureLocation.expressionToString(fl.getFeatureExpression());
				List<String> tokens = FeatureExpressionTokenizer.tokenize(featureExpression);

				ExpressionNode node = FeatureExpressionParser
						.parseByTokens(tokens.toArray(new String[tokens.size()]));
				if (!fl.isFeatureLocationOf(pc.getFeatureSet()))
					continue;

				for (int i = fl.getLineStart(); i <= fl.getLineEnd(); i++) {
					if (cc.getLine(i).getStatus() == ICounter.EMPTY) {
						continue;
					}

					FeatureCoverageMut fc = findFeatureCoverage(fcms, node);
					if (fc == null) {
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
		return fcms;
	}

	private FeatureCoverageMut findFeatureCoverage(List<FeatureCoverageMut> fcs, ExpressionNode node) {
		for (FeatureCoverageMut fc : fcs) {
			if (fc.node.equals(node))
				return fc;
		}
		return null;
	}

	public static final class FeatureCoverage {
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

	private static final class FeatureCoverageMut {
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

		public void add(FeatureCoverage fc) {
			this.numLines += fc.numLines;
			this.numPartlyCoveredLines += fc.numPartlyCoveredLines;
			this.numCoveredLines += fc.numCoveredLines;
		}
	}

	public Collection<FeatureCoverage> getFeatureCoveragesOfClass(String className) {
		className = Tools.convertClassNameByConvention(className);

		if (!fcs.containsKey(className))
			return new ArrayList<>();

		return fcs.get(className);
	}

	public Collection<FeatureCoverage> getFeatureCoverage(FeatureSet targetFeatureSet) {
		List<FeatureCoverageMut> fcms = new ArrayList<>();
		for (IClassCoverage cc : pc.getClassCoverages()) {
			String name = Tools.convertClassNameByConventionAndRemoveDollar(cc.getName());

			for (FeatureCoverage fc : fcs.get(name)) {
				if (FeatureExpressionParser.evaluate(fc.node, targetFeatureSet)) {
					FeatureCoverageMut fcm = findFeatureCoverage(fcms, fc.node);
					if (fcm == null) {
						fcm = new FeatureCoverageMut(fc.node);
						fcms.add(fcm);
					}

					fcm.add(fc);
				}
			}
		}

		return convertFeatureCoverageMutToImmut(fcms);
	}

	private static Collection<FeatureCoverage> convertFeatureCoverageMutToImmut(Collection<FeatureCoverageMut> fcs) {
		Collection<FeatureCoverage> result = new ArrayList<>();

		for (FeatureCoverageMut fc : fcs) {
			result.add(new FeatureCoverage(fc.node, fc.numLines, fc.numPartlyCoveredLines, fc.numCoveredLines));
		}

		return result;
	}

}
