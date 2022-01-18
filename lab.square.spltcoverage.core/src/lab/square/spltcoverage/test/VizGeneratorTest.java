package lab.square.spltcoverage.test;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collection;

import org.junit.Test;

import lab.square.spltcoverage.core.analysis.ProductLinker;
import lab.square.spltcoverage.io.SpltCoverageReader;
import lab.square.spltcoverage.model.ProductCoverageManager;
import lab.square.spltcoverage.model.ProductGraph;
import lab.square.spltcoverage.report.GraphVizGenerator;

public class VizGeneratorTest {
	
	@Test
	public void testGeneratorChess() {
		String directory;
		String classDirectory;
		directory = "D:/directorypath/chess/";
		classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/chess/bin";
		
		generateViz(directory, classDirectory);
	}
	
	//@Test
	public void testGeneratorElevator() {
		String directory;
		String classDirectory;
		directory = "D:/directorypath/elevator/";
		classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/elevator/bin";
		
		generateViz(directory, classDirectory);
	}

	
	//@Test
	public void testGeneratorSudoku() {
		String directory;
		String classDirectory;
		directory = "D:/directorypath/sudoku/";
		classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/sudoku/bin";
		
		generateViz(directory, classDirectory);
	}

	private void generateViz(String directory, String classDirectory) {
		Collection<ProductGraph> heads = createLinker(directory, classDirectory);
		
		try {
			GraphVizGenerator.generate(heads, GraphVizGenerator.CONFIG_LIGHT_LEFTTORIGHT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void testGeneratorMinepump() {
		String directory;
		String classDirectory;
		directory = "D:/directorypath/minepump/";
		classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/minepump/bin";
		
		generateViz(directory, classDirectory);
	}
	
	//@Test
	public void testGeneratorGpl() {
		String directory;
		String classDirectory;
		directory = "D:/directorypath/gpl/";
		classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/gpl/bin";
		
		generateViz(directory, classDirectory);
	}

	private Collection<ProductGraph> createLinker(String directory, String classDirectory) {
		ProductCoverageManager manager = new ProductCoverageManager();
		SpltCoverageReader reader = new SpltCoverageReader(manager, directory, classDirectory);
		try {
			reader.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Collection<ProductGraph> heads = ProductLinker.link(manager);
		if (heads.isEmpty())
			fail();
		
		return heads;
	}
}
