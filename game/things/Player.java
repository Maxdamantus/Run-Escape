package game.things;

import game.*;

import serialization.*;

import java.util.*;

public class Player extends Character {
	public static void makeSerializer(SerializerUnion<GameThing> union, final GameWorld world){
		union.addIdentifier(new SerializerUnion.Identifier<GameThing>(){
			public String type(GameThing g){
				return g instanceof Player? "player" : null;
			}
		});

		union.addSerializer("player", new Serializer<GameThing>(){
			public Tree write(GameThing o){
				Player in = (Player)o;
				Tree out = new Tree();
				out.add(new Tree.Entry("type", new Tree(in.type)));
				out.add(new Tree.Entry("name", new Tree(in.name)));
				out.add(new Tree.Entry("location", LocationS.s(null).write(in.lastLocation == null? in.location() : in.lastLocation)));
				return out;
			}

			public GameThing read(Tree in){
				return new Player(world, in.find("type").value(), in.find("name").value(), LocationS.s(world).read(in.find("location")));
			}
		});
	}

	private Location lastLocation;
	private String type;
	private int health;
	private final String name;
	private final static int WALKDELAY = 50;
	private final Container inventory;

	public Player(GameWorld world, String t, String n, Location spawn){
		super(world, t);
		type = t;
		name = n;
		world.setPlayer(n, this);
		lastLocation = spawn != null? spawn : LocationS.NOWHERE;
		update();
		health(1000);
		inventory = new Container(world);
	//	world.schedule(blah, 1000);
	}
/*
	private Runnable blah = new Runnable(){
		public void run(){
			world().schedule(blah, 5000);
		}
	};
	*/

	public Player(GameWorld world, String renderer, String n){
		this(world, renderer, n, spawnPointWorkAroundCrap(world.getSpawnPoint()));
	}

	private static Location spawnPointWorkAroundCrap(SpawnPoint sp){
		return sp != null? sp.location() : null;
	}

	public Player(GameWorld world){
		this(world, "cordi");
	}

	public Player(GameWorld world, String t){
		this(world, t, "<insert name here>");
	}

	public void logout(){
		lastLocation = location();
		LocationS.NOWHERE.put(this);
	}

	public void login(){
		lastLocation.put(this);
		lastLocation = null;
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
	}
	
	public void pickup(GameThing g){
		inventory.put(g);
		//for testing
		for(GameThing gt : inventory.contents()){
			System.out.println(gt.name());
		}
	}

	public void damage(int amt, Character from){
		super.damage(amt, from);
		if(health() <= 0){
			SpawnPoint sp = world().getSpawnPoint();
			if(sp != null)
				sp.location().put(this);
			health(1000);
		}
	}
	
	public void drop(GameThing g){
		inventory.remove(g);
		g.location(this.location());
	}
	
}
