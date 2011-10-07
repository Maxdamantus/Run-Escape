package game.things;

import game.*;

import serialization.*;
import util.Direction;

public class Wall extends AbstractGameThing {
	public static void makeSerializer(SerializerUnion<GameThing> union, final GameWorld world){
		union.addIdentifier(new SerializerUnion.Identifier<GameThing>(){
			public String type(GameThing g){
				return g instanceof Wall? "wall" : null;
			}
		});

		union.addSerializer("wall", new Serializer<GameThing>(){
			public Tree write(GameThing o){
				Wall in = (Wall)o;
				Tree out = new Tree();
				out.add(new Tree.Entry("type", new Tree(in.renderer)));
				return out;
			}

			public GameThing read(Tree in) throws ParseException {
				return new Wall(world, in.find("type").value());
			}
		});
	}

	private final String renderer;

	public Wall(GameWorld world, String name){
		super(world);
		renderer = name;
		update();
	}
	
	public Wall(GameWorld world){
		this(world, "wallcross");
	}

	public String renderer(){
		return renderer;
	}

	public String name(){
		return "Wall";
	}
	
	@Override
	public boolean canWalkInto(Direction d, Character p) {
		return false;
	}
	
	@Override
	public int renderLevel() {
		return ui.isometric.abstractions.IsoSquare.WALL;
	}
}
