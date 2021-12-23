package lab.square.spltcoverage.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.SessionInfoStore;
import org.jacoco.core.tools.ExecFileLoader;

import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.ProductCoverageManager;
import lab.square.spltcoverage.model.TestCaseCoverage;
import lab.square.spltcoverage.model.TestMethodCoverage;

/**
 * The CoverageReader class is the class reading the product coverages.
 * @author SQUARELAB
 *
 */
public class CoverageReader {

	public final static String FEATURESET_FILENAME = "featureset.txt";

	private String execFilesPath;
	private String classPath;
	private ProductCoverageManager manager;

	public CoverageReader(ProductCoverageManager manager, String classPath) {
		this.manager = manager;
		this.classPath = classPath;
	}
	
	/**
	 * 
	 * @param manager
	 * @param execFilesPath
	 * @param classPath
	 */
	public CoverageReader(ProductCoverageManager manager, String execFilesPath, String classPath) {
		this(manager, classPath);
		this.execFilesPath = execFilesPath;
	}

	public ProductCoverageManager getManager() {
		return manager;
	}
	
	public void setExecFilesPath(String path) {
		this.execFilesPath = path;
	}

	public void read() throws IOException {

		File folder = new File(execFilesPath);
		if (!folder.exists())
			return;

		File[] productFolders = folder.listFiles();

		for (File productFolder : productFolders) {
			final String productName = productFolder.getName();

			File[] testCaseFolders = productFolder.listFiles();

			ProductCoverage productCoverage = null;

			// find feature set and init productCoverage.
			Map<String, Boolean> featureSet = findFeatureSet(testCaseFolders);
			if (featureSet == null)
				continue;
			productCoverage = new ProductCoverage(featureSet);

			// collect execution data.
			for (File testCaseFolder : testCaseFolders) {
				final String testCaseName = testCaseFolder.getName();

				TestCaseCoverage testCaseCoverage = new TestCaseCoverage(testCaseName);

				// the file is not a directory.
				if (!testCaseFolder.isDirectory()) {
					if (testCaseFolder.getName().endsWith("Merged.exec")) {
						productCoverage.addClassCoverages(load(testCaseFolder));
					}
					continue;
				}

				// load test method coverages.
				File[] testMethodCoverages = testCaseFolder.listFiles();
				for (File testMethodCoverageFile : testMethodCoverages) {
					final String testMethodName = testMethodCoverageFile.getName().replaceAll(".exec", "");

					if (testMethodName.endsWith("Merged")) {
						// TODO: find the way to separate between the merged *.exec file or not, more
						// reasonably.
						testCaseCoverage.addClassCoverages(load(testMethodCoverageFile));
						continue;
					}

					TestMethodCoverage testMethodCoverage = new TestMethodCoverage(testMethodName,
							load(testMethodCoverageFile));
					testCaseCoverage.addTestMethodCoverage(testMethodCoverage);
				}

				productCoverage.addTestCaseCoverage(testCaseCoverage);
			}
			manager.addProductCoverage(productCoverage);
		}
	}

	private Collection<IClassCoverage> load(File testMethodCoverageFile) throws IOException {
		ExecFileLoader execFileLoader = new ExecFileLoader();

		execFileLoader.load(testMethodCoverageFile);

		final ExecutionDataStore execStore = execFileLoader.getExecutionDataStore();
		final SessionInfoStore sessionStore = execFileLoader.getSessionInfoStore();

		final CoverageBuilder coverageBuilder = new CoverageBuilder();
		final Analyzer analyzer = new Analyzer(execStore, coverageBuilder);

		analyzer.analyzeAll(new File(classPath));

		return new HashSet<IClassCoverage>(coverageBuilder.getClasses());
	}

	private Map<String, Boolean> findFeatureSet(File[] testCaseFolders) throws FileNotFoundException, IOException {
		Map<String, Boolean> featureSet = new HashMap<String, Boolean>();

		for (File isFeatureSet : testCaseFolders) {
			final String testCaseName = isFeatureSet.getName();
			if (isFeatureSet.getName().equalsIgnoreCase(FEATURESET_FILENAME)) {

				FileReader fr = new FileReader(isFeatureSet);
				BufferedReader br = new BufferedReader(fr);
				String given = br.readLine();
				given = given.replace("{", "");
				given = given.replace("}", "");
				String[] pairs = given.split(",");

				for (String pair : pairs) {
					String[] splitted = pair.split("=");
					String key = splitted[0];
					String value = splitted[1];

					featureSet.put(key, value.equals("true") ? true : false);
				}
			}
		}
		return featureSet;
	}

