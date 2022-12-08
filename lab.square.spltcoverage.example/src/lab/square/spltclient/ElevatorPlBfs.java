package lab.square.spltclient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import lab.square.spltcoverage.core.analysis.ProductLinker;
import lab.square.spltcoverage.core.antenna.AntennaCoverageAccumulator;
import lab.square.spltcoverage.core.launch.SplCoverageGeneratorLauncher;
import lab.square.spltcoverage.io.AbstractSplCoverageReader;
import lab.square.spltcoverage.io.FeatureIdeConfigReader;
import lab.square.spltcoverage.io.SplCoverageReaderFactory;
import lab.square.spltcoverage.io.antenna.AntennaSplCoverageReader;
import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.model.ISplCoverageVisitor;
import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.ProductNode;
import lab.square.spltcoverage.model.ProductSourceInfo;
import lab.square.spltcoverage.model.SplCoverage;
import lab.square.spltcoverage.model.TestCaseCoverage;
import lab.square.spltcoverage.model.TestMethodCoverage;
import lab.square.spltcoverage.model.antenna.AntennaProductCoverage;
import lab.square.spltcoverage.report.GraphVizGenerator;
import lab.square.spltcoverage.utils.Tools;

public class ElevatorPlBfs {
	private static final String PL_BASE_DIRECTORY = "D:/workspace-featureide";
	private static final String PRODUCT_SUFFIX = "Elevator-Antenna-v1.2-000";
	private static final String CLASSPATH_ROOT = "bin";
	private static final String DEPENDENCIES_ROOT = "lib/*";
	private static final String TEST_CLASSPATH_1 = "de/ovgu/featureide/examples/elevator/test/TestElevator.class";
	private static final String TEST_CLASSPATH_2 = "de/ovgu/featureide/examples/elevator/test/TestFloorChooseDialog.class";
	private static final String OUTPUT_PATH = "D:/directorypath/elevator-allproducts";
	
	private static String[] productDirectories;
	private static String[] productSourcePaths;
	private static String[] outputDirectories;
	private static Collection<FeatureSet> featureSets;
	private static List<ProductSourceInfo> info;
	private static int NUM_PRODUCTS = 16;

	private static Collection<ProductNode> roots;
	
	private static SplCoverage splCoverage;
	private static Set<FeatureSet> featureSetVisited;
	private static Set<String> featureVisited;
	private static Queue<ProductNode> q;
	
	
	public static void main(String[] args) {
		setUp();
		//generateCoverages();
		//readSplCoverage();		
		//generateProductGraph();
		//GenerateProductHierarchyFigure();
		//accumulateAndPrintResults();
	}

	private static void accumulateAndPrintResults() {
		AntennaCoverageAccumulator accumulator = new AntennaCoverageAccumulator();
		
		q = new LinkedList<>();
		featureSetVisited = new HashSet<>();
		
		for(ProductNode root : roots) {
			q.add(root);
			featureSetVisited.add(root.getFeatureSet());
		}
		
		while(!q.isEmpty()) {			
			ProductNode node = q.poll();
			featureSetVisited.add(node.getFeatureSet());
			
			accumulator.accumulate((AntennaProductCoverage)node.getProductCoverage());
			System.out.println("===========visited!===========");
			System.out.println("Product Name: " + node.getProductCoverage().getName());
			System.out.println("Feature Set: " + node.getFeatureSet().toString());
			System.out.println("Faeture Set(Simplified): " + Arrays.toString(node.getFeatureSet().getFeatures().toArray()));
			System.out.println();
			System.out.println("New feature added: " + Arrays.toString(accumulator.getNewlyVisitedFeatures().toArray()));
			System.out.println("is coverage changed?: " + accumulator.isCoverageChanged());
			System.out.println();
			
			for(String className : accumulator.getNewlyActivatedClasses()) {
				System.out.println("\tclass: " + className);
				System.out.println("\tnewly activated: " + accumulator.getNewlyActivatedLineCountOfClass(className));
				System.out.println("\tnewly covered: " + accumulator.getNewlyCoveredLineCountOfClass(className));
				
				System.out.println("\tnewly covered line numbers: " + accumulator.getNewlyCoveredLineNumbersOfClass(className));
				
				int accumulatedActivated = accumulator.getTotalActivatedLineCountOfClass(className);
				int accumulatedCovered = accumulator.getTotalCoveredLineCountOfClass(className);				
				double accumulatedRatio = (double)accumulatedCovered / (double)accumulatedActivated * 100;
				String formattedRatio = String.format("%.2f", accumulatedRatio);
				
				System.out.println("\taccumulated line coverage: " + accumulatedCovered + "/" + accumulatedActivated + "(" + formattedRatio +"%)");
				System.out.println();
			}
			System.out.println();
			
			int totalActivated = accumulator.getTotalActivatedLine();
			int totalCovered = accumulator.getTotalCoveredLine();
			double totalRatio = (double)totalCovered / (double)totalActivated * 100;
			String formattedRatio = String.format("%.2f", totalRatio);
			System.out.println("total line coverage: " + totalCovered + "/" + totalActivated + "(" + formattedRatio + "%)");
			
			for(ProductNode child : node.getChildren()) {
				q.add(child);
			}
		}
	}

