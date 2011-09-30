package util;

import java.util.HashMap;
import java.util.Map;

import serialization.Serializer;
import serialization.Serializers;
import serialization.Tree;

public interface Serializable<T> {
	@SuppressWarnings("rawtypes")
	public static final Serializer<Serializable> serializer = new Serializer<Serializable>() {
		private static final String TYPE = "type";
		private static final String OBJECT = "object";
		
		@Override
		public Serializable<?> read(Tree in) {
			return null;
		}

		@SuppressWarnings({ "unchecked" })
		@Override
		public Tree write(Serializable in) {
			Map<String, Tree> header = new HashMap<String, Tree>();
			header.put(TYPE, new Tree(in.getClass().getCanonicalName()));
			header.put(OBJECT, in.serializer().write(in));
			return new Serializers.Map<String, Tree>(Serializers.Serializer_String, Serializers.Serializer_Tree).write(header);
		}
	};

	public Serializer<T> serializer();
}
