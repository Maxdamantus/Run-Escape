package game;

import game.things.*;

import serialization.*;

public class ThingsS {
	public static Serializer<GameThing> makeSerializer(GameWorld w){
		SerializerUnion<GameThing> u = new SerializerUnion<GameThing>();
		Door.makeSerializer(u, w);
		Player.makeSerializer(u, w);
		Wall.makeSerializer(u, w);
		GroundTile.makeSerializer(u, w);
		SpawnPoint.makeSerializer(u, w);
		TestPickUp.makeSerializer(u, w);
		Enemy.makeSerializer(u, w);

		return u.serializer();
	}	
}
