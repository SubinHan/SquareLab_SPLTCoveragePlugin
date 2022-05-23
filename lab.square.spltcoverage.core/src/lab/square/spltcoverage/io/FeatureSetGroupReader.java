package lab.square.spltcoverage.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author selab
 *
 */
public class FeatureSetGroupReader implements IFeatureSetReader {

	private String directory;

	public FeatureSetGroupReader(String directory) {
		this.directory = directory;
	}

	@Override
	public Map<String, Boolean> read() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<Map<String, Boolean>> readAll() {
		File groupFolder = new File(directory);
		Collection<Map<String, Boolean>> products = new LinkedList<Map<String, Boolean>>();

		for (File productFolder : groupFolder.listFiles()) {
			File featureSetFile = findFeatureSetFile(productFolder);
			if(featureSetFile == null)
				continue;

			Map<String, Boolean> featureSet = readFeatureSet(featureSetFile);
			
			products.add(featureSet);
		}

		return products;
	}

	private Map<String, Boolean> readFeatureSet(File featureSetFile) {
		Map<String, Boolean> featureSet = new HashMap<String, Boolean>();
		if(isOlderVersionFeatureSet(featureSetFile.getName())) {
			readFeatureSetOld(featureSetFile, featureSet);
		}
		else {
			readFeatureSetNew(featureSetFile, featureSet);
		}
		return featureSet;
	}

	private void readFeatureSetNew(File featureSetFile, Map<String, Boolean> featureSet) {
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = db.parse(featureSetFile);
			NodeList featureList = document.getElementsByTagName("feature");
			for (int i = 0; i < featureList.getLength(); i++) {
				Node feature = featureList.item(i);
				if(feature.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element)feature;
					String name = element.getAttribute("name");
					String selected = element.getAttribute("manual");
					featureSet.put(name, selected.equalsIgnoreCase("selected"));
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	private void readFeatureSetOld(File featureSetFile, Map<String, Boolean> featureSet) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(featureSetFile));
			String line = null;
			while((line = reader.readLine()) != null){
				featureSet.put(line, true);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean isOlderVersionFeatureSet(String name) {
		return name.toUpperCase().endsWith(".CONFIG");
	}

	private File findFeatureSetFile(File productFolder) {
		for (File file : productFolder.listFiles()) {
			if(isOlderVersionFeatureSet(file.getName())) {
				return file;
			}
			// newer
			if(file.getName().toUpperCase().endsWith(".XML")) {
				return file;
			}
		}
		return null;
	}

}
