package lab.square.spltcoverage.test;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collection;

import org.junit.Test;

import lab.square.spltcoverage.core.analysis.CoverageReader;
import lab.square.spltcoverage.core.analysis.ProductLinker;
import lab.square.spltcoverage.model.ProductCoverageManager;
import lab.square.spltcoverage.model.ProductGraph;
import lab.square.spltcoverage.report.VizGenerator;

public class VizGeneratorTest {
	
	@Test
	public void generatorTestMinepump() {
		String directory;
		String classDirectory;
		directory = "D:/directorypath/minepump/";
		classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/minepump/bin";
		
		Collection<ProductGraph> heads = createLinker(directory, classDirectory);
		
		try {
			VizGenerator.generate(heads);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//@Test
	public void generatorTestGpl() {
		String directory;
		String classDirectory;
		directory = "D:/directorypath/gpl/";
		classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/gpl/bin";
		
		Collection<ProductGraph> heads = createLinker(directory, classDirectory);
		
		try {
			VizGenerator.generate(heads);
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

		ProductLinker linker = new ProductLinker(manager);
		Collection<ProductGraph> heads = linker.linkAll();
		if (heads.isEmpty())
			fail();
		
		return heads;
	}
}
