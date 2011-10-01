package serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

import javax.xml.bind.DatatypeConverter;

import data.Database;


/**
 * 
 * Serializing objects to the Tree
 * List, Set, MapEntry, Map, String, tree, integer, double and nullable objects
 * 
 * @author wafaahma
 *
 */
public class Serializers {
	/**
	 * A List<T> serializer class which implements read and write methods 
	 * @author wafaahma
	 *
	 * @param <T>
	 */
	public static class List<T> implements Serializer<java.util.List<T>> {
		private final Serializer<T> elemSerializer;

		//Constructor
		public List(Serializer<T> s){
			elemSerializer = s;
		}

		public java.util.List<T> read(Tree in){
			java.util.List<T> out = new ArrayList<T>();
			for(Tree.Entry c : in.children()){
				assert c.name().equals("i");
				out.add(elemSerializer.read(c.tree()));
			}
			return out;
		}

		public Tree write(java.util.List<T> in){
			return write((java.util.Collection<T>)in);
		}

		public Tree write(java.util.Collection<T> in){
			Tree out = new Tree();
			for(T c : in)
				out.add(new Tree.Entry("i", elemSerializer.write(c)));
			return out;
		}
	}

	/**
	 * A Set<T> serializer class which implements read and write methods 
	 * @author wafaahma
	 *
	 * @param <T>
	 */
	// two-pass (first creates a List then a Set .. simpler, doesn't really need to be efficient: we're using XML anyway
	public static class Set<T> implements Serializer<java.util.Set<T>> {
		// Serializers.List<T>, not java.util.List<T>
		public final List<T> listSerializer;

		public Set(Serializer<T> s){
			listSerializer = new List<T>(s);
		}

		public java.util.Set<T> read(Tree in){
			return new HashSet<T>(listSerializer.read(in));
		}

		public Tree write(java.util.Set<T> in){
			return listSerializer.write(in);
		}
	}
	
	/**
	 * A MapEntry<K, V> serializer class which implements read and write methods 
	 * @author wafaahma
	 *
	 * @param <T>
	 */
	public static class MapEntry<K, V> implements Serializer<java.util.Map.Entry<K, V>> {
		private final Serializer<K> keySerializer;
		private final Serializer<V> elemSerializer;

		public MapEntry(Serializer<K> k, Serializer<V> v){
			keySerializer = k;
			elemSerializer = v;
		}

		public java.util.Map.Entry<K, V> read(Tree in){
			K k = null;
			V v = null;
			boolean kg = false, vg = false;

			for(Tree.Entry c : in.children())
				if(c.name().equals("k")){
					kg = true;
					k = keySerializer.read(c.tree());
				}
				else if(c.name().equals("v")){
					vg = true;
					v = elemSerializer.read(c.tree());
				}
			return kg && vg? new java.util.AbstractMap.SimpleImmutableEntry<K, V>(k, v) : null;
		}

		public Tree write(java.util.Map.Entry<K, V> in){
			Tree out = new Tree();
			out.add(new Tree.Entry("k", keySerializer.write(in.getKey())));
			out.add(new Tree.Entry("v", elemSerializer.write(in.getValue())));
			return out;
		}
	}

	/**
	 * A Map<K, V> serializer class which implements read and write methods 
	 * @author wafaahma
	 *
	 * @param <T>
	 */
	public static class Map<K, V> implements Serializer<java.util.Map<K, V>> {
		private final Set<java.util.Map.Entry<K, V>> setSerializer;

		public Map(Serializer<K> k, Serializer<V> v){
			setSerializer = new Set<java.util.Map.Entry<K, V>>(new MapEntry<K, V>(k, v));
		}

		public java.util.Map<K, V> read(Tree in){
			// I thought there was some HashMap<K, V>(Set<Map.Entry<K, V>>) .. meh
			java.util.Set<java.util.Map.Entry<K, V>> s = setSerializer.read(in);
			java.util.Map<K, V> out = new java.util.HashMap<K, V>();
			for(java.util.Map.Entry<K, V> kv : s)
				out.put(kv.getKey(), kv.getValue());
			return out;
		}

		public Tree write(java.util.Map<K, V> in){
			return setSerializer.write(in.entrySet());
		}
	}
	
	public static class JSerializable<T extends java.io.Serializable> implements Serializer<T> {
		@SuppressWarnings("unchecked")
		public T read(Tree in){
			byte[] bytes = DatatypeConverter.parseBase64Binary(Database.escapeNewLines(in.value()));
			T out = null;
			try {
				ObjectInputStream reader = new ObjectInputStream(new ByteArrayInputStream(bytes));
				out = (T)reader.readObject();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			return out;
		}

		public Tree write(T in) {
			String encoded = null;
			
			try {
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				ObjectOutputStream writer = new ObjectOutputStream(bytes);
				writer.writeObject(in);
				encoded = Database.escapeNewLines(DatatypeConverter.printBase64Binary(bytes.toByteArray()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return new Tree(encoded);
		}
	}

	public static final Serializer<String> Serializer_String = new Serializer<String>(){
		public String read(Tree in){
			return in.value();
		}

		public Tree write(String in){
			return new Tree(in);
		}
	};
	
	public static final Serializer<Tree> Serializer_Tree = new Serializer<Tree>(){

		public Tree read(Tree in) {
			return in;
		}

		public Tree write(Tree in) {
			return in;
		}
	};
	
	public static final Serializer<Integer> Serializer_Integer = new Serializer<Integer>(){
		public Integer read(Tree in){
			return Integer.parseInt(in.value());
		}

		public Tree write(Integer in){
			return new Tree(in.toString());
		}
	};

	public static final Serializer<Long> Serializer_Long = new Serializer<Long>(){
		public Long read(Tree in){
			return Long.parseLong(in.value());
		}

		public Tree write(Long in){
			return new Tree(in.toString());
		}
	};

	public static final Serializer<Double> Serializer_Double = new Serializer<Double>(){
		public Double read(Tree in){
			return Double.parseDouble(in.value());
		}

		public Tree write(Double in){
			return new Tree(in.toString());
		}
	};

	public static final Serializer<Boolean> Serializer_Boolean = new Serializer<Boolean>(){
		public Boolean read(Tree in){
			return Boolean.valueOf(in.value());
		}

		public Tree write(Boolean in){
			return new Tree(in.toString());
		}
	};

	/**
	 * Wraps another serializer to support null values.
	 * Other serializers can not necessarily hold null values.
	 * @author wafaahma
	 *
	 * @param <T>
	 */
	public static class Nullable<T> implements Serializer<T> {
		private final Serializer<T> elemSerializer;

		public Nullable(Serializer<T> s){
			elemSerializer = s;
		}

		public T read(Tree in){
			for(Tree.Entry c : in.children()){
				assert c.name().equals("just");
				return elemSerializer.read(c.tree());
			}
			return null;
		}

		public Tree write(T in){
			Tree out = new Tree();
			if(in != null)
				out.add(new Tree.Entry("just", elemSerializer.write(in)));
			return out;
		}
	}
}
