package code.summaries.xml.parser;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import code.summaries.xml.camel.split.CamelCaseSplit;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A boundary class for parse xmls. It communicates with {@link File},
 * {@link FileAppend}, {@link DocumentBuilderFactory}, and other 5 classes.
 * 
 * @stereotype BOUNDARY
 */
public class ParseXml {

	/**
	 * @stereotype COLLABORATOR
	 */

	public static void main(String argv[]) {
		List<String> voidMethodNames = new ArrayList<String>();
		try {

			File folder = new File(
					"C:/MS Software Engineering/Fall 2016/Research Projects/code summaries/SrcXml/");
			List<File> fileList = FileAppend.listFilesForFolder(folder);
			for (File f : fileList) {

				File fXmlFile = new File(f.getAbsolutePath());
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);

				// optional, but recommended
				// read this -
				// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
				doc.getDocumentElement().normalize();

				

				NodeList nList = doc.getElementsByTagName("class");
				NodeList blockChildList = null;
				NodeList funcChildList = null;

			

				for (int temp = 0; temp < nList.getLength(); temp++) {

					Node nNode = nList.item(temp);

					
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {

						Element eElement = (Element) nNode;

						System.out.println("Class Name : "
								+ eElement.getElementsByTagName("name").item(0)
										.getTextContent());

						blockChildList = eElement.getElementsByTagName("block");
						

					}
				}
				if (null != blockChildList) {
					for (int i = 0; i < blockChildList.getLength(); i++) {

						Node nNode = blockChildList.item(i);

						

						if (nNode.getNodeType() == Node.ELEMENT_NODE) {

							Element eElement = (Element) nNode;
							if (eElement.getElementsByTagName("function")
									.getLength() != 0) {
								

								funcChildList = eElement
										.getElementsByTagName("function");
								
							}
						}
					}
				}
				if (null != funcChildList) {
					for (int j = 0; j < funcChildList.getLength(); j++) {

						Node njNode = funcChildList.item(j);

						

						if (njNode.getNodeType() == Node.ELEMENT_NODE) {

							Element eElement = (Element) njNode;

							

							if (eElement.getElementsByTagName("type").item(0)
									.getTextContent().equals("void")) {

								System.out
										.println("*************************************");

								System.out.println(eElement
										.getElementsByTagName("name").item(1)
										.getTextContent()
										+ " is a Void S_Unit");

								voidMethodNames.add(eElement
										.getElementsByTagName("name").item(1)
										.getTextContent());

								System.out
										.println("*************************************");
							} 
						}

					}

				}

			}
			System.out.println("Hiiii");
			Map<String, LinkedList<String>> splitMethodSignatureMap = getCamelCaseSplit(voidMethodNames);
			getPOSTagger(splitMethodSignatureMap);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void getPOSTagger(
			Map<String, LinkedList<String>> splitMethodSignatureMap) {

		MaxentTagger tagger = new MaxentTagger(
				"taggers/english-left3words-distsim.tagger");

		for (Map.Entry<String, LinkedList<String>> entry : splitMethodSignatureMap
				.entrySet()) {

			System.out.println("*****Start POS Tagger ****");

			System.out.println("Key : " + entry.getKey() + " Value : "
					+ entry.getValue());

			System.out.println(tagger.tagString(entry.getValue().toString()));
		}

	}

	private static Map<String, LinkedList<String>> getCamelCaseSplit(
			List<String> voidMethodNames) {

		LinkedList<String> splitMethodSignature = new LinkedList<String>();
		Map<String, LinkedList<String>> splitMethodSignatureMap = new HashMap<String, LinkedList<String>>();

		for (String s : voidMethodNames) {
			splitMethodSignature = CamelCaseSplit.splitCamelCaseString(s);
			splitMethodSignatureMap.put(s, splitMethodSignature);
		}
		System.out.println("*****Start Camel Case Split****");

		for (Map.Entry<String, LinkedList<String>> entry : splitMethodSignatureMap
				.entrySet()) {
			System.out.println("Key : " + entry.getKey() + " Value : "
					+ entry.getValue());
		}
		return splitMethodSignatureMap;
	}

}
