package lab.square.spltclient.learningtest;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlParser {
	
	private static final String XML_PATH = "D:/workspace-featureide/Elevator-Antenna-v1.2-00001/configs/01.xml";
	
	@Test
	public void testXmlParsing() throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		Document document = builder.parse(XML_PATH);
		
		Element root = document.getDocumentElement();
		
		System.out.println(root.getNodeName());
		NodeList children = root.getChildNodes();
		
		for(int i = 0; i < children.getLength(); i++) {
			if(!children.item(i).hasAttributes())
				continue;
			NamedNodeMap attributes = children.item(i).getAttributes();
			for(int j = 0; j < attributes.getLength(); j++) {
				System.out.println(attributes.item(j).getNodeName());
				System.out.println(attributes.item(j).getNodeValue());
			}
		}
	}
}
