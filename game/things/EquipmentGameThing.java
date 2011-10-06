package game.things;

import game.Container;
import game.GameWorld;

import java.util.LinkedList;
import java.util.List;

public class EquipmentGameThing extends PickupGameThing {
	public static enum Slot {
		WEAPON, ARMOUR, SHIELD, GAUNTLET, BOOTS, HELMET, ACCESSORY;
	}

	private int attack, strength, defence, delay;
	private String name, renderer;
	private Slot slottype;

	public EquipmentGameThing(GameWorld w, int a, int s, int d, int e, Slot sl, String nom, String ren){
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
			List<String> out = new LinkedList<String>(super.interactions());
			out.add("equip");
			return out;
		}
		return super.interactions();
	}
	
	public void interact(String name, Player who){
		if(name.equals("equip") && who.carrying(this))
			who.equip(this);
		else
			super.interact(name, who);
	}
	
	public Slot slot(){
		return this.slottype;
	}

	@Override
	public String renderer() {
		return renderer;
	}

	@Override
	public String name() {
		return name;
	}
}
