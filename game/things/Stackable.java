package game.things;

import game.*;

import java.util.*;

public abstract class Stackable extends PickupGameThing {
	private int amount;

	abstract public Object type();

	public Location location(Location l){
		if(l != LocationS.NOWHERE)
			for(GameThing g : l.contents())
				if(g instanceof Stackable && ((Stackable)g).merge(this)){
					LocationS.NOWHERE.put(this);
					forget();
					return super.location(LocationS.NOWHERE);
				}
		return super.location(l);
	}

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

	public int add(int s){
		return amount(amount() + s);
	}

	public int subtract(int s){
		return add(-s);
	}

	public boolean merge(Stackable s){
		if(s.type() == type()){
			amount(amount() + s.amount());
			return true;
		}
		return false;
	}

	public Map<String, String> info(){
		Map<String, String> out = new HashMap<String, String>(super.info());
		out.put("stackcount", String.valueOf(amount));
		return out;
	}
}
