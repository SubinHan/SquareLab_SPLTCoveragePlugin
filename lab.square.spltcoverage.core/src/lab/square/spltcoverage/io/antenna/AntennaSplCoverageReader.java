package lab.square.spltcoverage.io.antenna;

import java.io.File;
import java.io.IOException;

import lab.square.spltcoverage.io.CoverageReader;
import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.SplCoverage;
import lab.square.spltcoverage.utils.Tools;

public class AntennaSplCoverageReader {

	public static SplCoverage read(String execDirectoryPath, String[] classpaths, String[] javaSourcePaths)
			throws IOException {
		File folder = new File(execDirectoryPath);
		if (!folder.exists())
			return null;

		File[] productFolders = folder.listFiles();

		SplCoverage splCoverage = new SplCoverage("spl");

		int productCount = 0;
		for (File productFolder : productFolders) {
			if (!productFolder.isDirectory()) {
				continue;
			}

			ProductCoverage productCoverage = AntennaCoverageReader.read(productFolder.getAbsolutePath(),
					classpaths[productCount], javaSourcePaths[productCount]);
			productCoverage.setName("product" + productCount);
			productCount++;

			splCoverage.addChild(productCoverage);
		}

		return splCoverage;
	}
}
