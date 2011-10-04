package game;

import game.things.Chest;
import game.things.Player;

import java.util.LinkedList;
import java.util.List;

import serialization.Serializer;
import serialization.SerializerUnion;
import serialization.Tree;

public abstract class PickupGameThing extends AbstractGameThing {
	
	protected Location curLoc;
	protected final static List<String> interactions;
	static {
		interactions = new LinkedList<String>();
		interactions.add("pickup");
		interactions.add("drop");
	}
	

	public PickupGameThing(GameWorld w) {
		super(w);
		curLoc = location();
		// TODO Auto-generated constructor stub
	}
	
	public void interact(String name, Player who){
		if(name != null) {
			if(name.equals("pickup"))
				who.pickup(this);
			else if(name.equals("drop")){
				who.drop(this);
			}
		}
	}
	

}
