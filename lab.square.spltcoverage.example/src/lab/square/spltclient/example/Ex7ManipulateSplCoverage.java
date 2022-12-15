package lab.square.spltclient.example;

import java.util.Collection;

import org.jacoco.core.analysis.IClassCoverage;

import lab.square.spltcoverage.model.ICoverageModelComponent;
import lab.square.spltcoverage.model.ICoverageModelComposite;
import lab.square.spltcoverage.model.ISplCoverageVisitor;
import lab.square.spltcoverage.model.ProductCoverage;
import lab.square.spltcoverage.model.SplCoverage;
import lab.square.spltcoverage.model.TestCaseCoverage;
import lab.square.spltcoverage.model.TestMethodCoverage;

public class Ex7ManipulateSplCoverage {

	private static SplCoverage splCoverage;
	
	public static void main(String[] args) {
		// read again before we created coverages.
		splCoverage = Ex2ReadAntennaPlCoverage.readSplCoverage();
		
		// We can print children's coverage information naively.
		// SplCoverage is implementation of ICoverageModelComposite.
		// ICoverageModelComposites have its children, we can get children by calling getChildren().
		// The type of a child is ICoverageModelComponent.
		// ICoverageModelComponents have IClassCoverages.
		// IClassCoverage is a type provided by JaCoCo, has info about coverage.
		printChildren(splCoverage);
		
		// Hierarchy:
		// SplCoverages have ProductCoverages.
		// ProductCoverages have TestCaseCoverages.
		// TestCaseCoverages have TestMethodCoverages.
		// Following is the example of hierarchy:
		printHierarchy(splCoverage);
		
		// But sometimes, traversing hierarchy is too complex.
		// Following is how to traverse these with accept() method using Visitor Pattern.
		// Note that each visit method should calls visit() method again to low level.
		traverseByVisitor(splCoverage);
	}

	private static void printChildren(ICoverageModelComposite composite) {
		System.out.println("=====Print Children=====");
		for(ICoverageModelComponent child : composite.getChildren()) {
			System.out.println("Child name: " + child.getName());
			for(IClassCoverage coverage : child.getClassCoverages()) {
				System.out.println("\tClass name: " + coverage.getName());
				System.out.println("\tLine coverage: " + coverage.getLineCounter());
			}
		}
		
	}
	
	private static void printHierarchy(SplCoverage splCoverage) {
		System.out.println("=====Hierarchy=====");
		System.out.println("SplCoverage: " + splCoverage.getName());
		Collection<ICoverageModelComponent> productCoverages = splCoverage.getChildren();
		for(ICoverageModelComponent pcComponent : productCoverages) {
			if(!(pcComponent instanceof ProductCoverage))
				continue;
			ProductCoverage pc = (ProductCoverage)pcComponent;
			System.out.println("\tProductCoverage: " + pc.getName());
			
			Collection<ICoverageModelComponent> testCaseCoverages = pc.getChildren();
			for(ICoverageModelComponent tccComponent : testCaseCoverages) {
				if(!(tccComponent instanceof TestCaseCoverage))
					continue;
				TestCaseCoverage tcc = (TestCaseCoverage)tccComponent;
				System.out.println("\t\tTestCaseCoverage: " + tcc.getName());

				Collection<ICoverageModelComponent> testMethodCoverages = tcc.getChildren();
				
				for(ICoverageModelComponent tmcComponent : testMethodCoverages) {
					if(!(tmcComponent instanceof TestMethodCoverage))
						continue;
					TestMethodCoverage tmc = (TestMethodCoverage)tmcComponent;

					System.out.println("\t\t\tTestMethodCoverage: " + tmc.getName());
				}
			}
		}
	}
	
	private static void traverseByVisitor(SplCoverage splCoverage) {
		System.out.println("=====HierarchyByVisitor=====");
		
		splCoverage.accept(new MyVisitor());
	}
	
	private static class MyVisitor implements ISplCoverageVisitor {
		@Override
		public void visit(SplCoverage pcm) {
			System.out.println("SplCoverage: " + splCoverage.getName());
			
			Collection<ICoverageModelComponent> productCoverages = splCoverage.getChildren();
			for(ICoverageModelComponent pcComponent : productCoverages) {
				if(!(pcComponent instanceof ProductCoverage))
					continue;
				ProductCoverage pc = (ProductCoverage)pcComponent;
				visit(pc);
			}
		}

		@Override
		public void visit(ProductCoverage pc) {
			System.out.println("\tProductCoverage: " + pc.getName());
			
			Collection<ICoverageModelComponent> testCaseCoverages = pc.getChildren();
			for(ICoverageModelComponent tccComponent : testCaseCoverages) {
				if(!(tccComponent instanceof TestCaseCoverage))
					continue;
				TestCaseCoverage tcc = (TestCaseCoverage)tccComponent;
				visit(tcc);
			}
		}

		@Override
		public void visit(TestCaseCoverage tcc) {
			System.out.println("\t\tTestCaseCoverage: " + tcc.getName());

			Collection<ICoverageModelComponent> testMethodCoverages = tcc.getChildren();
			
			for(ICoverageModelComponent tmcComponent : testMethodCoverages) {
				if(!(tmcComponent instanceof TestMethodCoverage))
					continue;
				TestMethodCoverage tmc = (TestMethodCoverage)tmcComponent;
				visit(tmc);
			}
			
		}

		@Override
		public void visit(TestMethodCoverage tmc) {
			System.out.println("\t\t\tTestMethodCoverage: " + tmc.getName());
		}
		
	}
}
