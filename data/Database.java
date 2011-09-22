package data;

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
import org.xml.sax.InputSource;

import serialization.Tree;
import xml.XML;

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
				node.appendChild(n);
				treeToDOC(t.tree(), doc, n);
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
	
	//Should take XML instead of String, but for meantime use String
//	public static Tree treeFromXML(String xml){
//		InputSource is = new InputSource( new StringReader( xml ) );
//		//this will fail without valid XML, it must have "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
//		Document d = newDocument().parse( is );
//		return null;
//	}
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