	public static void main(String[] args) throws IOException {
		String directory = "D:/directorypath/bankaccount/";
		String classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/bankaccount/bin/bankaccount";
		Class[] targetClasses = loadClasses(
				"D:/workspacechallenege/challenge-master/workspace_IncLing/bankaccount/bin/", "bankaccount.Account",
				"bankaccount.Application", "bankaccount.LogEntry", "bankaccount.Transaction");
		ExecFileLoader execFileLoader = new ExecFileLoader();

		File folder = new File(directory);

		if (!folder.exists())
			return;

		ProductCoverageManager manager = new ProductCoverageManager();

		File[] productFolders = folder.listFiles();

		for (File productFolder : productFolders) {
			final String productName = productFolder.getName();

			File[] testCaseFolders = productFolder.listFiles();

			ProductCoverage productCoverage = null;

			// find feature set and init productCoverage.
			for (File isFeatureSet : testCaseFolders) {
				final String testCaseName = isFeatureSet.getName();
				if (isFeatureSet.getName().equalsIgnoreCase("featureset.txt")) {

					FileReader fr = new FileReader(isFeatureSet);
					BufferedReader br = new BufferedReader(fr);
					String given = br.readLine();
					given = given.replace("{", "");
					given = given.replace("}", "");
					String[] pairs = given.split(",");

					Map<String, Boolean> featureSet = new HashMap<String, Boolean>();

					for (String pair : pairs) {
						String[] splitted = pair.split("=");
						String key = splitted[0];
						String value = splitted[1];

						featureSet.put(key, value.equals("true") ? true : false);
					}
					productCoverage = new ProductCoverage(featureSet);
				}
			}

			if (productCoverage == null)
				continue;

			// collect execution data.
			for (File testCaseFolder : testCaseFolders) {
				final String testCaseName = testCaseFolder.getName();

				TestCaseCoverage testCaseCoverage = new TestCaseCoverage(testCaseName);

				// the file is not a directory.
				if (!testCaseFolder.isDirectory())
					continue;

				// load test method coverages.
				File[] testMethodCoverages = testCaseFolder.listFiles();
				for (File testMethodCoverageFile : testMethodCoverages) {
					final String testMethodName = testMethodCoverageFile.getName().replaceAll(".exec", "");

					if (testMethodName.contains("Merged")) {
						// TODO: find the way to separate between the merged *.exec file and not.
					}

					execFileLoader.load(testMethodCoverageFile);

					final ExecutionDataStore execStore = execFileLoader.getExecutionDataStore();
					final SessionInfoStore sessionStore = execFileLoader.getSessionInfoStore();

					final CoverageBuilder coverageBuilder = new CoverageBuilder();
					final Analyzer analyzer = new Analyzer(execStore, coverageBuilder);

					analyzer.analyzeAll(new File(classDirectory));

					TestMethodCoverage testMethodCoverage = new TestMethodCoverage(testMethodName,
							new HashSet<IClassCoverage>(coverageBuilder.getClasses()));
					testCaseCoverage.addTestMethodCoverage(testMethodCoverage);
				}

				productCoverage.addTestCaseCoverage(testCaseCoverage);
			}
			manager.addProductCoverage(productCoverage);
		}

		List<ProductCoverage> products = manager.getProductCoverages();
		for (ProductCoverage product : products) {
			System.out.println("Feature Set: " + product.getFeatureSet().toString());
			Collection<TestCaseCoverage> cases = product.getTestCaseCoverages();
			for (TestCaseCoverage testCase : cases) {
				System.out.println("Test Method Name: " + testCase.getTestCaseName());
				Collection<TestMethodCoverage> methods = testCase.getTestMethodCoverages();
				for (TestMethodCoverage method : methods) {
					System.out.println(method.getMethodName());
				}
			}
		}

		for (ProductCoverage product : products) {
			for (ProductCoverage product2 : products) {
				String[] classNames = new String[targetClasses.length];
				int i = 0;
				for(Class klass : targetClasses) {
					classNames[i++] = klass.getCanonicalName().replace(".", "/");
				}
				
				if (product.equals(product2, classNames)) {
					if (!product.getFeatureSet().equals(product2.getFeatureSet())) {
						System.out.println(product.getFeatureSet() + "\n" + product2.getFeatureSet());
						System.out.println("===================");
					}
				}
			}
		}
	}

	private static Class[] loadClasses(String classDirectory, String... classNames) {
		File directory = new File(classDirectory);
		Class[] toReturn = new Class[classNames.length];

		for (int i = 0; i < classNames.length; i++) {
			URLClassLoader loader = null;
			try {
				loader = new URLClassLoader(new URL[] { new URL("file://" + classDirectory) });
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				toReturn[i] = loader.loadClass(classNames[i]);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return toReturn;
	}
}
