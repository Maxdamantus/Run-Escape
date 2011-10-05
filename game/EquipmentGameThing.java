package game;

import game.things.Player;

import java.util.LinkedList;
import java.util.List;

public class EquipmentGameThing extends PickupGameThing {

	public EquipmentGameThing(GameWorld w) {
		super(w);
		// TODO Auto-generated constructor stub
	}

	public List<String> interactions(){
		List<String> out = new LinkedList<String>();
		// assuming if it's not in a level, it must be droppable.
		out.add(location() instanceof Level.Location? "pick up" : "drop");
		return out;
	}
	
	public void interact(String name, Player who){
		if(name.equals("pick up"))
			who.pickup(this);
		else if(name.equals("drop")){
			who.drop(this);
		}
		else if(name.equals("equip")){
			who.equip(this);
		}
	}
}

