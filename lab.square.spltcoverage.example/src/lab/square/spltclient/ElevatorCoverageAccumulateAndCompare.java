package lab.square.spltclient;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICounter;

import lab.square.spltcoverage.core.launch.CoverageGeneratorLauncher;
import lab.square.spltcoverage.io.CoverageReader;
import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.antenna.AntennaSourceFile;

public class ElevatorCoverageAccumulateAndCompare {

	private static final String[] PROJECT_PATHS = { "D:/workspace-featureide/elevator-Antenna-v1.2-SabOnly/",
			"D:/workspace-featureide/elevator-Antenna-v1.2-ShortestPath/",
			"D:/workspace-featureide/elevator-Antenna-v1.2-UndirectedCall/",
			"D:/workspace-featureide/elevator-Antenna-v1.2-Both/",
			"D:/workspace-featureide/elevator-Antenna-v1.2-FpOnly/"};

	private static String[] CLASSPATHS;
	private static String[] OUTPUT_PATHS;
	private static String[] SRC_PATHS;

	private static final String[] TARGET_SRC_PATHS = {
			"src/de/ovgu/featureide/examples/elevator/core/controller/ControlUnit.java",
			"src/de/ovgu/featureide/examples/elevator/core/controller/Request.java" };

	private static final String[] TESTS_PATH = new String[] {
			"de\\ovgu\\featureide\\examples\\elevator\\test\\TestElevator.class" };

	private static final String OUTPUT_PATH = "D:/directorypath/ElevatorCoverageAccumulateAndCompare/";
	private static final String SOURCE_CLASSPATH_1 = "D:\\workspace-featureide\\elevator-Antenna-v1.2-Both\\src\\";

