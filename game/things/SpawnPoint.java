package game.things;

import game.*;

import serialization.*;

public class SpawnPoint extends AbstractGameThing {
	public static void makeSerializer(SerializerUnion<GameThing> union, final GameWorld world){
		union.addIdentifier(new SerializerUnion.Identifier<GameThing>(){
			public String type(GameThing g){
				return g instanceof SpawnPoint? "spawnpoint" : null;
			}
		});

		union.addSerializer("spawnpoint", new Serializer<GameThing>(){
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

	public String renderer(){
		return "spawn_point";
	}

	public String name(){
		return "Spawn Point";
	}
	
	@Override
	public int renderLevel() {
		return ui.isometric.abstractions.IsoSquare.SPAWN_POINT;
	}
}
