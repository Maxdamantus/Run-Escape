package game.things;

import game.*;

import java.util.*;

public class ShopItem extends Stackable {
	public static interface Generator<T> {
		public T create();
	}

	private final DumbGameThing prototype;
	private final Generator<? extends GameThing> generator;
	private int cost;

	public ShopItem(GameWorld w, Generator<? extends GameThing> g, int a){
		super(w, a);
		generator = g;
		GameThing t = g.create();
		prototype = new DumbGameThing(t);
		LocationS.NOWHERE.put(t);
		t.forget();
	}

	public int cost(){
		return cost;
	}

	public int cost(int s){
		return cost = s;
	}

	private final Object type = new Object();
	public Object type(){
		return type;
	}

	public List<String> interactions(){
		List<String> out = new LinkedList<String>();
		out.add("buy");
		out.add("value");
		out.addAll(super.interactions());
		return out;
	}

	public void interact(String name, Player who){
		if(name.equals("value"))
			System.out.println(prototype.name() + " costs " + cost + " gold");
		else if(name.equals("buy")){
			boolean okay = cost == 0;
			if(!okay)
				for(GameThing gt : who.inventory())
					if(gt instanceof Coins && ((Coins)gt).amount() > cost){
						((Coins)gt).subtract(cost);
						okay = true;
						break;
					}
			if(okay){
				who.inventory().put(generator.create());
				update();
			}
		}
	}

	public String name(){
		return prototype.name();
	}

	public String renderer(){
		return prototype.renderer();
	}
}