	public static void main(String[] args) {

		initClasspaths();
		initOutputPaths();
		initSrcPaths();
		generateCoverage();

		int numOfProducts = PROJECT_PATHS.length;
		int numOfTargets = TARGET_SRC_PATHS.length;
		AntennaSourceFile[][] antennaSources = new AntennaSourceFile[numOfProducts][];

		for (int i = 0; i < numOfProducts; i++) {
			antennaSources[i] = new AntennaSourceFile[numOfTargets];
			for (int j = 0; j < numOfTargets; j++) {
				antennaSources[i][j] = new AntennaSourceFile(PROJECT_PATHS[i] + TARGET_SRC_PATHS[j]);
			}
		}

		ProductCoverage[] coverages = new ProductCoverage[numOfProducts];

		for (int i = 0; i < numOfProducts; i++) {
			try {
				coverages[i] = CoverageReader.read(OUTPUT_PATHS[i], CLASSPATHS[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		AccumulatingResult result = new AccumulatingResult(coverages, antennaSources);

		System.out.println("===PRODUCT INFO===");
		System.out.println("Product 0: {Sabbath}");
		System.out.println("Product 1: {ShortestPath, DirectedCall}");
		System.out.println("Product 2: {FIFO, UndirectedCall}");
		System.out.println("Product 3: {ShortestPath, UndirectedCall}");
		System.out.println("Product 4: {ShortestPath, UndirectedCall, FloorPermission");
		System.out.println();

		System.out.println("line | isActivated |   isCovered   | content");


		for (int i = 0; i < TARGET_SRC_PATHS.length; i++) {
			int lineCount = 1;
			try (BufferedReader reader = new BufferedReader(new FileReader(PROJECT_PATHS[0] + TARGET_SRC_PATHS[i]))) {
				String line;
				while ((line = reader.readLine()) != null) {
					System.out.print(String.format("%3d ", lineCount));
					System.out.print(String.format("%15s",
							result.getIndicesOfProductActivatedLineAt(antennaSources[0][i].getFileName(), lineCount)));
					System.out.print(String.format("%15s",
							result.getIndicesOfProductCoveredLineAt(antennaSources[0][i].getFileName(), lineCount)));
					System.out.println("  " + line);
					lineCount++;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

//		System.out.println("==Activated line numbers in ControlUnit.java of {Sabbath}==");
//		printActivatedLineNumbers(source1);
//		
//		AntennaSourceFile common;
//		AntennaSourceFile diff;
//		AntennaSourceFile acc;
//
//		System.out.println("\n\n==Activated line numbers in ControlUnit.java of {ShortestPath, DirectedCall}==");
//		printActivatedLineNumbers(source2);
//		
//		System.out.println("\n\n==Common activated line numbers in ControlUnit.java between {Sabbath} and {ShortestPath, DirectedCall}==");
//		common = source1.intersect(source2);
//		printActivatedLineNumbers(common);
//		
//		System.out.println("\n\n==Added line numbers in ControlUnit.java from {Sabbath} to {ShortestPath, DirectedCall}==");
//		acc = source1.add(source2);
//		diff = acc.subtract(source1);
//		printActivatedLineNumbers(diff);
//		

	}

	private static void initSrcPaths() {
		SRC_PATHS = new String[PROJECT_PATHS.length];
		for (int i = 0; i < PROJECT_PATHS.length; i++) {
			SRC_PATHS[i] = PROJECT_PATHS[i] + "src/";
		}
	}

	private static void initOutputPaths() {
		OUTPUT_PATHS = new String[PROJECT_PATHS.length];
		for (int i = 0; i < PROJECT_PATHS.length; i++) {
			OUTPUT_PATHS[i] = OUTPUT_PATH + "product" + i + "/";
		}
	}

	private static void initClasspaths() {
		CLASSPATHS = new String[PROJECT_PATHS.length];
		for (int i = 0; i < PROJECT_PATHS.length; i++) {
			CLASSPATHS[i] = PROJECT_PATHS[i] + "bin/";
		}
	}

	private static void generateCoverage() {
		for (int i = 0; i < PROJECT_PATHS.length; i++) {
			CoverageGeneratorLauncher.launch(CLASSPATHS[i], Arrays.asList(TESTS_PATH), OUTPUT_PATHS[i]);
		}

	}

	private static void printActivatedLineNumbers(AntennaSourceFile source) {
		int count = 1;

		for (int i = 1; i <= source.getNumberOfLine(); i++) {
			if (source.isActivatedAt(i)) {
				System.out.print(String.format("%3d, ", i));
				count++;
			}

			if (count % 20 == 0) {
				count++;
				System.out.println();
			}
		}
	}

	private static void printIsActivatedOrNotWithMessageLineByLine(AntennaSourceFile source, String message) {
		for (int i = 1; i <= source.getNumberOfLine(); i++) {
			System.out.print(String.format("%3d  ", i));
			if (source.isActivatedAt(i))
				System.out.print(message);
			System.out.println();
		}
	}

	public static class AccumulatingResult {
		ProductCoverage[] coverages;
		AntennaSourceFile[][] sources;
		Map<String, boolean[]>[] coveredLines;
		Map<String, boolean[]>[] activatedLines;

		public AccumulatingResult(ProductCoverage[] coverages, AntennaSourceFile[][] sources) {
			this.coverages = coverages;
			this.sources = sources;
			coveredLines = new HashMap[coverages.length];
			activatedLines = new HashMap[sources.length];

			for (int i = 0; i < coverages.length; i++) {
				coveredLines[i] = new HashMap<>();

				Collection<IClassCoverage> classCoverages = coverages[i].getClassCoverages();
				for (IClassCoverage each : classCoverages) {
					if (each.getFirstLine() == -1)
						continue;

					boolean[] isCovered = new boolean[each.getLastLine() + 100];

					for (int j = each.getFirstLine(); j <= each.getLastLine(); j++) {
						isCovered[j] = each.getLine(j).getStatus() == ICounter.FULLY_COVERED;
					}
					coveredLines[i].put(each.getSourceFileName(), isCovered);
				}
			}
		}

		public Set<Integer> getIndicesOfProductActivatedLineAt(String className, int lineNumber) {
			Set<Integer> result = new HashSet<>();

			for (int i = 0; i < sources.length; i++) {
				for (int j = 0; j < sources[i].length; j++) {
					if (!sources[i][j].getFileName().equals(className))
						continue;
					if (sources[i][j].isActivatedAt(lineNumber))
						result.add(i);
				}
			}

			return result;
		}

		public Set<Integer> getIndicesOfProductCoveredLineAt(String className, int lineNumber) {
			Set<Integer> result = new HashSet<>();

			for (int i = 0; i < sources.length; i++) {
				boolean[] covered = coveredLines[i].get(className);

				try {
					if (covered[lineNumber]) {
						result.add(i);
					}
				} catch (Exception e) {
					;
				}
			}

			return result;
		}
	}

}
