package serialization.utl;

import java.util.*;
import serialization.Tree;

public class TreeImpl implements Tree {
	// Exactly one of children and value will be null
	private final List<Entry> children;
	private final String value;

	// Non-leaf constructor
	public TreeImpl(){
		children = new ArrayList<Entry>();
		value = null;
	}

	// Leaf constructor
	public TreeImpl(String v){
		children = null;
		value = v;
	}

	public boolean isLeaf(){
		return value != null;
	}

	public String value(){
		if(value == null)
			throw new NullPointerException("Tree is not a leaf node");
		return value;
	}

	public Iterable<Entry> children(){
		if(children == null)
			throw new NullPointerException("Tree is a leaf node");
		return Collections.unmodifiableList(children);
	}

	public void add(Entry e){
		children.add(e);
	}

	public Entry get(int i){
		return children.get(i);
	}
}