	private static void setUp() {
		setUpDirectories();
		setUpProductSourceInfo();
		setUpFeatureSetsFromProductSourceInfo();
	}

	private static void generateProductGraph() {
		roots = ProductLinker.link(featureSets);
		
		
		for(ProductNode root : roots) {
			root.fillCoverageRecursivelyIfEmpty(splCoverage);
		}
	}

	private static SplCoverage readSplCoverage() {
		splCoverage = null;
		String[] classpaths = makeClasspathsArray();
		
		for(ProductSourceInfo each : info) {
			System.out.println(each.featureSet);
		}
		
		AbstractSplCoverageReader reader = 
				SplCoverageReaderFactory
				.createAntennaSplCoverageReader(classpaths, productSourcePaths);
		
		splCoverage = reader.readSplCoverage(OUTPUT_PATH);
		return splCoverage;
	}

	private static void GenerateProductHierarchyFigure() {
		try {
			GraphVizGenerator.generate(roots, GraphVizGenerator.CONFIG_SHOWPROBLEM_LEFTTORIGHT, OUTPUT_PATH + "/graph.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void setUpFeatureSetsFromProductSourceInfo() {
		featureSets = new HashSet<>();
		
		for(ProductSourceInfo each : info) {
			featureSets.add(each.featureSet);
		}
	}

	private static void printEachScore(SplCoverage splCoverage) {
		splCoverage.accept(new ISplCoverageVisitor() {

			@Override
			public void visit(SplCoverage pcm) {
				for(ProductCoverage pc : pcm.getProductCoverages())
					this.visit(pc);
			}

			@Override
			public void visit(ProductCoverage pc) {
				System.out.println(pc.getFeatureSet());
				System.out.println(pc.getScore());
			}

			@Override
			public void visit(TestCaseCoverage tcc) {
				
			}

			@Override
			public void visit(TestMethodCoverage tmc) {
				
			}
			
		});
	}
	
	private static String[] makeClasspathsArray() {
		String[] classpaths = new String[info.size()];
		
		for(int i = 0; i < info.size(); i++) {
			classpaths[i] = info.get(i).classpath;
		}
		
		return classpaths;
	}

	private static void setUpDirectories() {
		productDirectories = new String[NUM_PRODUCTS];
		outputDirectories = new String[NUM_PRODUCTS];
		
		for(int i = 0; i < Math.min(9, NUM_PRODUCTS); i++) {
			productDirectories[i] = PL_BASE_DIRECTORY + "/" + PRODUCT_SUFFIX + "0" + (i + 1);
			outputDirectories[i] = OUTPUT_PATH + "/product" + (i + 1);
		}
		
		for(int i = 9; i < NUM_PRODUCTS; i++) {
			productDirectories[i] = PL_BASE_DIRECTORY + "/" + PRODUCT_SUFFIX + (i + 1);
			outputDirectories[i] = OUTPUT_PATH + "/product" + (i + 1);
		}

		productSourcePaths = new String[NUM_PRODUCTS];
		for(int i = 0; i < NUM_PRODUCTS; i++) {
			productSourcePaths[i] = productDirectories[i] + "/src";
		}
	}
	
	private static void generateCoverages() {
		try {
			Tools.deleteDirectoryRecursively(new File(OUTPUT_PATH));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		SplCoverageGeneratorLauncher.launch(OUTPUT_PATH, info);
	}

	private static void setUpProductSourceInfo() {
		info = new ArrayList<>();
		
		int productCount = 1;
		
		for(String productDirectory : productDirectories) {
			String classpath = productDirectory + "/" + CLASSPATH_ROOT;
			String testPath1 = classpath + "/" + TEST_CLASSPATH_1;
			String testPath2 = classpath + "/" + TEST_CLASSPATH_2;
			String additionalDependencies = productDirectory + "/" + DEPENDENCIES_ROOT;
			FeatureSet featureSet = findFeatureSet(productDirectory, productCount++);
			
			info.add(new ProductSourceInfo(classpath, Arrays.asList(testPath2, testPath1), featureSet, Arrays.asList(additionalDependencies)));
		}
	}

	private static FeatureSet findFeatureSet(String productDirectory, int productCount) {
		String xmlName;
		if(productCount < 10)
			xmlName = "0" + productCount;
		else
			xmlName = String.valueOf(productCount);
		String configDirectory = productDirectory + "/configs/" + xmlName + ".xml";
		
		return FeatureIdeConfigReader.readFeatureSet(configDirectory);
	}
}
