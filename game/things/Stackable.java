package game.things;

import game.*;

import java.util.*;

public abstract class Stackable extends AbstractGameThing {
	private int amount;

	abstract public Object type();

	public Stackable(GameWorld w, int a){
		super(w);
		amount = a;
	}

	public Stackable(GameWorld w){
		this(w, 1);
	}

	public int amount(){
		return amount;
	}

	public int amount(int s){
		return amount = s;
	}

	public Map<String, String> info(){
		Map<String, String> out = new HashMap<String, String>(super.info());
		out.put("stackcount", String.valueOf(amount));
		return out;
	}
}
