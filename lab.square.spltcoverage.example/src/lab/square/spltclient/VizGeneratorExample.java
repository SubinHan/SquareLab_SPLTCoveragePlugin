package lab.square.spltclient;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collection;

import org.junit.Test;

import lab.square.spltcoverage.core.analysis.ProductLinker;
import lab.square.spltcoverage.io.AbstractSplCoverageReader;
import lab.square.spltcoverage.io.FeatureSetGroupReader;
import lab.square.spltcoverage.io.SplCoverageReader;
import lab.square.spltcoverage.io.SplCoverageReaderFactory;
import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.model.ProductNode;
import lab.square.spltcoverage.model.SplCoverage;
import lab.square.spltcoverage.report.GraphVizGenerator;

public class VizGeneratorExample {
	
	public static void main(String[] args) {
		// testSomething();
	}
	
	public void testGeneratorChess() {
		String directory;
		String classDirectory;
		directory = "D:/directorypath/chess/";
		classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/chess/bin";

		generateViz(directory, classDirectory);
	}

	public void testGeneratorElevator() {
		String directory;
		String classDirectory;
		directory = "D:/directorypath/elevator/";
		classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/elevator/bin";

		generateViz(directory, classDirectory);
	}

	public void testGeneratorElevator2() {
		String directory;
		directory = "D:\\workspacechallenege\\challenge-master\\workspace_IncLing\\Tools\\All_valid_conf\\elevator\\products";

		generateVizWithOnlyFeatureSets(directory);
	}

	public void testGeneratorSudoku() {
		String directory;
		String classDirectory;
		directory = "D:/directorypath/sudoku/";
		classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/sudoku/bin";

		generateViz(directory, classDirectory);
	}

	private void generateViz(String directory, String classDirectory) {
		Collection<ProductNode> heads = createLinker(directory, classDirectory);

		try {
			GraphVizGenerator.generate(heads, GraphVizGenerator.CONFIG_SHOWPROBLEM_LEFTTORIGHT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void generateVizWithOnlyFeatureSets(String directory) {
		FeatureSetGroupReader reader = new FeatureSetGroupReader(directory);
		Collection<FeatureSet> featureSets = reader.readAll();

		Collection<ProductNode> heads = ProductLinker.link(featureSets);

		try {
			GraphVizGenerator.generate(heads, GraphVizGenerator.CONFIG_SHOWPROBLEM_LEFTTORIGHT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testGeneratorMinepump() {
		String directory;
		String classDirectory;
		directory = "D:/directorypath/minepump/";
		classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/minepump/bin";

		generateViz(directory, classDirectory);
	}

	public void testGeneratorMinepump2() {
		String directory;
		directory = "D:\\workspacechallenege\\challenge-master\\workspace_IncLing\\Tools\\All_valid_conf\\MinePump\\products";

		generateVizWithOnlyFeatureSets(directory);
	}

	public void testGeneratorGpl() {
		String directory;
		String classDirectory;
		directory = "D:/directorypath/gpl/";
		classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/gpl/bin";

		generateViz(directory, classDirectory);
	}

	public void testGeneratorVendingMachine() {
		String directory;
		String classDirectory;
		directory = "D:/directorypath/vendingmachine/";
		classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/vending/bin";

		generateViz(directory, classDirectory);
	}

	private Collection<ProductNode> createLinker(String directory, String classDirectory) {
		String[] folders = directory.split("/");
		AbstractSplCoverageReader reader = 
				SplCoverageReaderFactory.createInvariableSplCoverageReader(classDirectory);
		SplCoverage manager = new SplCoverage(folders[folders.length - 1]);
		reader.readSplCoverage(directory);
		Collection<ProductNode> heads = ProductLinker.link(manager);
		if (heads.isEmpty())
			fail();

		return heads;
	}
}
