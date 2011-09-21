package data;

import serialization.Tree;
import xml.XML;

public interface Database { // Call this something different and make it a class
	public XML treeToXML(Tree tree); // Make this static
	public Tree treeFromXML(XML xml); // Make this static
	public String treeToString(Tree tree); // Make this static
	public Tree treeFromString(String str); // Make this static
	public void writeTreeToXMLFile(Tree tree, String path); // Make this static
	public Tree readTreeFromXMLFile(String path); // Make this static
}