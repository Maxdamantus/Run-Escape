package data;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import serialization.Tree;

public class Database { // Call this something different and make it a class
	
	private static final String XML_ROOT = "tree";
	
	public static Document newDocument(){
		//set up the factory
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	//Should return XML instead of String, but for meantime use String
//	public static String treeToXML(Tree tree){
//		StringWriter sw = new StringWriter();
//		sw.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
//		treeToXML(tree, sw);
//		return sw.toString();
//	}
//	
//	public static void treeToXML(Tree tree, StringWriter sw){
//		if(tree.isLeaf()){
//			sw.append("*"+tree.value());
//		}else{
//			for(Tree.Entry t: tree.children()){
//				sw.append("<" + t.name() + ">");
//				treeToXML(t.tree(), sw);
//				sw.append("</" + t.name() + ">");
//			}
//		}
//	}
	
	public static String treeToXML(Tree tree){
		return documentToString(treeToDOC(tree), true);
	}
	
	public static Document treeToDOC(Tree tree){
		Document doc = newDocument();
		treeToDOC(tree,doc,doc.appendChild(doc.createElement(XML_ROOT)));
		return doc;
	}
	
	private static void treeToDOC(Tree tree, Document doc, Node node){
		if(tree.isLeaf()){
			node.appendChild(doc.createTextNode("*"+tree.value()));
		}else{
			for(Tree.Entry t : tree.children()){
				Node n = doc.createElement(t.name());
				treeToDOC(t.tree(), doc, n);
				node.appendChild(n);
			}
		}
	}
	
	public static String documentToString(Node doc, boolean indent){
		StringWriter sw = new StringWriter();
		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
			if(indent) {
				tFactory.setAttribute("indent-number", 2);
			}
			Transformer transformer = tFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, indent?"yes":"no");
			transformer.transform(new DOMSource(doc), new StreamResult(sw));
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		}
		return sw.toString();
	}
	
	public static Tree xmlToTree(String XML){
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
		
		stripEmptyNodes(node);
		
		return xmlToTree(node);
	}
	

	private static void stripEmptyNodes(Node node) {
		XPathFactory xpathFactory = XPathFactory.newInstance();
		// XPath to find empty text nodes.
		XPathExpression xpathExp;
		NodeList emptyTextNodes = null;
		try {
			xpathExp = xpathFactory.newXPath().compile("//text()[normalize-space(.) = '']");
			emptyTextNodes = (NodeList)xpathExp.evaluate(node, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		// Remove each empty text node from document.
		for (int i = 0; i < emptyTextNodes.getLength(); i++) {
		    Node emptyTextNode = emptyTextNodes.item(i);
		    emptyTextNode.getParentNode().removeChild(emptyTextNode);
		}
	}

	//Should take XML instead of String, but for meantime use String
	private static Tree xmlToTree(Node node){
		NodeList nl = node.getChildNodes();
		Node firstChild = nl.item(0);
		if(nl.getLength() > 0 && firstChild instanceof Text){
				return new Tree(((Text)firstChild).getWholeText().substring(1));
		}
		Tree t = new Tree();
		for(int i = 0; i < nl.getLength(); i++)
			t.add(new Tree.Entry(nl.item(i).getNodeName(), xmlToTree(nl.item(i))));
		return t;
		/*
		if (node.hasChildNodes()){
			//if it does, get them into a node list
			NodeList nl = node.getChildNodes();
			Tree tree = new Tree();
			for (int i = 0; i < nl.getLength(); i++){
				//looping over the second level of XML provided, aka the children of the root class
				xmlToTree(nl.item(0));
			}
			return tree;
		}
		else{
			if(node.getNodeName().toLowerCase().startsWith("*")){
				//if the node name equals one of these predefined values, set the string to be the nodes content
				return new Tree(node.getTextContent().substring(1));
			}
		}
		return null;*/
	}
	
	public static String treeToString(Tree tree){
		return tree.toString();
	}
	public Tree treeFromString(String str){
		try {
			return Tree.fromString(str);
		} catch (ParseException e) {
			throw new RuntimeException("Parse exception: " + e.getMessage()); // TODO: wtf
		}
	}
	public void writeTreeToXMLFile(Tree tree, String path){
		
	}
	public Tree readTreeFromXMLFile(String path){
		return null;
		
	}
}