package serialization;

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.*;

public class Tree{
	public static class Entry {
		private final String name;
		private final Tree tree;

		public Entry(String n, Tree t){
			name = n; tree = t;
		}

		public String name(){
			return name;
		}

		public Tree tree(){
			return tree;
		}
	}

	// Exactly one of children and value will be null
	private final List<Entry> children;
	private final String value;

	// Non-leaf constructor
	public Tree(){
		children = new ArrayList<Entry>();
		value = null;
	}

	// Leaf constructor
	public Tree(String v){
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

	/* Leaf representation:
	 * List of characters, preceded by %
	 * Character representation:
	 * \% - end of string
	 * \\ - a literal \
	 * any other character - it
	 * Non-leaf representation:
	 * List of trees, preceded by [, succeeded by ]
	 * (this should be unambiguous)
	 */

	public String toString(){
		StringWriter sw = new StringWriter();
		try{
			toString(sw);
		}catch(IOException e){} // impossible!
		return sw.toString();
	}

	public void toString(Appendable out) throws IOException {
		if(isLeaf()){
			out.append('%');
			out.append(value.replace("\\", "\\\\"));
			out.append("\\%");
		}else{
			out.append('[');
			for(Entry c : children()){
				out.append(c.name().replace("\\", "\\\\"));
				out.append("\\%");
				c.tree().toString(out);
			}
			out.append(']');
		}
	}

	private static String readStr(String in, int[] cur) throws ParseException {
		StringBuilder out = new StringBuilder();

		for(;;){
			int i = in.indexOf('\\', cur[0]);
			if(i < 0)
				throw new ParseException("Unterminated string", cur[0]);
			out.append(in.substring(cur[0], i));
			cur[0] = i + 2;
			if(in.charAt(cur[0] - 1) == '%')
				return out.toString();
			out.append(in.charAt(cur[0] - 1));
		}
	}

	// cur.length == 1; cur[0] read and updated as cursor position in the string
	public static Tree fromString(String in, int[] cur) throws ParseException {
		if(in.charAt(cur[0]) == '%'){
			cur[0]++;
			return new Tree(readStr(in, cur));
		}
		if(in.charAt(cur[0]) == '['){
			cur[0]++;
			Tree out = new Tree();
			while(in.charAt(cur[0]) != ']')
				out.add(new Entry(readStr(in, cur), fromString(in, cur)));
			cur[0]++;
			return out;
		}
		throw new ParseException("Invalid input", cur[0]);
	}

	public static Tree fromString(String in) throws ParseException {
		return fromString(in, new int[1]);
	}

	public void print(){
		print("");
	}

	private void print(String indent){
		if(isLeaf())
			System.out.println(indent + value());
		else{
			String next = indent + " ";
			for(Tree.Entry c : children()){
				System.out.println(indent + c.name());
				c.tree().print(next);
			}
		}
	}
}
