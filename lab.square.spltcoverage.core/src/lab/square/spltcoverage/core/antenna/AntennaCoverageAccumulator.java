package lab.square.spltcoverage.core.antenna;

import java.util.ArrayList;
import java.util.List;

import lab.square.spltcoverage.model.antenna.AntennaProductCoverage;

public class AntennaCoverageAccumulator {
	
	List<AntennaProductCoverage> accumulatedCoverages;
	
	
	public AntennaCoverageAccumulator() {
		accumulatedCoverages = new ArrayList<>();
	}
	
	public boolean isCoverageChanged() {
		return false;
	}

	public void accumulate(AntennaProductCoverage productCoverage) {
		accumulatedCoverages.add(productCoverage);
	}

}
