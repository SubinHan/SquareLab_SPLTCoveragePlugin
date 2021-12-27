package lab.square.spltcoverage.test;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collection;

import org.junit.Test;

import lab.square.spltcoverage.core.analysis.ProductGraphAnalyzer;
import lab.square.spltcoverage.core.analysis.ProductLinker;
import lab.square.spltcoverage.io.CoverageReader;
import lab.square.spltcoverage.model.ProductCoverageManager;
import lab.square.spltcoverage.model.ProductGraph;

public class PGAnalyzerTest {
	
	//@Test
	public void testBankaccount() {
		String directory;
		String classDirectory;
		directory = "D:/directorypath/bankaccount/";
		classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/bankaccount/bin/bankaccount";
		
		testAnalyzer(directory, classDirectory);
	}
	
	//@Test
	public void testLinkerFeatureAmp1() {		
		String directory;
		String classDirectory;
		directory = "D:/directorypath/featureamp1";
		classDirectory = "D:\\workspacechallenege\\challenge-master\\workspace_IncLing\\FeatureAMP1\\bin";
		
		testAnalyzer(directory, classDirectory);
	}
	
	//@Test
	public void testLinkerFeatureAmp8() {		
		String directory;
		String classDirectory;
		directory = "D:/directorypath/featureamp8";
		classDirectory = "D:\\workspacechallenege\\challenge-master\\workspace_IncLing\\FeatureAMP8\\bin";
		
		testAnalyzer(directory, classDirectory);
	}
	
	//@Test
	public void testLinkerAtm() {		
		String directory;
		String classDirectory;
		directory = "D:/directorypath/atm";
		classDirectory = "D:\\workspacechallenege\\challenge-master\\workspace_IncLing\\ATM\\bin";
		
		testAnalyzer(directory, classDirectory);
	}
	
	//@Test
	public void testLinkerChess() {		
		String directory;
		String classDirectory;
		directory = "D:/directorypath/chess";
		classDirectory = "D:\\workspacechallenege\\challenge-master\\workspace_IncLing\\Chess\\bin";
		
		testAnalyzer(directory, classDirectory);
	}
	
	@Test
	public void testLinkerElevator() {		
		String directory;
		String classDirectory;
		directory = "D:/directorypath/elevator";
		classDirectory = "D:\\workspacechallenege\\challenge-master\\workspace_IncLing\\Elevator\\bin";
		
		testAnalyzer(directory, classDirectory);
	}
	
	//@Test
	public void testLinkerMinepump() {		
		String directory;
		String classDirectory;
		directory = "D:/directorypath/minepump";
		classDirectory = "D:\\workspacechallenege\\challenge-master\\workspace_IncLing\\MinePump\\bin";
		
		testAnalyzer(directory, classDirectory);
	}

	//@Test
	public void testLinkerVendingMachine() {		
		String directory;
		String classDirectory;
		directory = "D:/directorypath/vendingmachine";
		classDirectory = "D:\\workspacechallenege\\challenge-master\\workspace_IncLing\\vending\\bin";
		
		testAnalyzer(directory, classDirectory);
	}


	private void testAnalyzer(String directory, String classDirectory) {
		ProductCoverageManager manager = new ProductCoverageManager();
		CoverageReader reader = new CoverageReader(manager, directory, classDirectory);
		try {
			reader.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(directory);
		
		Collection<ProductGraph> heads = ProductLinker.link(manager);
		if (heads.isEmpty())
			fail();
		
		ProductGraphAnalyzer analyzer = new ProductGraphAnalyzer(heads);
		for(ProductGraph problem : analyzer.getProblemProducts()) {
			System.out.println(problem.getProductCoverage().getFeatureSet());
		}
		
		System.out.println("Problem features :");
		
		for(Collection<String> set : analyzer.getProblemFeatures()) {
			System.out.print("  {");
			for(String key : set) {
				System.out.print(key + ", ");
			}
			System.out.println("}");
		}
		System.out.println("# Problem products: " + analyzer.getProblemProducts().size());
		
	}
}