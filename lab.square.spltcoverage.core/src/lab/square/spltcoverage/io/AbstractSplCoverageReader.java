package lab.square.spltcoverage.io;

import java.io.File;

import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.SplCoverage;

public abstract class AbstractSplCoverageReader {
	
	protected abstract SplCoverage createSplCoverage();
	protected abstract ProductCoverage read(File product);
	protected abstract void hook(File product);
	
	protected SplCoverage splCoverage;
	
	public SplCoverage readSplCoverage(String splCoveragePath) {
		File splCoverageFolder = new File(splCoveragePath);
		if (!splCoverageFolder.exists())
			return null;

		SplCoverage splCoverage = readSplCoverage(splCoverageFolder);
		
		return splCoverage;
	}
	
	private SplCoverage readSplCoverage(File splCoverageFolder) {
		File[] productFolders = splCoverageFolder.listFiles();
		splCoverage = createSplCoverage();
		
		for (File product : productFolders) {
			ProductCoverage pc = read(product);
			hook(product);
			splCoverage.addChild(pc);
		}
		return splCoverage;
	}
}
