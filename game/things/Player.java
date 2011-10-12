package game.things;

import game.*;
import game.things.EquipmentGameThing.Slot;

import serialization.*;

import java.util.*;

/**
 * @author wheelemaxw and zerzoumax
 *
 *Class extending Character for the in-game representation of connected
 *clients
 *
 *Contins most of the code for interacting with objects and NPC's
 */
public class Player extends Character {
	
	/**
	 * Custom serializer for Player
	 * @param union
	 * @param world
	 */
	
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
	}

	//Legacy constructor
	public Player(GameWorld world, String t, String n, Location spawn){
		this(world, t, n, spawn, new Container(world), new Container(world), new Container(world));
	}

	//Legacy constructor
	public Player(GameWorld world, String renderer, String n){
		this(world, renderer, n, spawnPointWorkAroundCrap(world.getSpawnPoint()));
	}

	/**
	 * Getting for location of the given spawnpoint
	 * @param sp
	 * @return Location of the spawn point, 
	 */
	private static Location spawnPointWorkAroundCrap(SpawnPoint sp){
		return sp != null? sp.location() : null;
	}

	//Legacy constructor
	public Player(GameWorld world){
		this(world, "cordi");
	}

	//Legacy constructor
	public Player(GameWorld world, String t){
		this(world, t, "<insert name here>");
	}

	/**
	 * Removes player thoroughly from the world
	 * 
	 */
	public void clear(){
		logout();
		lastLocation = LocationS.NOWHERE;
	}

	/**
	 * Saves current location and then moves Player into a 'Nowhere'
	 * state.
	 */
	public void logout(){
		lastLocation = location();
		LocationS.NOWHERE.put(this);
	}

	/**
	 * Returns player to previous location. If this i nowhere
	 * then spawns them at closest spawnpoint
	 */
	public void login(){
		if(lastLocation == LocationS.NOWHERE){
			SpawnPoint sp = world().getSpawnPoint();
			if(sp != null)
			lastLocation = sp.location();
		}
		lastLocation.put(this);
		lastLocation = null;
	}

	/**
	 * Returns name of the player (usrName given on connect)
	 */
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

	/**
	 * @return Returns the list of available interactions
	 */
	public List<String> interactions(){
		List<String> out = new LinkedList<String>(super.interactions());
		out.add("follow");
		out.add("attack");
		out.add("examine");
		return out;
	}

	
	/**
	 * Calls the appropriate interaction method in the Player
	 */
	public void interact(String name, Player who){
		if(name.equals("follow"))
			who.follow(this);
		else if(name.equals("attack"))
			who.attack(this);
		else if(name.equals("_showinventory") && who == this)
			showContainer(inventory, "Inventory", false);
		else if(name.equals("_showequipment") && who == this)
			showContainer(equipment, "Equipment", false);
		else if(name.equals("_showbuffer") && who == this)
			showContainer(buffer, "Buffer", false);
		else
			super.interact(name, who);
	}

	Set<Container> sensitiveContainers = new HashSet<Container>();
	/**
	 * Emits a ShowContainer worldDelta to the player in question
	 * for the Container in question.
	 * @param c - Container to show
	 * @param n - Name of the Container
	 * @param s - Name of the Container
	 */
	public void showContainer(Container c, String n, boolean s){
		world().emitShowContainer(c, n, this);
		if(s)
			sensitiveContainers.add(c);
	}

	public void showContainer(Container c, String n){
		showContainer(c, n, true);
	}

	public void distract(){
		for(Container c : sensitiveContainers)
			world().emitHideContainer(c, this);
		sensitiveContainers.clear();
	}

	/**
	 * Method for doing damage to the player, calls Character damage
	 * method.
	 * Emits a Say WorldDelta to the player on how much they've been hurt
	 * If dead, creates new corpse and move player to nowhere until
	 * respawning them at closest spawn, return their health to full
	 */
	public void damage(int amt, Character from){
		super.damage(amt, from);
		world().emitSay(from, this, from.name() + " hurts " + name() + " and his health is now " + health());
		if(health() <= 0){
			from.stopAttack();
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

	/**
	 * If the GameThing is in a container, it removes from
	 * the container and places the GameThing in the players inventory.
	 * Otherwise requires the player to walk to the GameThing before it can be picked up.
	 * Then ends out appropriate Say delta
	 * @param g - GameThing to pick up
	 */
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
	
	/**
	 * If in the players inventory, puts the GameThing in the
	 * same location a the player.
	 * If it is equipped, it unequips first.
	 * Sends out appropriate Say delta
	 * @param g - GameThing to drop
	 */
	public void drop(GameThing g){
		if(g.location() == inventory)
			location().put(g);
		if(g.location() == equipment)
			unequip((EquipmentGameThing) g);
			location().put(g);
		world().emitSay(g, this, "You dropped "+g.name());
	}

	/**
	 * Iterates through the current equipment and checks whether
	 * there is already an item in the corresponding slot.
	 * Swaps the two if so, otherwise just removes the GameThing
	 * from the inventory, adds to equipment and updates the players
	 * stats.
	 * @param g - GameThing being Equipped
	 */
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
	
	/**
	 * Adds given stats to players
	 * @param stats
	 */
	private void addStats(int[] stats){
		addStats(stats, 1);
	}

	/**
	 * Adds or remove stats using the given int array
	 * @param stats
	 * @param m - 1 or -1 removing or adding
	 */
	private void addStats(int[] stats, int m) {
		attack(attack() + stats[0]*m);
		strength(strength() + stats[1]*m);
		defence(defence() + stats[2]*m);
		delay(delay() + stats[3]*m);
	}
	
	/**
	 * Calls addStats with -1 (takes off given stats)
	 * @param stats
	 */
	private void remStats(int[] stats) {
		addStats(stats, -1);
	}

	/**
	 * Can only be called if item is equipped.
	 * Removes stats and moves item to the inventory
	 * @param g
	 */
	public void unequip(EquipmentGameThing g) {
		if(equipment.contains(g)){
			equipment.remove(g);
			inventory.put(g);	
		}
		remStats(g.getStats());
		update();
	}

	/**
	 * A buffer that interactions on other objects may use,
	 * @return The buffer 
	 */
	public Container buffer(){
		return buffer;
	}

	/**
	 * adds a GameThing into the buffer
	 */
	public void send(GameThing g){
		buffer.put(g);
	}

	/**
	 * Essentially a contains method
	 * @param g - GameThing to check
	 * @return true if in inventory, false otherwise
	 */
	public boolean carrying(GameThing g){
		return inventory.contains(g);
	}

	/**
	 * Essentially a contains method
	 * @param g - GameThing to check
	 * @return true if in equipment, false otherwise
	 */
	public boolean equipped(GameThing g){
		return equipment.contains(g);
	}

	/**
	 * Getter
	 * @return Players inventory
	 */
	public Container inventory(){
		return inventory;
	}
	
	/**
	 * Interaction for drinking a potion,
	 * Heals user by 200 points up to max limit of 1000
	 * @param potion
	 */
	public void drink(Potion potion) {
		if((health() + 200) <= 1000)
			health(health() + 200);
		else
			health(1000);
		update();
		world().emitSay(potion, this, "You drink the potion, it heals some of your wounds");
		potion.subtract(1);
	}

	/**
	 * Different emitSays depending on type of GameThing, uses
	 * instanceofs which is a bit messy, but hey.
	 * If the object is outside the inventory or equipment, it must
	 * be walked to first.
	 * Otherwise just emits the corresponding Say.
	 * @param g
	 */
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

	/**
	 * @return Map with info for creating DumbGameThings 
	 */
	public Map<String, String> info(){
		Location l = location();
		if(!(l instanceof Level.Location))
			return super.info();
		Map<String, String> out = new HashMap<String, String>(super.info());
		out.put("luminance", String.valueOf(((Level.Location)l).level().luminance()));
		return out;
	}


}
