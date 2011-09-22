package serialization.utl;
import java.util.*;
import java.io.StringWriter;
import java.text.*;

//import data.Database;

import serialization.Serializer;
import serialization.Tree;


class SerializersTest {
	public static void main(String[] argv) throws ParseException {
		Serializer<List<String>> serL = new Serializers.List<String>(Serializers.string);
		Serializer<Map<String, List<String>>> serM = new Serializers.Map<String, List<String>>(Serializers.string, serL);
		
		//List<Integer>
		Serializer<List<Integer>> serLI = new Serializers.List<Integer>(Serializers.integer);
//		Serializer<Map<String, List<String>>> serM = new Serializers.Map<String, List<>>(Serializers.integer, serLI);
		
		//A Serializer for Map<String, List<String>>
//		Database db = null; // Dont need this when Database methods are static

		List<String> foo = new ArrayList<String>();
		foo.add("foo");
		foo.add("bar");
		foo.add("baz");
		Tree lt = serL.write(foo);
//		XML xml1 = db.treeToXML(lt); // Write to xml
//		List<String> fooOut1 = serL.read(db.treeFromXML(xml1)); // Read from xml
//		String txt1 = db.treeToString(lt); // Write to txt
//		List<String> fooOut2 = serL.read(db.treeFromString(txt1)); // Read from txt
		
//		Map<String, List<String>> bar = new HashMap<String, List<String>>();
//		bar.put("qux", foo);
//		bar.put("abc", new ArrayList<String>(){{ add("def"); add("ghi"); }});
//		Tree mt = serM.write(bar);
//		XML xml2 = db.treeToXML(mt); // Write to xml
//		Map<String, List<String>> barOut = serM.read(db.treeFromXML(xml2)); // Read from xml
		
		
		//A Serializer for List<integer>
		List<Integer> integerList = new ArrayList<Integer>();
		integerList.add(1);
		integerList.add(2);
		Tree listInt = serLI.write(integerList);
		listInt.print();
		
		
		
	}
}