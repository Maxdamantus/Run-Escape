package util;

import serialization.Serializer;
import serialization.Serializers;

public class SerializableInteger extends Number implements Serializable<Integer> {
	private static final long serialVersionUID = 1L;
	
	private int value;
	
	public SerializableInteger(int val) {
		value = val;
	}
	
	@Override
	public Serializer<Integer> serializer() {
		return Serializers.Serializer_Integer;
	}

	@Override
	public int intValue() {
		return value;
	}

	@Override
	public long longValue() {
		return value;
	}

	@Override
	public float floatValue() {
		return value;
	}

	@Override
	public double doubleValue() {
		return value;
	}

}
