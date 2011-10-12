package game;

import util.*;

import java.util.*;

public abstract class AbstractGameThing implements GameThing {
	private Location location;
	private final static Area singleSpot = new Area(new Position(0, 0), 1, 1);
	private final long gid;
	private boolean forgotten = false;
	private final GameWorld world;
	private List<Runnable> trackers = new LinkedList<Runnable>();

	/**
	 * Create the GameThing then adds it to the GameWorld
	 * @param w - The GameWorld to introduce this to.
	 */
	public AbstractGameThing(GameWorld w){
		location = LocationS.NOWHERE;
		world = w;
		if(world != null){
			gid = w.introduce(this);
			w.emitIntroduce(this);
		}else
			gid = 0;
	}

	/**
	 * Create the GameThing then adds it to the GameWorld, with specific GID
	 * @param w - The GameWorld to introduce this to.
	 */
	private AbstractGameThing(GameWorld w, long g){
		location = LocationS.NOWHERE;
		gid = g;
		world = w;
		if(w != null)
			w.emitIntroduce(this);
	}

	// so normal extenders of AbstractGameThing don't have access to the special non-introductive constructor
	public static abstract class AbstractDumbGameThing extends AbstractGameThing {
	/*
		public AbstractDumbGameThing(int g){
			super(g);
		}
		*/

		public AbstractDumbGameThing(GameWorld w, long g){
			super(w, g);
		}
	}

	
	public void track(Runnable r){
		trackers.add(r);
	}

	/**
	 * @return 'Nowhere' if it ha no location, otherwise its Location
	 */
	public Location location(){
		return location == null? LocationS.NOWHERE : location;
	}

	/**
	 * Sets the current location, and updates the GameWorld
	 */
	public Location location(Location s){
		Location lo = location;
		location = s;
		if(world != null)
			world.emitPut(this, s);
		if(!s.equals(lo)){
			List<Runnable> old = trackers;
			trackers = new LinkedList<Runnable>();
			for(Runnable r : old)
				r.run();
		}
		return s;
	}

	public Area area(){
		return singleSpot;
	}

	/**
	 * @return The current renderer string
	 */
	abstract public String renderer();

	/**
	 * Returns a string
	 * @return Name of the Thing
	 */
	abstract public String name();

	/**
	 * @return The unique GameWorld identifier of the GameWorld
	 */
	public long gid(){
		return gid;
	}

	/**
	 * @return Returns the list of available interactions
	 */
	public List<String> interactions(){
		return Collections.emptyList();
	}

	/**
	 * Calls the appropriate interaction method in the Player
	 */
	public void interact(String inter, game.things.Player who){
		if(inter.equals("examine"))
			who.examine(this);
	}

	/**
	 * @return True if you can walk into
	 */
	public boolean canWalkInto(Direction d, game.things.Character who){
		return true;
	}

	public boolean didWalkInto(Direction d, game.things.Character who){
		return true;
	}

	/**
	 * @return String of the first interaction in the list
	 */
	public String defaultInteraction(){
		List<String> i = interactions();
		return i == null || i.size() == 0? null : i.get(0);
	}

	/**
	 * @return True if forgotten by the world
	 */
	public boolean forgotten(){
		return forgotten;
	}

	/**
	 * Remove this object from the game world
	 */
	public void forget(){
		if(world != null)
			world.emitForget(this);
		forgotten = true;
	}

	/**
	 * Gets the GameWorld to emit the specified animation
	 * @param name - specified animation
	 */
	public void animate(String name){
		if(world != null)
			world.emitAnimate(this, name);
	}

	/**
	 * @return - The current GameWorld
	 */
	public GameWorld world(){
		return world;
	}

	public void update(){
		if(world != null)
			world.emitUpdate(this);
	}

	public Map<String, String> info(){
		return Collections.emptyMap();
	}

	public String toString(){
		return "(" + renderer() + ")";
	}
}
