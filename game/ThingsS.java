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
		EquipmentGameThing.makeSerializer(u, w);
		OpenableFurniture.makeSerializer(u, w);
		ChattyNPC.makeSerializer(u, w);
		Key.makeSerializer(u, w);
		return u.serializer();
	}	

	public ShopItem.Generator<GameThing> makeGenerator(GameThing g){
		final Serializer<GameThing> ser = makeSerializer(g.world());
		final Tree tree = ser.write(g);
		return new ShopItem.Generator<GameThing>(){
			public GameThing create(){
				try{
					return ser.read(tree);
				}catch(ParseException e){
					throw new RuntimeException("wtf");
				}
			}
		};
	}
}
