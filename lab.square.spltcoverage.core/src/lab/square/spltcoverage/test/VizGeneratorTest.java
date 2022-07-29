package lab.square.spltcoverage.test;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.junit.Test;

import lab.square.spltcoverage.core.analysis.ProductLinker;
import lab.square.spltcoverage.io.FeatureSetGroupReader;
import lab.square.spltcoverage.io.SplCoverageReader;
import lab.square.spltcoverage.model.SplCoverage;
import lab.square.spltcoverage.model.ProductNode;
import lab.square.spltcoverage.report.GraphVizGenerator;

public class VizGeneratorTest {

	// @Test
	public void testGeneratorChess() {
		String directory;
		String classDirectory;
		directory = "D:/directorypath/chess/";
		classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/chess/bin";

		generateViz(directory, classDirectory);
	}

	// @Test
	public void testGeneratorElevator() {
		String directory;
		String classDirectory;
		directory = "D:/directorypath/elevator/";
		classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/elevator/bin";

		generateViz(directory, classDirectory);
	}

	//@Test
	public void testGeneratorElevator2() {
		String directory;
		directory = "D:\\workspacechallenege\\challenge-master\\workspace_IncLing\\Tools\\All_valid_conf\\elevator\\products";

		generateVizWithOnlyFeatureSets(directory);
	}

	// @Test
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
		Collection<Map<String, Boolean>> featureSets = reader.readAll();

		Collection<ProductNode> heads = ProductLinker.link(featureSets);

		try {
			GraphVizGenerator.generate(heads, GraphVizGenerator.CONFIG_SHOWPROBLEM_LEFTTORIGHT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGeneratorMinepump() {
		String directory;
		String classDirectory;
		directory = "D:/directorypath/minepump/";
		classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/minepump/bin";

		generateViz(directory, classDirectory);
	}
	
	//@Test
	public void testGeneratorMinepump2() {
		String directory;
		directory = "D:\\workspacechallenege\\challenge-master\\workspace_IncLing\\Tools\\All_valid_conf\\MinePump\\products";

		generateVizWithOnlyFeatureSets(directory);
	}

	// @Test
	public void testGeneratorGpl() {
		String directory;
		String classDirectory;
		directory = "D:/directorypath/gpl/";
		classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/gpl/bin";

		generateViz(directory, classDirectory);
	}

	// @Test
	public void testGeneratorVendingMachine() {
		String directory;
		String classDirectory;
		directory = "D:/directorypath/vendingmachine/";
		classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/vending/bin";

		generateViz(directory, classDirectory);
	}

	private Collection<ProductNode> createLinker(String directory, String classDirectory) {
		String[] folders = directory.split("/");
		SplCoverage manager = new SplCoverage(folders[folders.length - 1]);
		SplCoverageReader reader = new SplCoverageReader(manager, directory, classDirectory);
		try {
			reader.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Collection<ProductNode> heads = ProductLinker.link(manager);
		if (heads.isEmpty())
			fail();

		return heads;
	}
}
