package game.things;

import game.*;

import serialization.*;

import java.util.*;

public class Player extends Character {
	public static void makeSerializer(final SerializerUnion<GameThing> union, final GameWorld world){
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
				out.add(new Tree.Entry("inventory", Container.serializer(union.serializer(), world).write(in.inventory)));
				return out;
			}

			public GameThing read(Tree in){
				return new Player(world,
					in.find("type").value(),
					in.find("name").value(),
					LocationS.s(world).read(in.find("location")),
					Container.serializer(union.serializer(), world).read(in.find("inventory")));
			}
		});
	}

	private Location lastLocation;
	private String type;
	private int health;
	private final String name;
	private final static int WALKDELAY = 50;
	private final Container inventory;

	private Player(GameWorld world, String t, String n, Location spawn, Container inv){
		super(world, t);
		type = t;
		name = n;
		world.setPlayer(n, this);
		lastLocation = spawn != null? spawn : LocationS.NOWHERE;
		update();
		health(1000);
		inventory = inv;
	//	world.schedule(blah, 1000);
	}

	public Player(GameWorld world, String t, String n, Location spawn){
		this(world, t, n, spawn, new Container(world));
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
		else if(name.equals("_showinventory") && who == this)
			world().emitShowContainer(inventory, "Inventory");
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

	public void pickup(final GameThing g){
		System.out.println("pickup(" + g + ")");
		if(!moveTo(g.location(), new Runnable(){
			public void run(){
				inventory.put(g);
				//for testing
				for(GameThing gt : inventory.contents()){
					System.out.println(gt.name());
				}
			}
		})){
			// printing to the wrong place
			System.out.println("I can't reach that");
		}
	}
	
	/**
	 * For this player to receive an item from another player or npc
	 * @param g GameThing to receive
	 */
	public void receiveItem(final GameThing g){
		System.out.println("received(" + g + ")");
		inventory.put(g);
		//for testing
		for(GameThing gt : inventory.contents()){
			System.out.println(gt.name());
		}
	}
	
	public void drop(GameThing g){
		if(g.location() == inventory)
			location().put(g);
	}

	public void equip(EquipmentGameThing equipmentGameThing) {
		// TODO Auto-generated method stub
		
	}

	public void examine(AbstractGameThing abstractGameThing) {
		// TODO
		System.out.println("You examined it");
	}
}
