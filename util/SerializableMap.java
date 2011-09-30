package util;

import java.util.*;

import serialization.Serializer;
import serialization.Serializers;

public class SerializableMap<K, V> extends HashMap<K, V> implements Serializable<Map<K, V>> {
	private static final long serialVersionUID = 1L;
	
	private Serializer<K> keyS;
	private Serializer<V> valueS;
	
	public SerializableMap(Serializer<K> k, Serializer<V> v) {
		keyS = k;
		valueS = v;
	}
	
	@Override
	public Serializer<Map<K, V>> serializer() {
		return new Serializers.Map<K, V>(keyS, valueS);
	}
}
