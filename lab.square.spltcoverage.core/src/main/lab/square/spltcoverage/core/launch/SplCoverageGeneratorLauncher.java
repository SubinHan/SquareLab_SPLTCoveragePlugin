package lab.square.spltcoverage.core.launch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import lab.square.spltcoverage.core.analysis.SplCoverageGenerator;
import lab.square.spltcoverage.model.ProductSourceInfo;

public final class SplCoverageGeneratorLauncher {

	private SplCoverageGeneratorLauncher() {
	}

	public static void launch(String outputPath, List<ProductSourceInfo> productSourceInfos) {
		Path output = Paths.get(outputPath);
		try {
			Files.createDirectories(output);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		for (int i = 0; i < productSourceInfos.size(); i++) {
			Path productOutputPath = output.resolve(SplCoverageGenerator.PRODUCT_DIRECTORY_NAME + (i + 1));

			Path featureSetPath = productOutputPath.resolve(SplCoverageGenerator.FEATURESET_FILE_NAME);
			SplCoverageGenerator.writeFile(featureSetPath.toString(), productSourceInfos.get(i).featureSet.toString());

			CoverageGeneratorLauncher.launch(productSourceInfos.get(i).classpath,
					productSourceInfos.get(i).testClassPaths, productOutputPath.toString(), productSourceInfos.get(i).additionalDependencies);
		}
	}
}
