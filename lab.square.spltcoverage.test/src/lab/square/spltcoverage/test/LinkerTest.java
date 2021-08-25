package lab.square.spltcoverage.test;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import org.jacoco.core.analysis.IClassCoverage;
import org.junit.Test;

import junit.framework.TestCase;
import lab.square.spltcoverage.core.analysis.CoverageReader;
import lab.square.spltcoverage.core.analysis.ProductLinker;
import lab.square.spltcoverage.core.model.ProductCoverage;
import lab.square.spltcoverage.core.model.ProductCoverageManager;
import lab.square.spltcoverage.core.model.ProductGraph;

public class LinkerTest extends TestCase {
	
	private Collection<ProductGraph> visited;
	private int count = 0;
	
	@Test
	public void testLinkerBankaccount() {
		String directory;
		String classDirectory;
		directory = "D:/directorypath/bankaccount/";
		classDirectory = "D:/workspacechallenege/challenge-master/workspace_IncLing/bankaccount/bin/bankaccount";
		
		testLinker(directory, classDirectory);
	}
	
	@Test
	public void testLinkerFeatureAmp1() {
		String directory;
		String classDirectory;
		directory = "D:/directorypath/featureamp1";
		classDirectory = "D:\\workspacechallenege\\challenge-master\\workspace_IncLing\\FeatureAMP1\\bin";
		
		testLinker(directory, classDirectory);
	}

	private void testLinker(String directory, String classDirectory) {
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
		}
		
		int different = pc.getScore() - findParentsScore(graph);
		System.out.print("Score: " + pc.getScore());
		if(different >= 0)
			System.out.print(" (+" + different + ")");
		else
			System.out.print(" (" + different + ")");
		System.out.println();
		
		Map<String, IClassCoverage> parentMap;

		
		for(ProductGraph child : graph.getChildren()) {
			visitGraphRecur(child);
		}
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
			Collection<IClassCoverage> parentClassCoverages = parent.getProductCoverage().getClassCoverages();
			
			for(IClassCoverage cc : parentClassCoverages) {
				if(cc.getName().equals(name))
					return cc.getLineCounter().getCoveredRatio();
			}
			break;
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
