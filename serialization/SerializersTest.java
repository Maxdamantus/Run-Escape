package serialization;
import java.util.*;
import java.text.*;

import data.Database;


class SerializersTest {
	public static void main(String[] argv) throws ParseException {
//		Serializer<Map<String, List<String>>> serM = new Serializers.Map<String, List<String>>(Serializers.Serializer_String, serL);
		
		//List<Integer>
		Serializer<List<Integer>> serLI = new Serializers.List<Integer>(Serializers.Serializer_Integer);
		
		//A Serializer for Map<String, List<String>>

		List<String> foo = new ArrayList<String>();
		foo.add("foo");
		foo.add("bar");
		foo.add("baz");
		
		//A Serializer for List<integer>
		List<Integer> integerList = new ArrayList<Integer>();
		integerList.add(1);
		integerList.add(2);
		Tree listInt = serLI.write(integerList);
//		System.out.println(Database.treeToXML(listInt));
//		Database.treeFromXML((Database.treeToXML(listInt))).print();
		listInt.add(new Tree.Entry("rrr", new Tree("")));
		listInt.add(new Tree.Entry("rrr", new Tree("*")));
		listInt.add(new Tree.Entry("rrr", new Tree("#")));
		listInt.add(new Tree.Entry("rrr", new Tree(" ")));
		listInt.add(new Tree.Entry("rrr", new Tree("<>")));
		listInt.add(new Tree.Entry("bbb", new Tree()));
		System.out.println(listInt);
		String xml = Database.treeToXML(listInt);
		System.out.println(xml);
		System.out.println(Database.xmlToTree(xml));
//		listInt.print();
		
		
		
	}
}
