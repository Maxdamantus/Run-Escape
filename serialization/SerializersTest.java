package serialization;

import java.util.*;

import junit.framework.TestCase;

import data.Database;
import org.junit.*;

/**
 * A test is provided for every serializer in serializers class.
 * 
 * @author wafaahma
 *
 */
public class SerializersTest extends TestCase{
	String treeToString = "";
	String xmlToString = "";
	
	/**
	 * Tests the tree and xml serializations in general
	 * @throws ParseException
	 */
	@Test public void testGeneral() throws ParseException {
		Serializer<List<Integer>> serLI = new Serializers.List<Integer>(Serializers.Serializer_Integer);
		treeToString = "[i\\||1\\|i\\||2\\|rrr\\||\\|rrr\\||*\\|rrr\\||#\\|rrr\\|| \\|rrr\\||<>\\|bbb\\|[]]";
		xmlToString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
				"\n<tree>" +
				"\n  <i>1</i>" +
				"\n  <i>2</i>" +
				"\n  <rrr>*</rrr>" +
				"\n  <rrr>**</rrr>" +
				"\n  <rrr>*#</rrr>" +
				"\n  <rrr> </rrr>" +
				"\n  <rrr>&lt;&gt;</rrr>" +
				"\n  <bbb>#</bbb>" +
				"\n</tree>\n";

		//A Serializer for List<integer>
		List<Integer> integerList = new ArrayList<Integer>();
		integerList.add(1);
		integerList.add(2);
		
		//Adding entries to the tree
		Tree listInt = serLI.write(integerList);
		listInt.add(new Tree.Entry("rrr", new Tree("")));
		listInt.add(new Tree.Entry("rrr", new Tree("*")));
		listInt.add(new Tree.Entry("rrr", new Tree("#")));
		listInt.add(new Tree.Entry("rrr", new Tree(" ")));
		listInt.add(new Tree.Entry("rrr", new Tree("<>")));
		listInt.add(new Tree.Entry("bbb", new Tree()));
		assertEquals(listInt.toString(), treeToString);
		
		//TreeToXML
		String xml = Database.treeToXML(listInt);
		assertEquals(xml, xmlToString);
	}
	
	/**
	 * Tests a list of Strings
	 * @throws ParseException
	 */
	@Test public void testListofString() throws ParseException {
		Serializer<List<String>> serLS = new Serializers.List<String>(Serializers.Serializer_String);
		treeToString = "[i\\||foo\\|i\\||bar\\|i\\||baz\\|]";
		xmlToString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
				"\n<tree>" +
				"\n  <i>foo</i>" +
				"\n  <i>bar</i>" +
				"\n  <i>baz</i>" +
				"\n</tree>\n";
		List<String> foo = new ArrayList<String>();
		foo.add("foo");
		foo.add("bar");
		foo.add("baz");
		
		Tree listString = serLS.write(foo);
		System.out.print(listString.toString());
		assertEquals(listString.toString(), treeToString);
		
		String xml = Database.treeToXML(listString);
		System.out.print(xml);
		System.out.print(xmlToString);
		assertEquals(xml, xmlToString);
	}
	
	/**
	 * tests map of String to String
	 * @throws ParseException
	 */
	@Test public void testMapofStringtoString() throws ParseException {
		Serializer<Map<String,String>> serLS = new Serializers.Map<String,String>(Serializers.Serializer_String,Serializers.Serializer_String);
		treeToString = "[i\\|[k\\||baz\\|v\\||baz\\|]i\\|[k\\||foo\\|v\\||foo\\|]i\\|[k\\||bar\\|v\\||bar\\|]]";
		xmlToString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
				"\n<tree>" +
				"\n  <i>" +
				"\n    <k>baz</k>" +
				"\n    <v>baz</v>" +
				"\n  </i>" +
				"\n  <i>" +
				"\n    <k>foo</k>" +
				"\n    <v>foo</v>" +
				"\n  </i>" +
				"\n  <i>" +
				"\n    <k>bar</k>" +
				"\n    <v>bar</v>" +
				"\n  </i>" +
				"\n</tree>\n";
		Map<String,String> foo = new HashMap<String,String>();
		foo.put("foo","foo");
		foo.put("bar","bar");
		foo.put("baz","baz");
		
		Tree listString = serLS.write(foo);
		System.out.print(listString.toString());
		assertEquals(listString.toString(), treeToString);
		
		String xml = Database.treeToXML(listString);
		System.out.print(xml);
		System.out.print(xmlToString);
		assertEquals(xml, xmlToString);
	}
	
	/**
	 * Tests a list of Strings
	 * @throws ParseException
	 */
	@Test public void testListofStringException() {
		Tree listString = null;
		Serializer<List<String>> serLS = new Serializers.List<String>(Serializers.Serializer_String);
		treeToString = "[i\\||foo\\|i\\||bar\\|i\\||baz\\|]";
		xmlToString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
				"\n<tree>" +
				"\n  <i>foo</i>" +
				"\n  <i>bar</i>" +
				"\n  <i>baz</i>" +
				"\n</tree>\n";
		List<String> foo;// = new ArrayList<String>();
//		foo.add("foo");
//		foo.add("bar");
//		foo.add("baz");
//		ListString = serLS.write(foo);
		try{
			foo = serLS.read(Database.xmlToTree("fdsad"+xmlToString));
			System.out.print(foo);
			assertTrue(false);
//			assertEquals(listString.toString(), treeToString);
		}catch(serialization.ParseException e){
		}
//		String xml = Database.treeToXML(listString);
//		System.out.print(xml);
//		System.out.print(xmlToString);
//		assertEquals(xml, xmlToString);
	}

	
}
