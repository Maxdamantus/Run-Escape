package game.things;

import game.*;

public class Valuable extends PickupGameThing {
	private int value;
	private String name, renderer;

	public Valuable(GameWorld w, String n, String r, int v){
		super(w);
		value = v;
		name = n;
		renderer = r;
		update();
	}

	public String name(){
		return name;
	}

	public String renderer(){
		return renderer;
	}

	public int value(){
		return value;
	}
}
