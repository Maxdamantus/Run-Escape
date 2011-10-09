package game.things;

import game.Container;
import game.GameWorld;
import game.Location;
import game.things.EquipmentGameThing.Slot;

import java.util.LinkedList;
import java.util.List;

public class Key extends PickupGameThing {

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
	
	public String doorcode(){
		return doorcode;
	}

}
