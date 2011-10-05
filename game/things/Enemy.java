package game.things;

import game.*;

import java.util.*;

import serialization.*;

public class Enemy extends Character {
	public static void makeSerializer(final SerializerUnion<GameThing> union, final GameWorld world){
		union.addIdentifier(new SerializerUnion.Identifier<GameThing>(){
			public String type(GameThing g){
				return g instanceof Enemy? "enemy" : null;
			}
		});

		union.addSerializer("enemy", new Serializer<GameThing>(){
			public Tree write(GameThing o){
				Enemy in = (Enemy)o;
				Tree out = new Tree();
				out.add(new Tree.Entry("type", new Tree(in.type())));
				out.add(new Tree.Entry("name", new Tree(in.name)));
				return out;
			}

			public GameThing read(Tree in){
				return new Enemy(world,
					in.find("type").value(),
					in.find("name").value());
			}
		});
	}

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
