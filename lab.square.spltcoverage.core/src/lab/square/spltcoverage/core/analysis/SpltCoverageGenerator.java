package lab.square.spltcoverage.core.analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.management.MalformedObjectNameException;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import lab.square.spltcoverage.io.CoverageWriter;
import lab.square.spltcoverage.model.CoverageResult;
import lab.square.spltcoverage.model.ProductSourceInfo;

public class SpltCoverageGenerator {
	
	public static final String SUFFIX_MERGED = "__merged__.exec";
	public static final String PRODUCT_DIRECTORY_NAME = "product";

	public void generateCoverage(IIterableSpltProvider provider) throws MalformedObjectNameException, IOException {
		int productNum = 0;
		Class[] targetClasses = provider.getTargetClasses();
		CoverageGenerator generator = new CoverageGenerator(System.out, targetClasses);
		while (provider.makeNextProduct()) {
			productNum++;
			String productDirectory;
			productDirectory = provider.getProductDirectory() + productNum;
			String pathOfFeatureSet = provider.getBaseDirectory() + productDirectory + "/featureset.txt";
			File featureSet = new File(pathOfFeatureSet);
			makeDirectory(pathOfFeatureSet);
			try {
				// write file containing the current featureSet in the product folder.
				BufferedWriter localFile = new BufferedWriter(new FileWriter(featureSet));
				localFile.write(provider.getFeatureSet().toString());
				localFile.close();
			} catch (Exception e) {
				;
			}

			JUnitCore junit = new JUnitCore();
			junit.addListener(new TestListener(provider, generator, productNum));
			org.junit.runner.Result result = junit.run(provider.getTestClasses());

			mergeExecs(provider.getBaseDirectory() + productDirectory);
		}
	}
	
	public void generateCoverage(String outputPath, ISpltProvider provider) throws MalformedObjectNameException, IOException {
		Collection<String> productPaths = new ArrayList<String>();
		
		for(int i = 0; i < provider.getNumProducts(); i++) {
			ProductSourceInfo info = provider.getProductInfo(i);
			CoverageGenerator generator = null;
			generator = new CoverageGenerator(System.out);
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
					// TODO Auto-generated method stub
					return info.classpath;
				}
			});
		}
		for(String productPath : productPaths) {
			mergeExecs(productPath);
		}
	}

	private void mergeExecs(String productDirectory) {
		File productFolder = new File(productDirectory);
		File[] testCaseExecs = new File[productFolder.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		}).length];

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
			merger.mergeExecs(new File(productFolder, productFolder.getName() + SUFFIX_MERGED), testCaseExecs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void makeDirectory(String directory) {
		String[] splitted = directory.split("/");
		String checkDirectory = "";
		for (int i = 0; i < splitted.length - 1; i++) {
			checkDirectory = checkDirectory + splitted[i] + "/";
			File file = new File(checkDirectory);
			if (!file.exists())
				file.mkdir();
		}
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
			System.out.println(description.getTestClass().getSimpleName());
			System.out.println(description.getMethodName());
			System.out.println("//==============finished===========//");
			String testCaseDirectory;
			String testMethodDirectory;
			testCaseDirectory = provider.getTestCaseDirectory() + description.getTestClass().getSimpleName();
			testMethodDirectory = provider.getTestMethodDirectory() + description.getMethodName();
			String directory = provider.getBaseDirectory() + provider.getProductDirectory() + productNumber + testCaseDirectory
					+ testMethodDirectory;
			CoverageResult result = generator.analyze();

			CoverageWriter.makeExecFile(directory, result.getProxy().getExecutionData(false));

			generator.resetData();
		}

		@Override
		public void testFailure(Failure failure) throws Exception {
			System.out.println(failure.getTestHeader());
			System.out.println(failure.getTrace());
			System.out.println(failure.getMessage());
		}
	}
}
