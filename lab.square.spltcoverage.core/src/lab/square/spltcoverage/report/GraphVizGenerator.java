package lab.square.spltcoverage.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Rank.RankDir;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;
import lab.square.spltcoverage.model.ProductGraph;

public class GraphVizGenerator {
	
	private static final int RENDER_HEIGHT = 2048;

	public GraphVizGenerator() {

	}

	public static void generate(ProductGraph root) throws IOException {
		Collection<ProductGraph> roots = new ArrayList<ProductGraph>();
		roots.add(root);
		generate(roots);
	}
	
	public static void generate(Collection<ProductGraph> roots) throws IOException {
		Graph g = Factory.graph("report").directed().graphAttr().with(Rank.dir(RankDir.LEFT_TO_RIGHT)).linkAttr()
				.with("class", "link-class");

		Node node = Factory.node("product");
		
		for(ProductGraph root : roots) {
			g = g.with(linkChildrenRecur(root, node, new HashSet<String>()));
		}
		
		try {
			Graphviz.fromGraph(g).height(RENDER_HEIGHT).render(Format.PNG).toFile(new File("example/ex1.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Node linkChildrenRecur(ProductGraph product, Node node, Set<String> visited) {
		for (ProductGraph child : product.getChildren()) {
			if(visited.contains(child.getProductCoverage().getFeatureSet().toString()))
				continue;
			visited.add(child.getProductCoverage().getFeatureSet().toString());
			
			Node childNode = Factory.node(toLightString(child.getProductCoverage().getFeatureSet()));
			if(!product.isCoveredMoreThanParent()) {
				childNode = childNode.with(Color.RED);
			}
			node = node.link(Factory.to(linkChildrenRecur(child, childNode, visited)));
		}
		return node;
	}
	
	private static String toLightString(Map<String, Boolean> featureSet) {
		StringBuilder builder = new StringBuilder();
		
		for(String feature : featureSet.keySet()) {
			if(featureSet.get(feature)) {
				builder.append(feature);
				builder.append(" " );
			}
		}
		
		return builder.toString();
	}

	public static void main(String[] args) {

		Graph g = Factory.graph("example").directed().graphAttr().with(Rank.dir(RankDir.TOP_TO_BOTTOM)).linkAttr()
				.with("class", "link-class")
				.with(Factory.node("aasdfassafaswqfqfwqfqfasfsafsafafafasf").with(Color.RED).link(Factory.node("b")),
						Factory.node("b").link(Factory.to(Factory.node("c"))// .with(Factory.linkAttrs().add("weight",
																			// 5), Style.DASHED)
						));
		try {
			Graphviz.fromGraph(g).height(200).render(Format.PNG).toFile(new File("example/ex1.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
