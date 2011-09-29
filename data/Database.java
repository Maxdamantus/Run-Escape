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


/**
 * This class is dealing with changing XML file to Tree and from Tree to XML.
 * You may also call TreeToStrong from this class
 * 
 * @author wafaahma
 *
 */
public class Database {
	
	private static final String XML_ROOT = "tree";
	private static final char STRING_ESCAPE = '*';
	private static final char TREE_ESCAPE = '#';
	
	/**
	 * Creates a new Document using dom library. The document will get called by the internal methods.
	 * @return Document
	 */
	public static Document newDocument(){
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * This method can be called from outside which then calls the private treeToDOC method.
	 * A method which returns a toString version of the tree after changing it to a doc.
	 * @param tree
	 * @return calls the documentToString method
	 */
	public static String treeToXML(Tree tree){
		return documentToString(treeToDOC(tree), true);
	}
	
	/**
	 * Creates a new doc and calls the treeToDOC method.
	 * It will also adds the root child to the doc tree.
	 * @param tree
	 * @return document
	 */
	public static Document treeToDOC(Tree tree){
		Document doc = newDocument();
		treeToDOC(tree,doc,doc.appendChild(doc.createElement(XML_ROOT)));
		return doc;
	}
	
	/**
	 * Change a tree to a XML document.
	 * * is used to represent an empty string
	 * # is used for an empty list
	 * These escape charactors will get added to the XML file and when reading gets taken out.
	 * They will not have any effects on the contents of the XML file.  
	 * @param tree
	 * @param doc
	 * @param node
	 */
	private static void treeToDOC(Tree tree, Document doc, Node node){
		if(tree.isLeaf()){
			String value = tree.value();
			if(value.length() == 0 || value.charAt(0) == STRING_ESCAPE || value.charAt(0) == TREE_ESCAPE) {
				value = STRING_ESCAPE + value;
			}
			node.appendChild(doc.createTextNode(value));
		}else{
			if(tree.size() > 0) {
				for(Tree.Entry t : tree.children()){
					Node n = doc.createElement(t.name());
					treeToDOC(t.tree(), doc, n);
					node.appendChild(n);
				}
			}
			else {
				node.appendChild(doc.createTextNode(""+TREE_ESCAPE));
			}
		}
	}
	
	/**
	 * Returns a toString of indented XML file from doc.
	 * @param doc
	 * @param indent
	 * @return String XML file
	 */
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
	
	/**
	 * This method will call another method to get rid of indentation, then calls a private method to convert XML to Tree.
	 * @param String XML
	 * @return Tree
	 */
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
	
	/**
	 * This method will get rid of the indentations of the the XML file
	 * @param root node
	 */
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

	/**
	 * Converts the XML to Tree
	 * * is used to represent an empty string
	 * # is used for an empty list
	 * These escape charectors will get removed from the file and will not effect the content of the Tree.
	 * @param node
	 * @return
	 */
	private static Tree xmlToTree(Node node){
		NodeList nl = node.getChildNodes();
		Node firstChild = nl.item(0);
		if(nl.getLength() > 0 && firstChild instanceof Text){
			String text = ((Text)firstChild).getWholeText();
			if(text.charAt(0) == STRING_ESCAPE) {
				return new Tree(text.substring(1));
			}
			else if(text.charAt(0) == TREE_ESCAPE) {
				return new Tree();
			}
			else {
				return new Tree(text);
			}
		}
		Tree t = new Tree();
		for(int i = 0; i < nl.getLength(); i++)
			t.add(new Tree.Entry(nl.item(i).getNodeName(), xmlToTree(nl.item(i))));
		return t;
	}
	
	/**
	 * A helper method which calls tree.toString()
	 * @param tree
	 * @return tree.toString()
	 */
	public static String treeToString(Tree tree){
		return tree.toString();
	}
	
	/**
	 * A helper method which calls tree.fromString(str).
	 * @param str
	 * @return
	 */
	public Tree treeFromString(String str){
		try {
			return Tree.fromString(str);
		} catch (ParseException e) {
			throw new RuntimeException("Parse exception: " + e.getMessage()); // TODO: wtf
		}
	}
	
	/**
	 * A helper method which adds new line to the XML file
	 * @param string
	 * @return String with indentation
	 */
	public static String escapeNewLines(String s) {
		return s.replaceAll("@n", "@@n").replaceAll("\\n", "@n");
	}
	
	/**
	 * A helper method which removes new line to the XML file
	 * @param string
	 * @return String without indentation
	 */
	public static String unescapeNewLines(String s) {
		return s.replaceAll("@@n", "@n").replaceAll("@n", "\n");
	}
}