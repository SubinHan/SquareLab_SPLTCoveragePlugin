package lab.square.spltcoverage.core.antenna;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICounter;

import lab.square.spltcoverage.model.antenna.AntennaProductCoverage;
import lab.square.spltcoverage.utils.Tools;

public class AntennaCoverageAccumulator {
	
	List<AntennaProductCoverage> accumulatedProductCoverages;
	Map<String, CoverageOfSingleClass> accumulated;
	Set<String> visitedFeatures;
	
	boolean isCoverageChanged;
	Set<String> newlyVisitedFeatures;
	Map<String, Integer> newlyCoveredLineCount;
	Set<String> newlyCoveredClasses;
	Map<String, CoverageOfSingleClass> diffWithPrevious;
	int totalCovered;
	int totalActivated;
	
	public AntennaCoverageAccumulator() {
		accumulatedProductCoverages = new ArrayList<>();
		accumulated = new HashMap<>();
		visitedFeatures = new HashSet<>();
		newlyVisitedFeatures = new HashSet<>();
		newlyCoveredLineCount = new HashMap<>();
		newlyCoveredClasses = new HashSet<>();
		diffWithPrevious = new HashMap<>();
	}
	
	public boolean isCoverageChanged() {
		return isCoverageChanged;
	}

	public void accumulate(AntennaProductCoverage productCoverage) {
		clearPreviousStates();
		accumulatedProductCoverages.add(productCoverage);
		updateNewlyVisitedFeatures(productCoverage);
		
		for(IClassCoverage classCoverage : productCoverage.getClassCoverages()) {
			accumulateCoverageOfClass(classCoverage);
		}
	}

	private void clearPreviousStates() {
		newlyVisitedFeatures.clear();
		isCoverageChanged = false;
		newlyCoveredLineCount.clear();
		newlyCoveredClasses.clear();
		diffWithPrevious.clear();
	}

	private void updateNewlyVisitedFeatures(AntennaProductCoverage newProductCoverage) {
		for(String feature : newProductCoverage.getFeatureSet().getFeatures()) {
			if(visitedFeatures.contains(feature))
				continue;
			
			visitedFeatures.add(feature);
			newlyVisitedFeatures.add(feature);
		}
		
	}

	private void accumulateCoverageOfClass(IClassCoverage classCoverage) {
		if(!classCoverage.containsCode())
			return;
		
		String className = Tools.convertClassNameByConvention(classCoverage.getName());
		CoverageOfSingleClass coverage = new CoverageOfSingleClass(classCoverage);
		
		accumulated.compute(className, (k, v) -> {
			if(v == null) {
				updateNewlyCovered(className, coverage);
				return coverage;
			}
			
			CoverageOfSingleClass diff = coverage.subtract(v);
			updateNewlyCovered(className, diff);

			return v.add(coverage);
		});
	}
	
	private void updateNewlyCovered(String className, CoverageOfSingleClass coverage) {
		diffWithPrevious.put(className, coverage);
		
		if(coverage.isNotCoveredAnything())
			return;
		isCoverageChanged = true;
		newlyCoveredLineCount.put(className, coverage.getCoveredCount());
		newlyCoveredClasses.add(className);
	}

	public Collection<String> getNewlyVisitedFeatures() {
		return newlyVisitedFeatures;
	}
	
	public int getNewlyCoveredLineCountOfClass(String classNameWithDots) {
		if(!newlyCoveredLineCount.containsKey(classNameWithDots))
			return 0;
		
		return newlyCoveredLineCount.get(classNameWithDots);
	}
	
	public Collection<String> getNewlyCoveredClaases() {
		return newlyCoveredClasses;
	}

	public int getNewlyActivatedLineCountOfClass(String classNameWithDots) {
		if(!diffWithPrevious.containsKey(classNameWithDots))
			return 0;
		
		return diffWithPrevious.get(classNameWithDots).getActivatedLineCount();
	}

	public int getTotalActivatedLineCountOfClass(String classNameWithDots) {
		CoverageOfSingleClass coverage = accumulated.get(classNameWithDots);
		
		return coverage.getActivatedLineCount();
	}

