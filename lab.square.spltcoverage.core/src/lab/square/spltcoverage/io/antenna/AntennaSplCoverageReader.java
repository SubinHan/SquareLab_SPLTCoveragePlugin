package lab.square.spltcoverage.io.antenna;

import java.io.File;
import java.io.IOException;

import lab.square.spltcoverage.io.AbstractSplCoverageReader;
import lab.square.spltcoverage.io.CoverageReader;
import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.SplCoverage;
import lab.square.spltcoverage.utils.Tools;

public class AntennaSplCoverageReader extends AbstractSplCoverageReader {

	private int productCount;
	private String splCoveragePath;
	private String[] classpaths;
	private String[] javaSourcePaths;
	
	public AntennaSplCoverageReader(String[] classpaths, String[] javaSourcePaths) {
		this.classpaths = classpaths;
		this.javaSourcePaths = javaSourcePaths;
		this.productCount = 0;
	}
	
	@Override
	protected SplCoverage createSplCoverage() {
		return new SplCoverage("AntennaSPL");
	}

	@Override
	protected ProductCoverage read(File productDirectory) {
		ProductCoverage productCoverage = tryRead(productDirectory);
		if(productCoverage == null)
			return null;
		
		productCoverage.setName(productDirectory.getName());
		productCount++;
		
		return productCoverage;
	}

	private ProductCoverage tryRead(File product) {
		ProductCoverage productCoverage = null;
		try {
			productCoverage = AntennaCoverageReader.read(product.getAbsolutePath(),
					classpaths[productCount], javaSourcePaths[productCount]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return productCoverage;
	}

	@Override
	protected void hook(File product) {		
	}
}
