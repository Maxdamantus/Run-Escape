package game;

import serialization.*;

public class ThingsS {
	public final static SerializerUnion<GameThing> UNION = new SerializerUnion<GameThing>();
	public final static Serializer<GameThing> SERIALIZER = UNION.serializer();
}
