package serialization;

import java.util.*;

public class SerializerUnion<T> {
	private final Map<String, Reader<? extends T>> readers = new HashMap<String, Reader<? extends T>>();
	private final Map<String, Writer<? super T>> writers = new HashMap<String, Writer<? super T>>();
	private final List<Identifier<T>> identifiers = new LinkedList<Identifier<T>>();

	public static interface Identifier<T> {
		public String type(T obj);
	}

	public String identify(T v){
		for(Identifier<T> i : identifiers){
			String n = i.type(v);
			if(n != null)
				return n;
		}
		return null;
	}

	public Serializer<T> serializer(){
		return new Serializer<T>(){
			public Tree write(T in){
				String type = identify(in);
				if(type == null)
					throw new NullPointerException("No identifier for " + in);
				if(!writers.containsKey(type))
					throw new NullPointerException("No writer for " + type);
				Tree out = new Tree();
				out.add(new Tree.Entry("type", new Tree(type)));
				out.add(new Tree.Entry("value", writers.get(type).write(in)));
				return out;
			}

			public T read(Tree in){
				String type = in.find("type").value();
				return readers.get(type).read(in.find("value"));
			}
		};
	}

	public void addReader(String name, Reader<? extends T> reader){
		readers.put(name, reader);
	}

	public void addWriter(String name, Writer<? super T> writer){
		writers.put(name, writer);
	}

	public void addSerializer(String name, Serializer<T> serializer){
		addReader(name, serializer);
		addWriter(name, serializer);
	}

	public void addIdentifier(Identifier<T> i){
		identifiers.add(i);
	}
}
