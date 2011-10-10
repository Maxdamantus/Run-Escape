package game.things;

import game.*;

import java.util.*;

import serialization.*;

public class ShopItem extends Stackable {
	private final Serializer<GameThing> reader;
	private final Tree tree;
	private final DumbGameThing prototype;
	private int cost;

	public ShopItem(GameWorld w, GameThing g, int a){
		super(w, a);
		reader = ThingsS.makeSerializer(w);
		tree = reader.write(g);
		prototype = new DumbGameThing(g);
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
				try{
					who.inventory().put(reader.read(tree));
				}catch(ParseException e){
					throw new RuntimeException("wtf");
				}
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
