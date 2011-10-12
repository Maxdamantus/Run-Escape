package game.things;

import java.util.List;

import serialization.ParseException;
import serialization.Serializer;
import serialization.SerializerUnion;
import serialization.Serializers;
import serialization.Tree;
import game.*;

/**
 * Potion is one of the 2 stackable items in the game,
 * Each potion can be drunk once, which heals the Player and
 * destroys the Potion.
 * @author wheelemaxw
 *
 */

public class Potion extends Stackable {
	
	/**
	 * Custom serializer for Potion
	 * @param union
	 * @param world
	 */
	public static void makeSerializer(SerializerUnion<GameThing> union, final GameWorld world){
		union.addIdentifier(new SerializerUnion.Identifier<GameThing>(){
			public String type(GameThing g){
				return g instanceof Potion? "Potion" : null;
			}
		});

		union.addSerializer("Potions", new Serializer<GameThing>(){
			public Tree write(GameThing o){
				Potion in = (Potion)o;
				Tree out = new Tree();
				out.add(new Tree.Entry("amount", Serializers.Serializer_Integer.write(in.amount())));
				return out;
			}

			public GameThing read(Tree in) throws ParseException {
				return new Potion(world,Serializers.Serializer_Integer.read(in.find("amount")));
			}
			
		});
	}
		
	private final static Object type = new Object();

	public Potion(GameWorld w, int amt){
		super(w, amt);
	}

	public Object type(){
		return type;
	}
	
	/**
	 * Returns list of available interactions
	 * Can only ber drunk if in the inventory
	 */
	public List<String> interactions(){
		List<String> out = super.interactions();
		Location ml = location();
		if(ml instanceof Container && ((Container)ml).owner() instanceof Player){
			out.add("drink");
		}
		return out;
	}
	
	/**
	 * Calls appropriate interaction in Player. 
	 * If drunk destroys the object
	 */
	public void interact(String name, Player who){
		if(name.equals("drink")){
			who.drink(this);
			location(LocationS.NOWHERE);
			world().forget(this);
		}
		else
			super.interact(name, who);
	}

	/**
	 * If you have more than one, they are Potions,
	 * not Potion
	 */
	public String name(){
		return amount() != 1? "Potions" : "Potion";
	}

	/**
	 * Static renderer
	 */
	public String renderer(){
		return "potion";
	}

	@Override
	public int renderLevel(){
		return ui.isometric.abstractions.IsoSquare.DROP;
	}
}