package game.things;

import game.*;
import game.things.EquipmentGameThing.Slot;

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
				out.add(new Tree.Entry("equipment", Container.serializer(union.serializer(), world).write(in.equipment)));
				out.add(new Tree.Entry("buffer", Container.serializer(union.serializer(), world).write(in.buffer)));
				return out;
			}

			public GameThing read(Tree in) throws ParseException {
				return new Player(world,
					in.find("type").value(),
					in.find("name").value(),
					LocationS.s(world).read(in.find("location")),
					Container.serializer(union.serializer(), world).read(in.find("inventory")),
					Container.serializer(union.serializer(), world).read(in.find("equipment")),
					Container.serializer(union.serializer(), world).read(in.find("buffer")));
			}
		});
	}

	private Location lastLocation;
	private String type;
	private final String name;
	private final static int WALKDELAY = 50;
	private final Container inventory;
	private final Container equipment;
	private final Container buffer;
	private boolean dead;

	private Player(GameWorld world, String t, String n, Location spawn, Container inv, Container equ, Container buf){
		super(world, t);
		type = t;
		name = n;
		world.setPlayer(n, this);
		lastLocation = spawn != null? spawn : LocationS.NOWHERE;
		health(1000);
		inventory = inv;
		equipment = equ;
		buffer = buf;
		inv.owner(this);
		equ.owner(this);
		setStats(10,10,10,10);
		update();
	//	world.schedule(blah, 1000);
	}

	public Player(GameWorld world, String t, String n, Location spawn){
		this(world, t, n, spawn, new Container(world), new Container(world), new Container(world));
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

	public void clear(){
		logout();
		lastLocation = LocationS.NOWHERE;
	}

	public void logout(){
		lastLocation = location();
		LocationS.NOWHERE.put(this);
	}

	public void login(){
		if(lastLocation == LocationS.NOWHERE){
			SpawnPoint sp = world().getSpawnPoint();
			if(sp != null)
			lastLocation = sp.location();
		}
		lastLocation.put(this);
		lastLocation = null;
	}

	public String name(){
		return name;
	}
	
	
	@Override
	public String rendererState() { // TODO: depends on equipped state
		if(dead) return "";
		if(equipment != null){
			for(GameThing gt: equipment){
				if(((EquipmentGameThing)gt).slot().equals(Slot.WEAPON)){
					return "sword";
				}
			}
		}
		return "empty";
	}

	public List<String> interactions(){
		List<String> out = new LinkedList<String>(super.interactions());
		out.add("follow");
		out.add("attack");
		out.add("examine");
		return out;
	}

	//Change to not let you follow or attack yourself, maybe put out a log message to inform of this.
	public void interact(String name, Player who){
		if(name.equals("follow"))
			who.follow(this);
		else if(name.equals("attack"))
			who.attack(this);
		else if(name.equals("_showinventory") && who == this)
			showContainer(inventory, "Inventory");
		else if(name.equals("_showequipment") && who == this)
			showContainer(equipment, "Equipment");
		else if(name.equals("_showbuffer") && who == this)
			showContainer(buffer, "Buffer");
		else
			super.interact(name, who);
	}

	public void showContainer(Container c, String n){
		world().emitShowContainer(c, n, this);
	}
	

	
	public void damage(int amt, Character from){
		super.damage(amt, from);
		world().emitSay(from, this, from.name() + " hurts " + name() + " and his health is now " + health());
		if(health() <= 0){
			final game.things.Corpse cp = new Corpse(world(),"corpse_1",null);
			cp.location(this.location());
			final Player thisp = this;
			final SpawnPoint sp = world().getSpawnPoint(location(), this);
			if(sp != null){
				world().schedule(new Runnable(){
					public void run(){
						thisp.dead(false);
						System.out.println("sp != null");
						sp.location().put(thisp);
						thisp.health(1000);
						thisp.dying(false);
						thisp.update();
					}},5000);
			}
			update();
		}
	}

	public void pickup(final GameThing g){
//		System.out.println("pickup(" + g + ")");
		if(g.location() instanceof game.Container){
			((Container)g.location()).remove(g);
			inventory.put(g);
			world().emitSay(g, this, "You pick up the "+g.name());
			
		}
		else{
			final Player temp = this;
			if(!moveTo(g.location(), new Runnable(){
				public void run(){
					inventory.put(g);
					world().emitSay(g, temp, "You pick up the "+g.name());
//					for(GameThing gt : inventory.contents()){
//						System.out.println(gt.name());
//					}
					
				}
			})){
			world().emitSay(g, this, "You can't reach that");
			}
		}
	}
	
	/**
	 * For this player to receive an item from another player or npc
	 * @param g GameThing to receive
	 */
	public void receiveItem(final GameThing g){
		world().emitSay(g, this, "You recieve "+g.name());
		inventory.put(g);
		//for testing
		for(GameThing gt : inventory.contents()){
			System.out.println(gt.name());
		}
	}
	
	public void drop(GameThing g){
		if(g.location() == inventory)
			location().put(g);
		if(g.location() == equipment)
			unequip((EquipmentGameThing) g);
			location().put(g);
		world().emitSay(g, this, "You dropped "+g.name());
	}

	public void equip(EquipmentGameThing g) {
		EquipmentGameThing gt;
		Iterator<GameThing> iter = equipment.contents().iterator();
		GameThing temp = null;
		while(iter.hasNext() && (temp = iter.next()) != null){
			if(temp instanceof EquipmentGameThing){
				gt = (EquipmentGameThing) temp;
				if(gt.slot().equals(g.slot())){
					iter.remove();
					inventory.put(gt);
				}
			}
		}
		equipment.put(g);
		addStats(g.getStats());
		update();
	}
	
	private void addStats(int[] stats){
		addStats(stats, 1);
	}

	private void addStats(int[] stats, int m) {
		attack(attack() + stats[0]*m);
		strength(strength() + stats[1]*m);
		defence(defence() + stats[2]*m);
		delay(delay() + stats[3]*m);
	}
	
	private void remStats(int[] stats) {
		addStats(stats, -1);
	}

	public void unequip(EquipmentGameThing g) {
		if(equipment.contains(g)){
			equipment.remove(g);
			inventory.put(g);	
		}
		remStats(g.getStats());
		update();
	}

	public Container buffer(){
		return buffer;
	}

	public void send(GameThing g){
		buffer.put(g);
	}

	public boolean carrying(GameThing g){
		return inventory.contains(g);
	}

	public boolean equipped(GameThing g){
		return equipment.contains(g);
	}

	public Container inventory(){
		return inventory;
	}
	
	public void drink(Potion potion) {
		if((health() + 200) <= 1000)
			health(health() + 200);
		else
			health(1000);
		update();
		world().emitSay(potion, this, "You drink the potion, it heals some of your wounds");
		potion.subtract(1);
	}

	public void examine(final AbstractGameThing g) {
		// TODO
		final Player temp = this;
		if(!(g.location() instanceof Container && (((Container)g.location()) == inventory || ((Container)g.location()) == equipment))){
			if(!moveTo(g.location(), new Runnable(){
				public void run(){
					world().emitSay(g, temp, "This is a "+g.name());
					if(g instanceof EquipmentGameThing){
						world().emitSay(g, temp, "Strength: "+((EquipmentGameThing)g).strength());
						world().emitSay(g, temp, "Attack: "+((EquipmentGameThing)g).attack());
						world().emitSay(g, temp, "Defence: "+((EquipmentGameThing)g).defence());
						world().emitSay(g, temp, "Delay: "+((EquipmentGameThing)g).delay());
					}
					if(g instanceof Key)
						world().emitSay(g, temp, "Fits the "+((Key)g).doorcode()+" door");
					if(g instanceof ShopItem)
						world().emitSay(g, temp, "Costs "+((ShopItem)g).cost()+ " coins");
					if(g instanceof ChattyNPC)
						world().emitSay(g, temp, "He looks chatty");
					if(g instanceof Enemy)
						world().emitSay(g, temp, "He looks up for a fight");	
			}
		})){
		world().emitSay(g, this, "You can't reach that");
		}
		}
		else{
			world().emitSay(g, temp, "This is a "+g.name());
			if(g instanceof EquipmentGameThing){
				world().emitSay(g, temp, "Strength: "+((EquipmentGameThing)g).strength());
				world().emitSay(g, temp, "Attack: "+((EquipmentGameThing)g).attack());
				world().emitSay(g, temp, "Defence: "+((EquipmentGameThing)g).defence());
				world().emitSay(g, temp, "Delay: "+((EquipmentGameThing)g).delay());
			}
			if(g instanceof Key)
				world().emitSay(g, temp, "Fits the "+((Key)g).doorcode()+" door");
			if(g instanceof ShopItem)
				world().emitSay(g, temp, "Costs "+((ShopItem)g).cost()+ " coins");
		}	
	}

	public Map<String, String> info(){
		Location l = location();
		if(!(l instanceof Level.Location))
			return super.info();
		Map<String, String> out = new HashMap<String, String>(super.info());
		out.put("luminance", String.valueOf(((Level.Location)l).level().luminance()));
		return out;
	}


}
