package game.things;

import game.*;
import util.*;
import serialization.*;

import java.util.*;

public class GroundTile extends AbstractGameThing {
	static {
		ThingsS.UNION.addIdentifier(new SerializerUnion.Identifier<GameThing>(){
			public String type(GameThing g){
				return g instanceof GroundTile? "groundtile" : null;
			}
		});

		ThingsS.UNION.addSerializer("groundtile", new Serializer<GameThing>(){
			public Tree write(GameThing o){
				GroundTile in = (GroundTile)o;
				Tree out = new Tree();
				out.add(new Tree.Entry("type", new Tree(in.renderer)));
				out.add(new Tree.Entry("block", Serializers.Serializer_Boolean.write(in.willBlock)));
				return out;
			}

			public GameThing read(Tree in){
				// arghoaehdaoet
				return new GroundTile(ThingsS.WORLD, in.find("type").value(), Serializers.Serializer_Boolean.read(in.find("block")));
			}
		});
	}

	private final String renderer;
	private final boolean willBlock;
	private final List<String> interactions = new LinkedList<String>();

	public GroundTile(GameWorld world, String name, boolean block){
		super(world);
		renderer = name;
		willBlock = block;
		if(!willBlock)
			interactions.add("walk here");
		update();
	}

	public GroundTile(GameWorld world, String name){
		this(world, name, false);
	}

	public GroundTile(GameWorld world){
		this(world, "ground_grey_1");
	}

	public List<String> interactions(){
		return interactions;
	}

	public String name(){
		return "Ground";
	}

	public void interact(String inter, Player who){
		if(inter.equals("walk here")){
			if(location() instanceof Level.Location)
				who.moveTo((Level.Location)location());
		}
	}

	public boolean canWalkInto(Direction d, Player who){
		return !willBlock;
	}

	public String renderer(){
		return renderer;
	}
}
