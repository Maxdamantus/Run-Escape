package game.things;

import game.*;

import java.util.*;

import serialization.*;

/**
 * Extension of a pickupable GameThing, which can be equiped to the
 * player and gives them additions to their stats and if a Sword,
 * changes their renderer.
 * @author wheelemaxw
 *
 */
public class EquipmentGameThing extends PickupGameThing {
	
	/**
	 * Custom serializers for EquipmentGameThing
	 * @param union
	 * @param world
	 */
	public static void makeSerializer(SerializerUnion<GameThing> union, final GameWorld world){
		union.addIdentifier(new SerializerUnion.Identifier<GameThing>(){
			public String type(GameThing g){
				return g instanceof EquipmentGameThing? "equipment" : null;
			}
		});

		union.addSerializer("equipment", new Serializer<GameThing>(){
			public Tree write(GameThing o){
				EquipmentGameThing in = (EquipmentGameThing)o;
				Tree out = new Tree();
				out.add(new Tree.Entry("attack", Serializers.Serializer_Integer.write(in.attack)));
				out.add(new Tree.Entry("strength", Serializers.Serializer_Integer.write(in.strength)));
				out.add(new Tree.Entry("defence", Serializers.Serializer_Integer.write(in.defence)));
				out.add(new Tree.Entry("delay", Serializers.Serializer_Integer.write(in.delay)));
				out.add(new Tree.Entry("slot", new Tree(in.slottype.toString())));
				out.add(new Tree.Entry("name", new Tree(in.name)));
				out.add(new Tree.Entry("renderer", new Tree(in.renderer)));
				return out;
			}

			public GameThing read(Tree in) throws ParseException {
				return new EquipmentGameThing(world,
					Serializers.Serializer_Integer.read(in.find("attack")),
					Serializers.Serializer_Integer.read(in.find("strength")),
					Serializers.Serializer_Integer.read(in.find("defence")),
					Serializers.Serializer_Integer.read(in.find("delay")),
					Slot.valueOf(in.find("slot").value()),
					in.find("name").value(),
					in.find("renderer").value());
			}
		});
	}

	/**
	 * Enum for the different slot types, for use when
	 * equiped/de-equiping
	 */
	public static enum Slot {
		WEAPON, ARMOUR, SHIELD, GAUNTLET, BOOTS, HELMET, ACCESSORY;
	}

	private int attack, strength, defence, delay;
	private String name, renderer;
	private Slot slottype;
	public static final String SLOT = "slot";

	public EquipmentGameThing(GameWorld w, int a, int s, int d, int e, Slot sl, String nom, String ren/*, boolean eq*/){
		super(w);
		attack = a;
		strength = s;
		defence = d;
		delay = e;
		name = nom;
		slottype = sl;
		this.renderer = ren;
		
	}

	/**
	 * If the equipment is in a container, it is checked if the item is in the 
	 * Players equipment, and if so given an interaction to Unequip
	 * Otherwise, if it is just in a player inventory, you can Equip
	 * Then adds superclasses
	 * @return A list of possible interactions, adding on to the superclass's (in ths case an empty list.)
	 */
	public List<String> interactions(){
		if(location() instanceof Container){
			List<String> out = new LinkedList<String>();
			Location ml = location();
			if(ml instanceof Container && ((Container)ml).owner() != null && ((Container)ml).owner().equipped(this)){
				out.add("unequip");
			}
			else if(ml instanceof Container && ((Container)ml).owner() instanceof Player){
				out.add("equip");
			}
			out.addAll(super.interactions());
			return out;
		}
		return super.interactions();
	}
	
	/**
	 * Calls the appropriate interaction method in the Player
	 */
	public void interact(String name, Player who){
		if(name.equals("equip") && who.carrying(this)){
			who.equip(this);
		}
		else if(name.equals("unequip") && who.equipped(this)){
			who.unequip(this);
		}
		else{
			super.interact(name, who);
		}
	}
	
	/**
	 *Getter
	 * @return Slottype
	 */
	public Slot slot(){
		return this.slottype;
	}

	/**
	 * Info for DumbGameThing, but added Slottype
	 */
	public Map<String, String> info(){
		Map<String, String> map = new HashMap<String, String>(super.info());
		map.put(SLOT, slottype.toString());
		return map;
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
	 * @return Attack stat
	 */
	public int attack(){
		return attack;
	}
	
	/**
	 * 
	 * @return Attack stat
	 */
	public int strength(){
		return strength;
	}
	
	/**
	 * Getter
	 * @return Attack stat
	 */
	public int defence(){
		return defence;
	}
	
	/**
	 * Getter
	 * @return Attack stat
	 */
	public int delay(){
		return delay;
	}

	/**
	 * Getter for all the stats
	 * @return ArrayList of the stats
	 */
	public int[] getStats() {
		int[] returnarray  = {attack,strength,defence,delay};
		return returnarray;
	}
}
