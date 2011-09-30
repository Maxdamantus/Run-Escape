package serialization;

import java.util.*;

public class SerializerUnion<T> {
	private final Map<String, Reader<? extends T>> readers = new HashMap<String, Reader<? extends T>>();
	private final Map<String, Writer<? super T>> writers = new HashMap<String, Writer<? super T>>();
	private final Identifier<T> identifier;

	public static interface Identifier<T> {
		public String type(T obj);
	}

	public SerializerUnion(Identifier<T> i){
		identifier = i;
	}

	public Serializer<T> serializer(){
		return new Serializer<T>(){
			public Tree write(T in){
				String type = identifier.type(in);
				Tree out = new Tree();
				out.add(new Tree.Entry("type", new Tree(type)));
				out.add(new Tree.Entry("value", writers.get(type).write(in)));
				return out;
			}

			public T read(Tree in){
				String type = in.find("type").value();
				return readers.get(type).read(in);
			}
		};
	}

	public void addReader(String name, Reader<? extends T> reader){
		readers.put(name, reader);
	}

	public void addReader(String name, Writer<? super T> writer){
		writers.put(name, writer);
	}
}
