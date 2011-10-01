package game.things;

import game.*;

import serialization.*;

public class Wall extends AbstractGameThing {
	static {
		ThingsS.UNION.addIdentifier(new SerializerUnion.Identifier<GameThing>(){
			public String type(GameThing g){
				return g instanceof Wall? "wall" : null;
			}
		});

		ThingsS.UNION.addSerializer("wall", new Serializer<GameThing>(){
			public Tree write(GameThing o){
				Wall in = (Wall)o;
				Tree out = new Tree();
				out.add(new Tree.Entry("type", new Tree(in.renderer)));
				return out;
			}

			public GameThing read(Tree in){
				// arghoaehdaoet
				return new Wall(ThingsS.WORLD, in.find("type").value());
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
}
