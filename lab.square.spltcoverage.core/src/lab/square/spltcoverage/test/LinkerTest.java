package lab.square.spltcoverage.test;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.jacoco.core.analysis.IClassCoverage;
import org.junit.Before;
import org.junit.Test;

import lab.square.spltcoverage.core.analysis.ProductLinker;
import lab.square.spltcoverage.io.SpltCoverageReader;
import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.ProductCoverageManager;
import lab.square.spltcoverage.model.ProductGraph;

/*
 * Test 시: core의 plugin.xml dependency -> org.jacoco (0.8.6) 이어야 함.
 * Plug-in testing으로 수행할 것.
 */
public class LinkerTest {
	
	private Collection<ProductGraph> visited;
	private int count = 0;
	private int notEnough = 0;
	private final double criteria = .5f;
	
	@Before
	public void setUp() {
		notEnough = 0;
		count = 0;
	}
	
	//@Test
	public void testLinkerGpl() {		
		String directory;
		String classDirectory;
		directory = "D:/directorypath/gpl/";
		classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/gpl/bin";
		
		testLinker(directory, classDirectory);
		
		pinrtNumOfNotEnoughs();
	}
	
	//@Test
	public void testLinkerMinepump() {		
		String directory;
		String classDirectory;
		directory = "D:/directorypath/minepump/";
		classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/minepump/bin";
		
		testLinker(directory, classDirectory);
		
		pinrtNumOfNotEnoughs();
	}
	
	//@Test
	public void testLinkerNotepad() {		
		String directory;
		String classDirectory;
		directory = "D:/directorypath/notepad/";
		classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/notepad/bin";
		
		testLinker(directory, classDirectory);
		
		pinrtNumOfNotEnoughs();
	}
	
	//@Test
	public void testLinkerSudoku() {		
		String directory;
		String classDirectory;
		directory = "D:/directorypath/sudoku/";
		classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/sudoku/bin";
		
		testLinker(directory, classDirectory);
		
		pinrtNumOfNotEnoughs();
	}
	
	//@Test
	public void testLinkerVendingMachine() {		
		String directory;
		String classDirectory;
		directory = "D:/directorypath/vendingmachine/";
		classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/vending/bin";
		
		testLinker(directory, classDirectory);
		
		pinrtNumOfNotEnoughs();
	}
	
	//@Test
	public void testLinkerBankaccount() {		
		String directory;
		String classDirectory;
		directory = "D:/directorypath/bankaccount/";
		classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/bankaccount/bin/bankaccount";
		
		testLinker(directory, classDirectory);
		
		pinrtNumOfNotEnoughs();
	}
	
	//@Test
	public void testLinkerFeatureAmp1() {		
		String directory;
		String classDirectory;
		directory = "D:/directorypath/featureamp1";
		classDirectory = "D:\\workspacechallenege\\challenge-master\\workspace_IncLing\\FeatureAMP1\\bin";
		
		testLinker(directory, classDirectory);
		
		pinrtNumOfNotEnoughs();
	}
	
	//@Test
	public void testLinkerFeatureAmp8() {		
		String directory;
		String classDirectory;
		directory = "D:/directorypath/featureamp8";
		classDirectory = "D:\\workspacechallenege\\challenge-master\\workspace_IncLing\\FeatureAMP8\\bin";
		
		testLinker(directory, classDirectory);
		
		pinrtNumOfNotEnoughs();
	}
	
	
	//@Test
	public void testLinkerAtm() {		
		String directory;
		String classDirectory;
		directory = "D:/directorypath/atm";
		classDirectory = "D:\\workspacechallenege\\challenge-master\\workspace_IncLing\\ATM\\bin";
		
		testLinker(directory, classDirectory);
		
		pinrtNumOfNotEnoughs();
	}
	
	@Test
	public void testLinkerChess() {		
		String directory;
		String classDirectory;
		directory = "D:/directorypath/chess";
		classDirectory = "D:\\workspacechallenege\\challenge-master\\workspace_IncLing\\Chess\\bin";
		
		testLinker(directory, classDirectory);
		
		pinrtNumOfNotEnoughs();
	}
	
	//@Test
	public void testLinkerElevator() {		
		String directory;
		String classDirectory;
		directory = "D:/directorypath/elevator";
		classDirectory = "D:\\workspacechallenege\\challenge-master\\workspace_IncLing\\Elevator\\bin";
		
		testLinker(directory, classDirectory);
		
		pinrtNumOfNotEnoughs();
	}
	
	private void pinrtNumOfNotEnoughs() {
		System.out.println("Num of Products that the coverage didn't change than its parents: " + notEnough);
	}

