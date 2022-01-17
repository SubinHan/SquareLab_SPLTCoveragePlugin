package lab.square.spltcoverage.core.analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.management.MalformedObjectNameException;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import lab.square.spltcoverage.io.CoverageWriter;
import lab.square.spltcoverage.model.CoverageResult;

public class SpltCoverageGenerator {
	
	public static final String PREFIX_MERGED = "__merged__.exec";

	public void generateCoverage(ISpltCoverageRunner runner) throws MalformedObjectNameException, IOException {
		int productNum = 0;
		Class[] targetClasses = runner.getTargetClasses();
		CoverageGenerator generator = new CoverageGenerator(System.out, targetClasses);
		while (runner.makeNextProduct()) {
			productNum++;
			String productDirectory;
			productDirectory = runner.getProductDirectory() + productNum;
			String pathOfFeatureSet = runner.getBaseDirectory() + productDirectory + "/featureset.txt";
			File featureSet = new File(pathOfFeatureSet);
			makeDirectory(pathOfFeatureSet);
			try {
				// write file containing the current featureSet in the product folder.
				BufferedWriter localFile = new BufferedWriter(new FileWriter(featureSet));
				localFile.write(runner.getFeatureSet().toString());
				localFile.close();
			} catch (Exception e) {
				;
			}

			JUnitCore junit = new JUnitCore();
			junit.addListener(new TestListener(runner, generator, productNum));
			org.junit.runner.Result result = junit.run(runner.getTestClasses());

			File productFolder = new File(runner.getBaseDirectory() + productDirectory);
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
				File testCaseExec = new File(testCaseFolder, testCaseFolder.getName() + PREFIX_MERGED);
				try {
					merger.mergeExecs(testCaseExec, testCaseFolder.listFiles());
				} catch (IOException e) {
					e.printStackTrace();
				}
				testCaseExecs[index++] = testCaseExec;
			}
			try {
				merger.mergeExecs(new File(productFolder, productFolder.getName() + PREFIX_MERGED), testCaseExecs);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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
		ISpltCoverageRunner runner;
		CoverageGenerator generator;
		int productNumber;

		public TestListener(ISpltCoverageRunner runner, CoverageGenerator generator, int productNumber) {
			this.runner = runner;
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
			testCaseDirectory = runner.getTestCaseDirectory() + description.getTestClass().getSimpleName();
			testMethodDirectory = runner.getTestMethodDirectory() + description.getMethodName();
			String directory = runner.getBaseDirectory() + runner.getProductDirectory() + productNumber + testCaseDirectory
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
