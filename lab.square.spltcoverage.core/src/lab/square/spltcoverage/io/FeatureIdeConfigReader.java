package lab.square.spltcoverage.io;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import lab.square.spltcoverage.model.FeatureSet;

public class FeatureIdeConfigReader {

	public static FeatureSet readFeatureSet(String targetPath) {
		FeatureSet result = new FeatureSet();

		NodeList features = getFeaturesList(targetPath);

		for (int i = 0; i < features.getLength(); i++) {
			if (isGarbageItem(features.item(i)))
				continue;

			Node feature = features.item(i);

			NamedNodeMap attributes = feature.getAttributes();
			if (isSelectedFeature(feature)) {
				result.setFeature(getFeatureName(feature), true);
			} else {
				result.setFeature(getFeatureName(feature), false);
			}
		}

		return result;
	}

	private static String getFeatureName(Node feature) {
		return getNodeValue(feature, "name");
	}

	private static boolean isSelectedFeature(Node feature) {
		String automaticValue = getNodeValue(feature, "automatic");
		String manualValue = getNodeValue(feature, "manual");

		boolean result = automaticValue.equalsIgnoreCase("selected") || manualValue.equalsIgnoreCase("selected");

		return result;
	}

	private static String getNodeValue(Node feature, String string) {
		Node value = feature.getAttributes().getNamedItem(string);
		if (value == null)
			return "";
		return value.getNodeValue();
	}

	private static NodeList getFeaturesList(String targetPath) {
		Document document = makeDocument(targetPath);
		Element root = document.getDocumentElement();
		NodeList features = root.getChildNodes();
		return features;
	}

	private static boolean isGarbageItem(Node item) {
		return !item.hasAttributes();
	}

	private static Document makeDocument(String targetPath) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		builder = newDocumentBuilder(factory);
		Document document = makeDocument(targetPath, builder);
		return document;
	}

	private static Document makeDocument(String targetPath, DocumentBuilder builder) {
		Document document = null;
		try {
			document = builder.parse(targetPath);
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		return document;
	}

	private static DocumentBuilder newDocumentBuilder(DocumentBuilderFactory factory) {
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return builder;
	}

}
