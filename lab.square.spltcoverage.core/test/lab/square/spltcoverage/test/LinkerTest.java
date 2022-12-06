package lab.square.spltcoverage.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

import org.junit.BeforeClass;
import org.junit.Test;

import lab.square.spltcoverage.core.analysis.ProductLinker;
import lab.square.spltcoverage.io.AbstractSplCoverageReader;
import lab.square.spltcoverage.io.FeatureSetGroupReader;
import lab.square.spltcoverage.io.SplCoverageReaderFactory;
import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.model.ProductNode;
import lab.square.spltcoverage.model.SplCoverage;
import lab.square.spltcoverage.test.target.Configuration;

/*
 * Test 시: core의 plugin.xml dependency -> org.jacoco (0.8.6) 이어야 함.
 * Plug-in testing으로 수행할 것.
 */
public class LinkerTest {
	
	private static Collection<ProductNode> visited;
	private static SplCoverage splCoverage;
	private static Collection<Answer> expected;

	@BeforeClass
	public static void setUp() {
		visited = new ArrayList<>();
		expected = new HashSet<>();
				
		FeatureSet featureSet1 = new FeatureSet();
		FeatureSet featureSet2 = new FeatureSet();
		FeatureSet featureSet3 = new FeatureSet();
		FeatureSet featureSet4 = new FeatureSet();
		FeatureSet featureSet5 = new FeatureSet();
		
		featureSet2.addFeature(Configuration.CONFIG_A);
		
		featureSet3.addFeature(Configuration.CONFIG_B);
		
		featureSet4.addFeature(Configuration.CONFIG_A);
		featureSet4.addFeature(Configuration.CONFIG_B);
		
		featureSet5.addFeature(Configuration.CONFIG_A);
		featureSet5.addFeature(Configuration.CONFIG_B);
		featureSet5.addFeature(Configuration.CONFIG_C);
		
		expected.add(new Answer(0, Collections.emptyList(), featureSet1));
		expected.add(new Answer(1, new HashSet(Arrays.asList(featureSet1)), featureSet2));
		expected.add(new Answer(1, new HashSet(Arrays.asList(featureSet1)), featureSet3));
		expected.add(new Answer(2, new HashSet(Arrays.asList(featureSet2, featureSet3)), featureSet4));
		expected.add(new Answer(3, new HashSet(Arrays.asList(featureSet4)), featureSet5));
	}
	
	@Test
	public void testLinkerWithOnlyFeatureSets() {
		String directory;
		directory = TestConfig.FEATURE_SET_GROUP_NEW;

		testLinker(directory);
	}

	private void testLinker(String directory) {
		FeatureSetGroupReader reader = new FeatureSetGroupReader(directory);
		Collection<FeatureSet> products = reader.readAll();

		Collection<ProductNode> heads = ProductLinker.link(products);
		verifyGraph(heads);
	}
	
	@Test
	public void testLinkerWithSplCoverage() {
		AbstractSplCoverageReader reader =
				SplCoverageReaderFactory.createInvariableSplCoverageReader(TestConfig.CLASSPATH_SELF);
		
		splCoverage = reader.readSplCoverage(TestConfig.SPL_COVERAGE_PATH);
		
		Collection<ProductNode> heads = ProductLinker.link(splCoverage);
		verifyGraph(heads);
	}

	private void verifyGraph(Collection<ProductNode> heads) {
		if (heads.isEmpty())
			fail();

		for (ProductNode head : heads)
			visitGraphRecur(head);
	}

	private void visitGraphRecur(ProductNode node) {
		if (visited.contains(node))
			return;
		visited.add(node);
		System.out.println("//============================//");
		System.out.println("Level: " + node.getLevel());
		System.out.println("Feature Set:");
		System.out.print("  ");
		printFeatures(node.getFeatureSet());
		System.out.println("Parent's Feature Set:");
		Collection<FeatureSet> parentFeatureSet = new HashSet<>();
		for (ProductNode parent : node.getParents()) {
			if (parent == null)
				continue;
			System.out.print("  ");
			printFeatures(parent.getFeatureSet());
			parentFeatureSet.add(parent.getFeatureSet());
		}

		System.out.println("Children's Feature Set:");
		for (ProductNode child : node.getChildren()) {
			if (child == null)
				continue;
			System.out.print("  ");
			printFeatures(child.getFeatureSet());
		}
		
		Answer answer = new Answer(node.getLevel(), parentFeatureSet, node.getFeatureSet());
		assertTrue(expected.contains(answer));
		
		for (ProductNode child : node.getChildren()) {
			visitGraphRecur(child);
		}
	}

	private void printFeatures(FeatureSet featureSet) {
		for (String feature : featureSet.getFeatures()) {
			System.out.print(feature + " ");
		}
		System.out.println();
	}
	
	private static class Answer {
		public final int level;
		public final Collection<FeatureSet> parentFeatureSet;
		public final FeatureSet featureSet;
		
		public Answer(int level, Collection<FeatureSet> parentFeatureSet, FeatureSet featureSet) {
			this.level = level;
			this.parentFeatureSet = parentFeatureSet;
			this.featureSet = featureSet;
		}

		@Override
		public int hashCode() {
			return Objects.hash(level) + featureSet.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof Answer))
				return false;
			Answer other = (Answer)obj;
			
			if(this.level != other.level)
				return false;
			
			if(!this.featureSet.equals(other.featureSet))
				return false;
			
			if(this.parentFeatureSet.size() != other.parentFeatureSet.size())
				return false;
			
			for(FeatureSet featureSet: other.parentFeatureSet) {
				if(!(this.parentFeatureSet.contains(featureSet)))
					return false;
			}
			
			return true;
		}
		
		
	}
	
}
