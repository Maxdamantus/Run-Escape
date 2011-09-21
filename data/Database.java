package data;

import java.io.StringWriter;
import serialization.Tree;
import xml.XML;

public class Database { // Call this something different and make it a class
	
	public static XML treeToXML(Tree tree){
		StringWriter sw = new StringWriter();
		
		for(Tree.Entry t: tree.children()){
			sw.append("<" + t.name() + ">");
		}
		return null;
	}
	public static Tree treeFromXML(XML xml){
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