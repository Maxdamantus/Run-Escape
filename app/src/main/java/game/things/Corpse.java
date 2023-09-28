package game.things;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import serialization.ParseException;
import serialization.Serializer;
import serialization.SerializerUnion;
import serialization.Tree;

import game.AbstractGameThing;
import game.Containable;
import game.Container;
import game.GameThing;
import game.GameWorld;
import game.Level;
import game.Location;

/**
 * 
 * @author wheelemaxw
 *
 *Corpses are created on the death of a player or enemy NPC, with supplied container
 *Players drop empty corpses, but NPC's drop their inventory.
 *
 */
public class Corpse extends AbstractGameThing implements Containable {

	/**
	 * Custom serializers for Corpse
	 * @param union
	 * @param world
	 */
	public static void makeSerializer(final SerializerUnion<GameThing> union, final GameWorld world){
		union.addIdentifier(new SerializerUnion.Identifier<GameThing>(){
			public String type(GameThing g){
				return g instanceof Corpse? "Corpse" : null;
			}
		});

		union.addSerializer("Corpse", new Serializer<GameThing>(){
			public Tree write(GameThing o){
				Corpse in = (Corpse)o;
				Tree out = new Tree();
				out.add(new Tree.Entry("name", new Tree(in.renderer)));
				out.add(new Tree.Entry("contents", Container.serializer(union.serializer(), world).write(in.contents)));
				return out;
			}

			public GameThing read(Tree in) throws ParseException {
				return new Corpse(world,
					in.find("name").value(),
					Container.serializer(union.serializer(), world).read(in.find("contents")));
			}
		});
	}
	
	private final String renderer;
	private final Container contents;
	
	
	public Corpse(GameWorld world, String name, Container cont){
		super(world);
		renderer = name;
		if(cont != null){
			this.contents = cont;
		}
		else{
			this.contents = new Container(world);
		}
		update();
	}
	
	@Override
	public int renderLevel() {
		return ui.isometric.abstractions.IsoSquare.FURNATURE;
	}
		
	@Override
	public Map<String, Container> getContainers() {
		Map<String,Container> returnmap = new HashMap<String,Container>();
		if(contents != null){
			returnmap.put("contents", contents);
		}
		return returnmap;
	}

	@Override
	public String renderer() {
		return renderer;
	}
	
	/**
	 * @return List of available interactions
	 */
	public List<String> interactions(){
		ArrayList<String> interactions = new ArrayList<String>();
		interactions.add("loot body");
		interactions.add("examine");
		return interactions;
			
	}
	
	/**
	 * Defines the interactions for looting the body
	 * (is essentially the same as opening an "Openable Furniture"
	 */
	public void interact(String name, final game.things.Player who){
		if(name.equals("loot body")){
			Location l = location();
			if(l instanceof Level.Location){
				who.moveTo((Level.Location)l, 1, new Runnable(){
					public void run(){
						update();
						who.showContainer(contents, renderer);
					}
				});
			
			}
		}
	}
	

	@Override
	public String name() {
		return "Corpse";
	}

}
