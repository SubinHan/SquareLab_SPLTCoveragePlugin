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

	public static int LEFT_TO_RIGHT = 0x00000000;
	public static int TOP_TO_BOTTOM = 0x00000001;
	public static int DRAW_ALL_ARROW = 0x00000002;
	public static int DIRECTED = 0x00000004;
	public static int HIGHLIGHT_PROBLEM_PRODUCTS = 0x00000008;

	public static final Config CONFIG_DEFAULT_TOPTOBOTTOM = new Config("product", TOP_TO_BOTTOM | DRAW_ALL_ARROW);
	public static final Config CONFIG_DEFAULT_LEFTTORIGHT = new Config("product", LEFT_TO_RIGHT | DRAW_ALL_ARROW);
	public static final Config CONFIG_LIGHT_TOPTOBOTTOM = new Config("product", TOP_TO_BOTTOM);
	public static final Config CONFIG_LIGHT_LEFTTORIGHT = new Config("product", LEFT_TO_RIGHT);
	public static final Config CONFIG_SHOWPROBLEM_TOPTOBOTTOM = new Config("product", TOP_TO_BOTTOM | DRAW_ALL_ARROW | HIGHLIGHT_PROBLEM_PRODUCTS);
	
	private static final int RENDER_HEIGHT = 2048;

	public GraphVizGenerator() {

	}

	public static void generate(ProductGraph root, Config config)
			throws IOException {
		Collection<ProductGraph> roots = new ArrayList<ProductGraph>();
		roots.add(root);
		generate(roots, config);
	}

	public static void generate(Collection<ProductGraph> roots, Config config)
			throws IOException {
		Graph g = Factory.graph("report").graphAttr().with(Rank.dir((TOP_TO_BOTTOM & config.config) != 0 ? RankDir.TOP_TO_BOTTOM : RankDir.LEFT_TO_RIGHT)).linkAttr().with("class",
				"link-class");

		Node node = Factory.node(config.rootName);

		for (ProductGraph root : roots) {
			g = g.with(linkChildrenRecur(root, node, new HashSet<String>(), (DRAW_ALL_ARROW & config.config) != 0, (HIGHLIGHT_PROBLEM_PRODUCTS & config.config) != 0));
		}

		try {
			Graphviz.fromGraph(g).height(RENDER_HEIGHT).render(Format.PNG).toFile(new File("example/ex1.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Node linkChildrenRecur(ProductGraph product, Node node, Set<String> visited, boolean drawAllArrow,
			boolean highlightProblemProducts) {
		for (ProductGraph child : product.getChildren()) {
			if (!drawAllArrow && visited.contains(child.getProductCoverage().getFeatureSet().toString()))
				continue;
			visited.add(child.getProductCoverage().getFeatureSet().toString());

			Node childNode = Factory.node(toLightString(child.getProductCoverage().getFeatureSet()));
			if (highlightProblemProducts && !product.isCoveredMoreThanParent()) {
				childNode = childNode.with(Color.RED);
			}
			node = node.link(
					Factory.to(linkChildrenRecur(child, childNode, visited, drawAllArrow, highlightProblemProducts)));
		}
		return node;
	}

	private static String toLightString(Map<String, Boolean> featureSet) {
		StringBuilder builder = new StringBuilder();

		for (String feature : featureSet.keySet()) {
			if (featureSet.get(feature)) {
				builder.append(feature);
				builder.append(" ");
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

	public static class Config {
		String rootName;
		final int config;

		public Config(String rootName, int config) {
			this.rootName = rootName;
			this.config = config;
		}
		
		public void setRootName(String title) {
			this.rootName = title;
		}
	}
}
