package game.things;

import game.*;

import serialization.*;

public class SpawnPoint extends AbstractGameThing {
	public static void makeSerializer(SerializerUnion<GameThing> union, final GameWorld world){
		union.addIdentifier(new SerializerUnion.Identifier<GameThing>(){
			public String type(GameThing g){
				return g instanceof Wall? "spawnpoint" : null;
			}
		});

		union.addSerializer("wall", new Serializer<GameThing>(){
			public Tree write(GameThing o){
				return new Tree();
			}

			public GameThing read(Tree in){
				return new SpawnPoint(world);
			}
		});
	}
	
	public SpawnPoint(GameWorld world){
		super(null);
		world.addSpawnPoint(this);
	}

	public SpawnPoint(){
		super(null);
		System.out.println("Err, wrong interface");
	}

	public String renderer(){
		return "spawn_point";
	}

	public String name(){
		return "Spawn Point";
	}
}
