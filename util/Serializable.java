package util;

import serialization.Serializer;
import serialization.Tree;

public interface Serializable<T> {
	@SuppressWarnings("rawtypes")
	Serializer<Serializable> serializer = new Serializer<Serializable>() {
		@Override
		public Serializable read(Tree in) {
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Tree write(Serializable in) {
			return in.serializer().write(in);
		}
	};

	public Serializer<T> serializer();
}
