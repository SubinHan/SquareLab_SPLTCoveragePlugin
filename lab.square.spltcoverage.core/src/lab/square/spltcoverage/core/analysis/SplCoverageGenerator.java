package lab.square.spltcoverage.core.analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import javax.management.MalformedObjectNameException;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import lab.square.spltcoverage.io.CoverageWriter;
import lab.square.spltcoverage.model.ProductSourceInfo;

public class SplCoverageGenerator {
	
	private static final Logger logger = Logger.getLogger(SplCoverageGenerator.class.getName());
	
	public static final String SUFFIX_MERGED = "__merged__.exec";
	public static final String PRODUCT_DIRECTORY_NAME = "product";
	public static final String FEATURESET_FILE_NAME = "featureset.txt";

	public void generateCoverage(IIterableSpltProvider provider) {
		int productNum = 0;
		Class[] targetClasses = provider.getTargetClasses();
		CoverageGenerator generator = new CoverageGenerator(targetClasses);
		while (provider.makeNextProduct()) {
			productNum++;
			String productDirectory;
			productDirectory = "/" + PRODUCT_DIRECTORY_NAME + productNum;
			String pathOfFeatureSet = provider.getBaseDirectory() + productDirectory + "/" + FEATURESET_FILE_NAME;
			writeFile(pathOfFeatureSet, provider.getFeatureSet().toString());

			JUnitCore junit = new JUnitCore();
			junit.addListener(new TestListener(provider, generator, productNum));
			junit.run(provider.getTestClasses());
		}
		mergeProducts(provider.getBaseDirectory());
	}

	private void mergeProducts(String baseDirectory) {
		File splFolder = new File(baseDirectory);
		FilenameFilter filter = (current, name) -> new File(current, name).isDirectory();
		File[] productExecs = new File[splFolder.list(filter).length];

		int index = 0;
		CoverageMerger merger = new CoverageMerger();
		for (File productFolder : splFolder.listFiles()) {
			if (!productFolder.isDirectory())
				continue;
			File productExec = null;
			productExec = mergeExecs(productFolder.getPath());
			productExecs[index++] = productExec;
		}
		try {
			merger.mergeExecs(new File(splFolder, splFolder.getName() + SUFFIX_MERGED), productExecs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static void writeFile(String filePath, String content) {
		File featureSet = new File(filePath);
		try {
			Files.createDirectories(Paths.get(featureSet.getParent()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try (BufferedWriter localFile = new BufferedWriter(new FileWriter(featureSet))) {
			// write file containing the current featureSet in the product folder.
			localFile.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void generateCoverage(String outputPath, ISpltProvider provider) {
		Collection<String> productPaths = new ArrayList<>();
		
		for(int i = 0; i < provider.getNumProducts(); i++) {
			ProductSourceInfo info = provider.getProductInfo(i);
			CoverageGenerator generator = null;
			generator = new CoverageGenerator();
			String thisOutputPath = outputPath + PRODUCT_DIRECTORY_NAME + i + "/";
			productPaths.add(thisOutputPath);
			generator.generateCoverage(new IProductProvider() {				
				@Override
				public Collection<String> getTestClassPaths() {
					return info.testClassPaths;
				}
				
				@Override
				public String getOutputPath() {
					return thisOutputPath;
				}
				
				@Override
				public String getClasspath() {
					return info.classpath;
				}
			});
		}
		for(String productPath : productPaths) {
			mergeExecs(productPath);
		}
	}

	public static File mergeExecs(String productDirectory) {
		File mergedExec = null;
		File productFolder = new File(productDirectory);
		FilenameFilter filter = (current, name) -> new File(current, name).isDirectory();
		File[] testCaseExecs = new File[productFolder.list(filter).length];

		int index = 0;
		CoverageMerger merger = new CoverageMerger();
		for (File testCaseFolder : productFolder.listFiles()) {
			if (!testCaseFolder.isDirectory())
				continue;
			File testCaseExec = new File(testCaseFolder, testCaseFolder.getName() + SUFFIX_MERGED);
			try {
				merger.mergeExecs(testCaseExec, testCaseFolder.listFiles());
			} catch (IOException e) {
				e.printStackTrace();
			}
			testCaseExecs[index++] = testCaseExec;
		}
		try {
			mergedExec = new File(productFolder, productFolder.getName() + SUFFIX_MERGED);
			merger.mergeExecs(mergedExec, testCaseExecs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mergedExec;
	}
	
	private class TestListener extends RunListener {
		IIterableSpltProvider provider;
		CoverageGenerator generator;
		int productNumber;

		public TestListener(IIterableSpltProvider provider, CoverageGenerator generator, int productNumber) {
			this.provider = provider;
			this.generator = generator;
			this.productNumber = productNumber;
		}

		@Override
		public void testStarted(Description description) throws Exception {
		}

		@Override
		public void testFinished(Description description) throws Exception {
			logger.info(description.getTestClass().getSimpleName() + "." + description.getMethodName() + " finished.");
			
			String testCaseDirectory;
			String testMethodDirectory;
			testCaseDirectory = "/" + description.getTestClass().getSimpleName();
			testMethodDirectory = "/" + description.getMethodName();
			String directory = provider.getBaseDirectory() + "/" + PRODUCT_DIRECTORY_NAME + productNumber + testCaseDirectory
					+ testMethodDirectory;
			
			JacocoConnection connection = JacocoConnection.getInstance();
			CoverageWriter.makeExecFile(directory, connection.getExecutionData(false));

			connection.resetData();
		}

		@Override
		public void testFailure(Failure failure) throws Exception {
			logger.severe("Test Failed: " + failure.getTestHeader());
			logger.severe(failure.getTrace());
			logger.severe(failure.getMessage());
		}
	}

}
