package lab.square.spltcoverage.test;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collection;

import org.junit.Test;

import lab.square.spltcoverage.core.analysis.ProductLinker;
import lab.square.spltcoverage.io.CoverageReader;
import lab.square.spltcoverage.model.ProductCoverageManager;
import lab.square.spltcoverage.model.ProductGraph;
import lab.square.spltcoverage.report.GraphVizGenerator;

public class VizGeneratorTest {
	
	@Test
	public void testGeneratorMinepump() {
		String directory;
		String classDirectory;
		directory = "D:/directorypath/minepump/";
		classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/minepump/bin";
		
		Collection<ProductGraph> heads = createLinker(directory, classDirectory);
		
		try {
			GraphVizGenerator.generate(heads);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//@Test
	public void testGeneratorGpl() {
		String directory;
		String classDirectory;
		directory = "D:/directorypath/gpl/";
		classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/gpl/bin";
		
		Collection<ProductGraph> heads = createLinker(directory, classDirectory);
		
		try {
			GraphVizGenerator.generate(heads);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Collection<ProductGraph> createLinker(String directory, String classDirectory) {
		ProductCoverageManager manager = new ProductCoverageManager();
		CoverageReader reader = new CoverageReader(manager, directory, classDirectory);
		try {
			reader.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Collection<ProductGraph> heads = ProductLinker.link(manager);
		if (heads.isEmpty())
			fail();
		
		return heads;
	}
}