	private void testLinker(String directory, String classDirectory) {
		ProductCoverageManager manager = new ProductCoverageManager();
		SpltCoverageReader reader = new SpltCoverageReader(manager, directory, classDirectory);
		try {
			reader.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Collection<ProductGraph> heads = ProductLinker.link(manager);
		if (heads.isEmpty())
			fail();
		
		visited = new HashSet<ProductGraph>();
		
		for(ProductGraph graph : heads) {
			visitGraphRecur(graph);
		}
		System.out.println(manager.getProductCoverages().size());
		System.out.println(count);
	}
	
	private void visitGraphRecur(ProductGraph graph) {
		if(visited.contains(graph))
			return;
		visited.add(graph);
		count++;
		double ratioSum = 0;
		
		ProductCoverage pc = graph.getProductCoverage();
		System.out.println("//============================//");
		System.out.println("Level: " + graph.getLevel());
		System.out.println("Feature Set:");
		System.out.print("  ");
		printFeatures(pc);
		System.out.println("Parent's Feature Set:");
		for(ProductGraph parent : graph.getParents()) {
			if(parent == null)
				continue;
			System.out.print("  ");
			printFeatures(parent.getProductCoverage());
		}
		System.out.println("Coverage: (The difference with the first parent)");
		Collection<IClassCoverage> classCoverages = pc.getClassCoverages();
		for(IClassCoverage cc : classCoverages) {
			System.out.println("  " + cc.getName());
			System.out.print("     Covered Ratio(Line): " + String.format("%.1f", cc.getLineCounter().getCoveredRatio() * 100));
			double different = cc.getLineCounter().getCoveredRatio() - findParentsLineRatio(graph, cc.getName());
			if(different >= 0)
				System.out.print("  (+" + String.format("%.1f", different * 100) + ")");
			else
				System.out.print("  (" + String.format("%.1f", different * 100) + ")");
			System.out.println();
			System.out.print("     Covered Ratio(Method): " + String.format("%.1f", cc.getMethodCounter().getCoveredRatio() * 100));
			different = cc.getMethodCounter().getCoveredRatio() - findParentsMethodRatio(graph, cc.getName());
			if(different >= 0)
				System.out.print("  (+" + String.format("%.1f", different * 100) + ")");
			else
				System.out.print("  (" + String.format("%.1f", different * 100) + ")");
			System.out.println();
			
			ratioSum += cc.getLineCounter().getCoveredRatio();
		}
		
		int different = pc.getScore() - findParentsScore(graph);
		System.out.print("Score: " + pc.getScore());
		if(different >= 0)
			System.out.print(" (+" + different + ")");
		else
			System.out.print(" (" + different + ")");
		System.out.println();
		
		Map<String, IClassCoverage> parentMap;

		if(isProblemProduct(graph))
			notEnough++;
			
		for(ProductGraph child : graph.getChildren()) {
			visitGraphRecur(child);
		}
	}
	
	private boolean isProblemProduct(ProductGraph graph) {
		ProductCoverage pc = graph.getProductCoverage();
		
		if(graph.getParents() == null)
			return false;
		
		for(ProductGraph parent : graph.getParents()) {
			if(parent == null)
				continue;
			Collection<IClassCoverage> classCoverages = pc.getClassCoverages();
			boolean isProblem = true;
			
			for(IClassCoverage cc : classCoverages) {
				double different = cc.getLineCounter().getCoveredRatio() - getLineRatioOfClass(parent.getProductCoverage().getClassCoverages(), cc.getName());
				if(different != 0) {
					isProblem = false;
					break;
				}
			}
			
			if(isProblem)
				return true;
		}
		return false;
	}

	private int findParentsScore(ProductGraph graph) {
		if(graph.getParents() == null)
			return 0;
		if(graph.getParents().isEmpty())
			return 0;
		
		for(ProductGraph parent : graph.getParents()) {
			if(parent == null)
				continue;
			return parent.getProductCoverage().getScore();
		}
		
		return 0;
	}

	private double findParentsLineRatio(ProductGraph graph, String name) {
		if(graph.getParents() == null)
			return 0.f;
		if(graph.getParents().isEmpty())
			return 0.f;
		
		for(ProductGraph parent : graph.getParents()) {
			if(parent == null)
				continue;
			
			return getLineRatioOfClass(parent.getProductCoverage().getClassCoverages(), name);
		}
		return 0.f;
	}
	
	private double getLineRatioOfClass(Collection<IClassCoverage> ccs, String className) {		
		for(IClassCoverage cc : ccs) {
			if(cc.getName().equals(className))
				return cc.getLineCounter().getCoveredRatio();
		}
		
		return 0.f;
	}
	
	private double findParentsMethodRatio(ProductGraph graph, String name) {
		if(graph.getParents() == null)
			return 0.f;
		if(graph.getParents().isEmpty())
			return 0.f;
		
		for(ProductGraph parent : graph.getParents()) {
			if(parent == null)
				continue;
			Collection<IClassCoverage> parentClassCoverages = parent.getProductCoverage().getClassCoverages();
			
			for(IClassCoverage cc : parentClassCoverages) {
				if(cc.getName().equals(name))
					return cc.getMethodCounter().getCoveredRatio();
			}
			break;
		}
		return 0.f;
	}


	private void printFeatures(ProductCoverage pc) {
		Map<String, Boolean> featureSet = pc.getFeatureSet();
		for(String key : featureSet.keySet()) {
			if(featureSet.get(key))
				System.out.print(key + " ");
		}
		System.out.println();
	}
}