	public int getTotalCoveredLineCountOfClass(String classNameWithDots) {
		CoverageOfSingleClass coverage = accumulated.get(classNameWithDots);
		return coverage.getCoveredCount();
	}

	private class CoverageOfSingleClass {
		
		private int[] lines;
		int lastLine;
		
		public CoverageOfSingleClass(IClassCoverage classCoverage) {			
			if(!isClassCoverageAvailable()) {
				lines = new int[0];
				return;
			}
			
			lastLine = classCoverage.getLastLine();
				
			lines = new int[lastLine + 1];
			initStatus(classCoverage);
		}
		
		public CoverageOfSingleClass(CoverageOfSingleClass classCoverage) {		
			lastLine = classCoverage.lastLine;
			lines = new int[lastLine + 1];
			System.arraycopy(classCoverage.lines, 0, this.lines, 0, lastLine + 1);
		}
		
		public boolean isClassCoverageAvailable() {
			return lastLine != -1;
		}

		private void initStatus(IClassCoverage classCoverage) {
			initLineStatusAsEmpty();
			initEachLineStatus(classCoverage);
		}

		private void initEachLineStatus(IClassCoverage classCoverage) {
			for(int i = 0; i <= lastLine; i++) {
				lines[i] = classCoverage.getLine(i).getStatus();
			}
		}

		private void initLineStatusAsEmpty() {
			for(int i = 0; i < lines.length; i++) {
				lines[i] = ICounter.EMPTY;
			}
		}
		
		public CoverageOfSingleClass add(CoverageOfSingleClass other) {
			CoverageOfSingleClass bigger;
			CoverageOfSingleClass smaller;
			bigger = this;
			smaller = other;
			
			if(!hasMoreLineThan(other)) {
				bigger = other;
				smaller = this;
			}
			
			CoverageOfSingleClass result = new CoverageOfSingleClass(bigger);
			for(int i = 0; i <= smaller.lastLine; i++) {
				if(isGreaterEqualThan(bigger.lines[i], smaller.lines[i])) {
					result.lines[i] = smaller.lines[i];
				}
			}
			
			return result;
		}
		

		public CoverageOfSingleClass subtract(CoverageOfSingleClass other) {
			CoverageOfSingleClass smaller;
			smaller = other;
			
			if(!hasMoreLineThan(other)) {
				smaller = this;
			}
			
			CoverageOfSingleClass result = new CoverageOfSingleClass(this);
			for(int i = 0; i <= smaller.lastLine; i++) {
				if(isGreaterEqualThan(result.lines[i], other.lines[i])) {
					result.lines[i] = ICounter.EMPTY;
				}
			}
			
			return result;
		}

		private boolean hasMoreLineThan(CoverageOfSingleClass other) {			
			return this.lastLine > other.lastLine;
		}

		private boolean isGreaterEqualThan(int status1, int status2) {
			if(status1 == ICounter.EMPTY)
				return true;
			
			if(status1 == ICounter.NOT_COVERED)
				return status2 != ICounter.EMPTY;
			
			if(status1 == ICounter.PARTLY_COVERED)
				return status2 == ICounter.PARTLY_COVERED || status2 == ICounter.FULLY_COVERED;
			
			return status2 == ICounter.FULLY_COVERED;
		}
		
		public boolean isNotCoveredAnything() {
			for(int i = 0; i < lines.length; i++) {
				if(lines[i] != ICounter.EMPTY && lines[i] != ICounter.NOT_COVERED)
					return false;
			}
			return true;
		}
		
		public int getCoveredCount() {
			int count = 0;
			
			for(int i = 0; i < lines.length; i++) {
				if(lines[i] == ICounter.FULLY_COVERED)
					count++;
			}
			
			return count;
		}
		
		public int getActivatedLineCount() {
			int count = 0;
			
			for(int i = 0; i < lines.length; i++) {
				if(lines[i] != ICounter.EMPTY)
					count++;
			}
			
			return count;
		}
		
	}

}
