package game.things;

import game.*;
import util.*;
import serialization.*;

import java.util.*;

/**
 * 
 * @author zerzoumax
 *
 */
public class GroundTile extends AbstractGameThing {
	public static void makeSerializer(SerializerUnion<GameThing> union, final GameWorld world){
		union.addIdentifier(new SerializerUnion.Identifier<GameThing>(){
			public String type(GameThing g){
				return g instanceof GroundTile? "groundtile" : null;
			}
		});

		union.addSerializer("groundtile", new Serializer<GameThing>(){
			public Tree write(GameThing o){
				GroundTile in = (GroundTile)o;
				Tree out = new Tree();
				out.add(new Tree.Entry("type", new Tree(in.renderer)));
				out.add(new Tree.Entry("block", Serializers.Serializer_Boolean.write(in.willBlock)));
				return out;
			}

			public GameThing read(Tree in) throws ParseException {
				return new GroundTile(world, in.find("type").value(), Serializers.Serializer_Boolean.read(in.find("block")));
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
		if(inter != null) {
			if(inter.equals("walk here")){
				if(location() instanceof Level.Location)
					who.moveTo((Level.Location)location());
			}
		}
	}

	@Override
	public boolean canWalkInto(Direction d, Character who){
		return !willBlock;
	}

	public String renderer(){
		return renderer;
	}
	
	@Override
	public int renderLevel() {
		return ui.isometric.abstractions.IsoSquare.GROUND;
	}
}
