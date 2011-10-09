package game.things;

import game.*;

import java.util.*;

public class ShopKeeper extends Character {
	private final Map<String, Container> parts = new HashMap<String, Container>();
	private final String name;

	public ShopKeeper(GameWorld world, String r, String n){
		super(world, r);
		name = n;
		update();
	}

	public String name(){
		return name;
	}

	public Container addPart(String name){
		Container c = new Container(world());
		parts.put(name, c);
		return c;
	}

	public List<String> interactions(){
		List<String> out = new LinkedList<String>();
		for(String n : parts.keySet())
			out.add("buy " + n);
		out.addAll(super.interactions());
		return out;
	}

	public void interact(String name, Player who){
		for(Map.Entry<String, Container> kv : parts.entrySet())
			if(name.equals("buy " + kv.getKey()))
				who.showContainer(kv.getValue(), "Buying " + kv.getKey());
		super.interact(name, who);
	}
}