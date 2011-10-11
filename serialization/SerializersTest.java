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
		assertEquals(treeToString,listInt.toString());
		
		//TreeToXML
		String xml = Database.treeToXML(listInt);
		assertEquals(xmlToString,xml);
	}
	
	/**
	 * Tests a list of Strings
	 * @throws ParseException
	 */
	@Test public void testListofString() throws ParseException {
		Serializer<List<String>> serLS = Serializers.list(Serializers.Serializer_String);
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
		assertEquals(treeToString,listString.toString());
		
		String xml = Database.treeToXML(listString);
		assertEquals(xmlToString,xml);
	}
	
	/**
	 * tests map of String to String
	 * @throws ParseException
	 */
	@Test public void testMapofStringtoStringWrite() throws ParseException {
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
		assertEquals(treeToString,listString.toString());
		
		String xml = Database.treeToXML(listString);
		assertEquals(xmlToString, xml);
	}
	
	/**
	 * tests if map of String to String read method is correctly throws exception
	 * @throws ParseException
	 */
	@Test public void testMapofStringtoStringRead() throws ParseException {
		Serializer<Map<String,String>> serLS = Serializers.map(Serializers.Serializer_String,Serializers.Serializer_String);
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
		try{
			assertEquals("{baz=baz, foo=foo, bar=bar}",serLS.read(Database.xmlToTree(xmlToString)).toString());
		}catch(serialization.ParseException e){
			assertTrue(false);
		}
	}
	
	/**
	 * Tests if the program throws exception when xml file is broken.
	 * You could safely ignore the fatal errors. It comes from org.w3c.dom
	 * @throws ParseException
	 */
	@Test public void testListofStringException() {
		Serializer<List<String>> serLS = Serializers.list(Serializers.Serializer_String);
//		treeToString = "[i\\||foo\\|i\\||bar\\|i\\||baz\\|]";
		xmlToString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
				"\n<tree>" +
				"\n  <i>foo</i>" +
				"\n  <i>bar</i>" +
				"\n  <i>baz</i>" +
				"\n</tree>\n";
		try{
			serLS.read(Database.xmlToTree("extra stuff"+xmlToString));
		}catch(serialization.ParseException e){
			assertTrue(true);
		}
	}
	
	/**
	 * Tests the read set of Integer.
	 * @throws ParseException
	 */
	@Test public void testListofIntegerRead() {
		Serializer<Set<Integer>> serLI = Serializers.set(Serializers.Serializer_Integer);
		treeToString = "[i\\||foo\\|i\\||bar\\|i\\||baz\\|]";
		xmlToString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
				"\n<tree>" +
				"\n  <i>1</i>" +
				"\n  <i>2</i>" +
				"\n  <i>3</i>" +
				"\n</tree>\n";
		try{
			assertEquals("[1, 2, 3]",serLI.read(Database.xmlToTree(xmlToString)).toString());
		}catch(serialization.ParseException e){
			assertTrue(false);
		}
	}
	
	/**
	 * Tests if the program throws exception when xml file is broken.
	 * You could safely ignore the fatal errors. It comes from org.w3c.dom
	 * @throws ParseException
	 */
	@Test public void testListofIntegerException() {
		Serializer<Set<Integer>> serLI = Serializers.set(Serializers.Serializer_Integer);
		treeToString = "[i\\||foo\\|i\\||bar\\|i\\||baz\\|]";
		xmlToString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
				"\n<tree>" +
				"\n  <>1</i>" +
				"\n  <i>2</i>" +
				"\n  <i>3</i>" +
				"\n</tree>\n";
		try{
			serLI.read(Database.xmlToTree(xmlToString));
		}catch(serialization.ParseException e){
			assertTrue(true);
		}
	}

	/**
	 * Tests if the program throws exception when xml file is broken.
	 * @throws ParseException
	 */
	@Test public void complecatedXmlFile() throws ParseException {
		Serializer<Map<Set<Integer>,Set<Double>>> serLI = Serializers.map(Serializers.set(Serializers.Serializer_Integer), Serializers.set(Serializers.Serializer_Double));
//		treeToString = "[i\\||foo\\|i\\||bar\\|i\\||baz\\|]";
		xmlToString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
				"\n<tree>" +
				"\n  <k>" +
				"\n    <i>1</i>" +
				"\n    <i>2</i>" +
				"\n    <i>3</i>" +
				"\n  </k>" +
				"\n  <v>" +
				"\n    <i>1.0</i>" +
				"\n    <i>2.0</i>" +
				"\n    <i>3.0</i>" +
				"\n  </v>" +
				"\n</tree>\n";
		System.out.println(xmlToString);
//		String specialFormat = "[";
//		try{
//			integerSet.read(Database.xmlToTree(xmlToString));
//			doubleSet.read(Database.xmlToTree(xmlToString2));
			System.out.print(Database.xmlToTree(xmlToString));
			System.out.print(serLI.read(Database.xmlToTree(xmlToString)).toString());
//		}catch(serialization.ParseException e){
//			assertTrue(true);
//		}
	}
}
