package game.things;

import game.AbstractGameThing;

import java.util.*;

import serialization.*;

import util.Direction;
import game.*;

/**
 * Extension of AbstractGameThing for displaying furniture, which
 * are only examineable, and cannot be walked through
 * @author wheelemaxw
 *
 */
public class Furniture extends AbstractGameThing {
	
	/**
	 * Custom serializer for Furniture
	 * @param union
	 * @param world
	 */
	public static void makeSerializer(final SerializerUnion<GameThing> union, final GameWorld world){
		union.addIdentifier(new SerializerUnion.Identifier<GameThing>(){
			public String type(GameThing g){
				return g instanceof Furniture? "Furniture" : null;
			}
		});
		
		union.addSerializer("Furniture", new Serializer<GameThing>(){
			public Tree write(GameThing o){
				Furniture in = (Furniture)o;
				Tree out = new Tree();
				out.add(new Tree.Entry("type", new Tree(in.renderer)));

			
				
				return out;
			}

			public GameThing read(Tree in) throws ParseException {
				return new Furniture(world, in.find("type").value());
			}
		});
	}
		
	private final String renderer;


	
	//For reading in (serializer to be completed)
	public Furniture(GameWorld world, String name){
		super(world);
		renderer = name;
		update();
	}

	/**
	 * Getter
	 * @return The renderer
	 */
	public String renderer(){
		return renderer;
	}

	
	/**
	 * Returns a list of possible interactions
	 */
	public List<String> interactions(){
		ArrayList<String> interactions = new ArrayList<String>();
		interactions.add("examine");
		return interactions;
			
	}

	/**
	 * Getter for the name of the object(in this case just the renderer)
	 */
	public String name(){
		return renderer;
	}
	
	/**
	 * Calls super, as this has no special interactions
	 */
	public void interact(String name, game.things.Player who){
		super.interact(name, who);
	}
	
	@Override
	public boolean canWalkInto(Direction d, Character p) {
		return false;
	}
	
	@Override
	public int renderLevel() {
		return ui.isometric.abstractions.IsoSquare.FURNATURE;
	}
}