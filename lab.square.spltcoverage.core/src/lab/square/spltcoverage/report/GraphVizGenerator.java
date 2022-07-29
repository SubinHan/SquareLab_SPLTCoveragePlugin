package lab.square.spltcoverage.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Rank.RankDir;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Engine;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;
import lab.square.spltcoverage.model.ProductNode;

public class GraphVizGenerator {

	private static final String ROOT_NAME = "product";
	
	public static final int LEFT_TO_RIGHT = 0x00000000;
	public static final int TOP_TO_BOTTOM = 0x00000001;
	public static final int DRAW_ALL_ARROW = 0x00000002;
	public static final int DIRECTED = 0x00000004;
	public static final int HIGHLIGHT_PROBLEM_PRODUCTS = 0x00000008;
	public static final Config CONFIG_DEFAULT_TOPTOBOTTOM = new Config(ROOT_NAME, TOP_TO_BOTTOM | DRAW_ALL_ARROW);
	public static final Config CONFIG_DEFAULT_LEFTTORIGHT = new Config(ROOT_NAME, LEFT_TO_RIGHT | DRAW_ALL_ARROW);
	public static final Config CONFIG_LIGHT_TOPTOBOTTOM = new Config(ROOT_NAME, TOP_TO_BOTTOM);
	public static final Config CONFIG_LIGHT_LEFTTORIGHT = new Config(ROOT_NAME, LEFT_TO_RIGHT);
	public static final Config CONFIG_SHOWPROBLEM_TOPTOBOTTOM = new Config(ROOT_NAME,
			TOP_TO_BOTTOM | DRAW_ALL_ARROW | HIGHLIGHT_PROBLEM_PRODUCTS);
	public static final Config CONFIG_SHOWPROBLEM_LEFTTORIGHT = new Config(ROOT_NAME,
			LEFT_TO_RIGHT | DRAW_ALL_ARROW | HIGHLIGHT_PROBLEM_PRODUCTS);

	private static final int RENDER_HEIGHT = 2048;
	private static final String DEFAULT_OUTPUT_PATH = "vizResult/result.png";

	public GraphVizGenerator() {
		// TODO: Make the constructor to config the graph drawing
	}

	public static void generate(ProductNode root, Config config) throws IOException {
		generate(root, config, DEFAULT_OUTPUT_PATH);
	}
	
	public static void generate(ProductNode root, Config config, String outputPath) throws IOException {
		Collection<ProductNode> roots = new ArrayList<>();
		roots.add(root);
		generate(roots, config, outputPath);
	}
	
	public static void generate(Collection<ProductNode> roots, Config config) throws IOException {
		generate(roots, config, DEFAULT_OUTPUT_PATH);
	}

	public static void generate(Collection<ProductNode> roots, Config config, String outputPath) throws IOException {
		if(!outputPath.toLowerCase().endsWith(".png"))
			outputPath = outputPath + ".png";
		
		Graph g = Factory.graph("report").graphAttr()
				.with(Rank.dir((TOP_TO_BOTTOM & config.config) != 0 ? RankDir.TOP_TO_BOTTOM : RankDir.LEFT_TO_RIGHT))
				.linkAttr().with(Style.SOLID);

		Node node = Factory.node(config.rootName);

		Map<String, Node> visited = new HashMap<>();

		for (ProductNode root : roots) {
			node = node.link(linkChildrenRecur(root, node, visited, (DRAW_ALL_ARROW & config.config) != 0,
					(HIGHLIGHT_PROBLEM_PRODUCTS & config.config) != 0));
		}
		g = g.with(node);

		try {
			Graphviz.fromGraph(g).engine(Engine.DOT).height(RENDER_HEIGHT).render(Format.PNG).toFile(new File(outputPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Node linkChildrenRecur(ProductNode product, Node parent, Map<String, Node> visited, boolean drawAllArrow,
			boolean highlightProblemProducts) {
		Node node;
		
		String key = toLightString(product.getFeatureSet());
		
		if(!visited.containsKey(key)){
			node = Factory.node(toLightString(product.getFeatureSet()));
			if(highlightProblemProducts && !product.isCoveredMoreThanParent()) {
				node = node.with(Color.RED);
			}
			
			for(ProductNode child : product.getChildren()) {
				node = node.link(Factory.to(linkChildrenRecur(child, node, visited, drawAllArrow, highlightProblemProducts)));
				visited.put(key, node);
			}
		}
		else {
			node = visited.get(key);
		}
		
		return node;
		
	}

	private static String toLightString(Map<String, Boolean> featureSet) {
		StringBuilder builder = new StringBuilder();

		for (Entry<String, Boolean> entry : featureSet.entrySet()) {
			if (entry.getValue()) {
				builder.append(entry.getKey());
				builder.append(" ");
			}
		}

		return builder.toString().trim();
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
