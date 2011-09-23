package data;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import serialization.Tree;

public class Database { // Call this something different and make it a class
	
	public static Document newDocument(){
		//set up the factory
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Should return XML instead of String, but for meantime use String
	public static String treeToXML(Tree tree){
		StringWriter sw = new StringWriter();
		sw.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		treeToXML(tree, sw);
		return sw.toString();
	}
	
	public static void treeToXML(Tree tree, StringWriter sw){
		if(tree.isLeaf()){
			sw.append("*"+tree.value());
		}else{
			for(Tree.Entry t: tree.children()){
				sw.append("<" + t.name() + ">");
				treeToXML(t.tree(), sw);
				sw.append("</" + t.name() + ">");
			}
		}
	}
	
	public static String test(Tree tree){
		return documentToString(treeToDOC(tree));
	}
	
	public static Document treeToDOC(Tree tree){
		Document doc = newDocument();
		treeToDOC(tree,doc,doc);
		return doc;
	}
	
	public static void treeToDOC(Tree tree, Document doc, Node node){
		if(tree.isLeaf()){
			node.appendChild(doc.createTextNode("*"+tree.value()));
		}else{
			for(Tree.Entry t : tree.children()){
				Node n = doc.createElement(t.name());
				System.out.println("t.name(): " + t.name());
				System.out.println("t.tree(): " + t.tree());
				System.out.println("node: "+ n);
				treeToDOC(t.tree(), doc, n);
				node.appendChild(n);
				System.out.println("node.appendChild(n): "+ n);
			}
		}
	}
	
	public static String documentToString(Document doc){
		StringWriter sw = new StringWriter();
		try {
			TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(sw));
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		}
		return sw.toString();
	}
	
	public static Tree treeFromXML(String XML){
		//set up the factory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document d = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		//provide the XML input
		InputSource is = new InputSource( new StringReader( XML ) );
		//this will fail without valid XML, it must have "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
		try {
			d = builder.parse( is );
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Node node = d.getChildNodes().item(0);
		return treeFromXML(node);
	}
	

	//Should take XML instead of String, but for meantime use String
	public static Tree treeFromXML(Node node){
		if (node.hasChildNodes()){
			//if it does, get them into a node list
			NodeList nl = node.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++){
				//looping over the second level of XML provided, aka the children of the root class
				treeFromXML(nl.item(0));
			}
		}
		else{
			if (node.getNodeName().toLowerCase().startsWith("*")){
				//if the node name equals one of these predefined values, set the string to be the nodes content
				return new Tree(node.getTextContent().substring(1));
			}
		}
		return null;
	}
	
	public String treeToString(Tree tree){
		return null;
		
	}
	public Tree treeFromString(String str){
		return null;
		
	}
	public void writeTreeToXMLFile(Tree tree, String path){
		
	}
	public Tree readTreeFromXMLFile(String path){
		return null;
		
	}
}