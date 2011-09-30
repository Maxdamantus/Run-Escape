package game;

import util.*;

import java.util.*;

public abstract class AbstractGameThing implements GameThing {
	private Location location;
	private final static Area singleSpot = new Area(new Position(0, 0), 1, 1);
	private final int gid;
	@SuppressWarnings("rawtypes")
	private final Map<String, Serializable> userArguments = new HashMap<String, Serializable>();
	private boolean forgotten = false;
	private final GameWorld world;

	public AbstractGameThing(GameWorld w){
		gid = w.introduce(this);
		location = LocationS.NOWHERE;
		world = w;
		w.emitIntroduce(this);
	}

	private AbstractGameThing(GameWorld w, int g){
		location = LocationS.NOWHERE;
		gid = g;
		world = w;
		if(w != null)
			w.emitIntroduce(this);
	}

	private AbstractGameThing(int g){
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

		public AbstractDumbGameThing(GameWorld w, int g){
			super(w, g);
		}
	}

	public Location location(){
		return location == null? LocationS.NOWHERE : location;
	}

	public Location location(Location s){
		world.emitPut(this, s);
		return location = s;
	}

	public Area area(){
		return singleSpot;
	}

	public String renderer(){
		return "null";
	}

	public int gid(){
		return gid;
	}

	public List<String> interactions(){
		return Collections.emptyList();
	}

	public void interact(String inter, game.things.Player who){}

	public boolean canWalkInto(Position p, Direction d, game.things.Player who){
		return true;
	}

	public boolean didWalkInto(Position p, Direction d, game.things.Player who){
		return true;
	}

/*
	public Level getLevel(){
		return modelblah.getLevelFor(this);
	}
*/
	@SuppressWarnings("rawtypes")
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
