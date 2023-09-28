package game.things;

import game.GameThing;
import game.GameWorld;

import serialization.ParseException;
import serialization.Serializer;
import serialization.SerializerUnion;
import serialization.Tree;

/**
 * A type of PickupGameThing for unlocking respective doors, has
 * a doorcode which corresponds to a specific/many doors.
 * @author wheelemaxw
 *
 */
public class Key extends PickupGameThing {
	
	/**
	 * Custom serializer for Keys
	 * @param union
	 * @param world
	 */
	public static void makeSerializer(SerializerUnion<GameThing> union, final GameWorld world){
		union.addIdentifier(new SerializerUnion.Identifier<GameThing>(){
			public String type(GameThing g){
				return g instanceof Key? "Key" : null;
			}
		});

		union.addSerializer("Key", new Serializer<GameThing>(){
			public Tree write(GameThing o){
				Key in = (Key)o;
				Tree out = new Tree();
				out.add(new Tree.Entry("renderer", new Tree(in.renderer)));
				out.add(new Tree.Entry("doorcode", new Tree(in.doorcode)));
				return out;
			}

			public GameThing read(Tree in) throws ParseException {
				return new Key(world,
					in.find("renderer").value(),
					in.find("doorcode").value());
			}
		});
	}
	
	private String name, renderer, doorcode;
	
	public Key(GameWorld w, String ren, String drcd){
		super(w);
		name = "Key";
		this.renderer = ren;
		doorcode = drcd;
	}
	
	@Override
	public String renderer() {
		return renderer;
	}

	@Override
	public String name() {
		return name;
	}
	
	/**
	 * Getter
	 * @return doorcode associated with this key
	 */
	public String doorcode(){
		return doorcode;
	}
	
	/**
	 * Setter
	 * @param set - new doorcode
	 */
	public void setDoorcode(String set){
		doorcode = set;
	}

}
