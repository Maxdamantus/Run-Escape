package game.things;

import game.*;

import java.util.*;

import serialization.*;

public class EquipmentGameThing extends PickupGameThing {
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
			out.add("examine");
			return out;
		}
		return super.interactions();
	}
	
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
	
	public Slot slot(){
		return this.slottype;
	}

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
	
	public int attack(){
		return attack;
	}
	
	public int strength(){
		return strength;
	}
	
	public int defence(){
		return defence;
	}
	
	public int delay(){
		return delay;
	}

	public int[] getStats() {
		int[] returnarray  = {attack,strength,defence,delay};
		return returnarray;
	}
}
