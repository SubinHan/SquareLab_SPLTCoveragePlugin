package lab.square.spltcoverage.io.antenna;

import java.io.File;
import java.io.IOException;

import lab.square.spltcoverage.core.analysis.SplCoverageGenerator;
import lab.square.spltcoverage.io.AbstractSplCoverageReader;
import lab.square.spltcoverage.io.CoverageReader;
import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.SplCoverage;
import lab.square.spltcoverage.utils.Tools;

public class AntennaSplCoverageReader extends AbstractSplCoverageReader {

	private int currentProductNumber;
	private String splCoveragePath;
	private String[] classpaths;
	private String[] javaSourcePaths;
	
	public AntennaSplCoverageReader(String[] classpaths, String[] javaSourcePaths) {
		this.classpaths = classpaths;
		this.javaSourcePaths = javaSourcePaths;
		this.currentProductNumber = -1;
	}
	
	@Override
	protected SplCoverage createSplCoverage() {
		return new SplCoverage("AntennaSPL");
	}

	@Override
	protected ProductCoverage read(File productDirectory) {
		currentProductNumber = findProductNumber(productDirectory.getName()) - 1;
		
		ProductCoverage productCoverage = tryRead(productDirectory);
		if(productCoverage == null)
			return null;
		
		productCoverage.setName(productDirectory.getName());
		
		return productCoverage;
	}

	private int findProductNumber(String name) {
		String productPrefix = SplCoverageGenerator.PRODUCT_DIRECTORY_NAME;
		
		String productNumber = name.replace(productPrefix, "");
		int number = Integer.valueOf(productNumber);
		
		return number;
	}

	private ProductCoverage tryRead(File product) {
		ProductCoverage productCoverage = null;
		try {
			productCoverage = AntennaCoverageReader.read(product.getAbsolutePath(),
					classpaths[currentProductNumber], javaSourcePaths[currentProductNumber]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return productCoverage;
	}

	@Override
	protected void hook(File product) {		
	}
}
