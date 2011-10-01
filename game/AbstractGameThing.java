package game;

import util.*;

import java.io.Serializable;
import java.util.*;

public abstract class AbstractGameThing implements GameThing {
	private Location location;
	private final static Area singleSpot = new Area(new Position(0, 0), 1, 1);
	private final long gid;
	private final Map<String, Serializable> userArguments = new HashMap<String, Serializable>();
	private boolean forgotten = false;
	private final GameWorld world;
	private List<Runnable> trackers = new LinkedList<Runnable>();

	public AbstractGameThing(GameWorld w){
		gid = w.introduce(this);
		location = LocationS.NOWHERE;
		world = w;
		w.emitIntroduce(this);
	}

	private AbstractGameThing(GameWorld w, long g){
		location = LocationS.NOWHERE;
		gid = g;
		world = w;
		if(w != null)
			w.emitIntroduce(this);
	}

	private AbstractGameThing(long g){
		world = null;
		gid = g;
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

	public Location location(){
		return location == null? LocationS.NOWHERE : location;
	}

	public Location location(Location s){
		location = s;
		world.emitPut(this, s);
		List<Runnable> old = trackers;
		trackers = new LinkedList<Runnable>();
		for(Runnable r : old)
			r.run();
		return s;
	}

	public Area area(){
		return singleSpot;
	}

	public String renderer(){
		return "null";
	}

	public String name(){
		return "thing";
	}

	public long gid(){
		return gid;
	}

	public List<String> interactions(){
		return Collections.emptyList();
	}

	public void interact(String inter, game.things.Player who){}

	public boolean canWalkInto(Direction d, game.things.Player who){
		return true;
	}

	public boolean didWalkInto(Direction d, game.things.Player who){
		return true;
	}

/*
	public Level getLevel(){
		return modelblah.getLevelFor(this);
	}
*/
	public Map<String, Serializable> userArguments(){
		return userArguments;
	}

	public String defaultInteraction(){
		List<String> i = interactions();
		return i == null || i.size() == 0? null : i.get(0);
	}

	public boolean forgotten(){
		return forgotten;
	}

	public void forget(){
		// will assume the world is removing its association from this' gid to this
		forgotten = true;
	}

	public GameWorld world(){
		return world;
	}

	public void update(){
		world.emitUpdate(this);
	}

	public String toString(){
		return "(" + renderer() + ")";
	}
}
