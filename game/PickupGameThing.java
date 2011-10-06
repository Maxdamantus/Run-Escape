package game;

import game.things.Chest;
import game.things.Player;

import java.util.LinkedList;
import java.util.List;

import serialization.Serializer;
import serialization.SerializerUnion;
import serialization.Tree;

public abstract class PickupGameThing extends AbstractGameThing {
	public PickupGameThing(GameWorld w) {
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
	}

	public Location location(Location l){
		Location r = super.location(l);
		update();
		return r;
	}
	
	@Override
	public int renderLevel() {
		return ui.isometric.abstractions.IsoSquare.PICKUP_ITEM;
	}
}
