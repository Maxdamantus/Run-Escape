package game;

import game.things.Player;

import java.util.LinkedList;
import java.util.List;

public class EquipmentGameThing extends PickupGameThing {
	public static enum Type {
		WEAPON, ARMOUR;
	}

	private int attack, strength, defence, delay;

	public EquipmentGameThing(GameWorld w, int a, int s, int d, int e, String renderer, String state){
		super(w);
		attack = a;
		strength = s;
		defence = d;
		delay = e;
	}

	public List<String> interactions(){
		List<String> out = new LinkedList<String>(super.interactions());
		out.add("equip");
		return out;
	}
	
	public void interact(String name, Player who){
		if(name.equals("equip"))
			who.equip(this);
		else
			super.interact(name, who);
	}
}
