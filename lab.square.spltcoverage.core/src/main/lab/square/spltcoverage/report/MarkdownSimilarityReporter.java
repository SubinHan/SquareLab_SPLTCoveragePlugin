package lab.square.spltcoverage.report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import lab.square.similaritymeasure.core.ISimilarityMeasurer;
import lab.square.similaritymeasure.core.Jaccard;
import lab.square.spltcoverage.io.MarkdownTableBuilder;
import lab.square.spltcoverage.model.FeatureSet;
import lab.square.spltcoverage.utils.Tools;

public class MarkdownSimilarityReporter {

	Collection<FeatureSet> products;

	public MarkdownSimilarityReporter(Collection<FeatureSet> products) {
		this.products = products;
	}

	public void generateReport(String directory, String fileName) {
		MarkdownTableBuilder builder = new MarkdownTableBuilder(products.size());
		
		builder = buildHeader(builder);
		builder = buildContents(builder);
		
		directory = directory.replace('\\', '/');
		if(!(directory.endsWith("/")))
			directory = directory.concat("/");
		
		try (FileWriter writer = new FileWriter(new File(directory + fileName + ".md"))){
			Files.createDirectories(Paths.get(directory));
			writer.write(builder.build());
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private MarkdownTableBuilder buildContents(MarkdownTableBuilder builder) {
		List<VectorAdapter> vectors = adaptVectors();
		ISimilarityMeasurer jaccard = new Jaccard();
		
		String[][] similarities = new String[vectors.size()][vectors.size()]; 
		
		for(int i = 0; i < vectors.size(); i++) {
			for(int j = i + 1; j < vectors.size(); j++) {
				try {
					similarities[i][j] = String.format("%.2f", jaccard.compare(vectors.get(i), vectors.get(j)));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		for(int i = 0; i < vectors.size(); i++) {
			builder = buildRow(builder, similarities, i);
		}
		
		return builder;
	}
	
	private MarkdownTableBuilder buildRow(MarkdownTableBuilder builder, String[][] similarities, int rowNum) {
		String[] columns = new String[similarities[rowNum].length];
		System.arraycopy(similarities[rowNum], 0, columns, 0, similarities[rowNum].length);
		
		return builder.addRow(columns);		
	}

	private MarkdownTableBuilder buildHeader(MarkdownTableBuilder builder) {
		String[] headers = new String[products.size()];
		int i = 0;
		for (FeatureSet product : products) {
			headers[i++] = toLightString(product);
		}
		
		return builder.addHeader(headers);
	}
	
	private String toLightString(FeatureSet product) {
		StringBuilder builder = new StringBuilder();
		
		for(String feature : product.getFeatures()) {
			builder.append(feature + " ");
		}
		return builder.toString();
	}
	
	private List<VectorAdapter> adaptVectors() {
		List<String> existsFeatures = Tools.getAllExistsFeatures(products);
		List<VectorAdapter> vectors = new LinkedList<>();
		for(FeatureSet product : products) {
			VectorAdapter adapter = new VectorAdapter(existsFeatures, product);
			vectors.add(adapter);
		}
		return vectors;
	}
}
