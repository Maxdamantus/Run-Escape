package game.things;

import game.*;

import serialization.*;

import java.util.*;

public class Enemy extends Character {
	private final String name;

	public Enemy(GameWorld world, String t, String n){
		super(world, t);
		name = n;
		update();
		health(1000);
	}

	public String name(){
		return name;
	}

	public List<String> interactions(){
		List<String> out = new LinkedList<String>(super.interactions());
		out.add("follow");
		out.add("attack");
		return out;
	}

	public void interact(String name, Player who){
		if(name.equals("follow"))
			who.follow(this);
		else if(name.equals("attack"))
			who.attack(this);
		else super.interact(name, who);
	}

	public void damage(int amt, Character from){
		super.damage(amt, from);
		attack(from);
		if(health() <= 0){
			LocationS.NOWHERE.put(this);
			forget();
		}
	}
}
