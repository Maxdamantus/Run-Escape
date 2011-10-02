package game.things;

import game.*;

import serialization.*;

import java.util.*;

public class Player extends AbstractGameThing {
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
				out.add(new Tree.Entry("type", new Tree(in.renderer)));
				out.add(new Tree.Entry("name", new Tree(in.name)));
				return out;
			}

			public GameThing read(Tree in){
				return new Player(world, in.find("type").value(), in.find("name").value());
			}
		});
	}

	private String renderer;
	private final String name;
	private final static int WALKDELAY = 50;
	private final static List<String> interactions;
	static {
		interactions = new LinkedList<String>();
		interactions.add("follow");
	}

	public Player(GameWorld world, String renderer, String n, SpawnPoint p){
		super(world);
		this.renderer = renderer;
		name = n;
		world.setPlayer(n, this);
		if(p != null)
			p.location().put(this);
		update();
	}

	public Player(GameWorld world, String renderer, String n){
		this(world, renderer, n, null);
	}

	public Player(GameWorld world){
		this(world, "character_cordi_empty");
	}

	public Player(GameWorld world, String renderer){
		this(world, renderer, "<insert name here>");
	}

	public String renderer(){
		return this.renderer;
	}

	public String name(){
		return name;
	}

	public List<String> interactions(){
		return interactions;
	}

	public void interact(String name, Player who){
		if(name.equals("follow"))
			who.follow(this);
	}

	public boolean moveTo(final Level.Location where, final int dist, final Runnable ondone, final boolean keepfollow){
		/*
		// call ((Level.Location)location()).nextTo(where).put(this) every so often, ensure only one moveTo at a time ..
		return false; // can't move
		*/
		/*
		where.put(this);
		if(ondone != null)
			ondone.run();
		return true;
		*/
		if(!keepfollow)
			following = null;
		Location l = location();
		if(l instanceof Level.Location){
			Level.Location to = ((Level.Location)l).nextTo(where, this, dist);
			if(to == null)
				return false;
			world().schedule(new Runnable(){
				public void run(){
					step(where, ondone, dist, stepIdent = new Object());
				}
			}, WALKDELAY);
			return true;
		}
		return false;
	}

	public boolean moveTo(Level.Location where, int dist, Runnable ondone){
		return moveTo(where, dist, ondone, false);
	}

	public boolean moveTo(Level.Location where, Runnable ondone){
		return moveTo(where, 0, ondone);
	}

	private Object stepIdent;
	private void step(final Level.Location where, final Runnable ondone, final int dist, final Object ident){
		if(stepIdent != ident)
			return;
		Location l = location();
		if(!(l instanceof Level.Location))
			return;
		if(((Level.Location)l).level().equals(where.level()) && where.dist((Level.Location)l) <= dist){
			if(ondone != null)
				ondone.run();
		}else{
			Level.Location to = ((Level.Location)l).nextTo(where, this, dist);
			if(to != null){
				to.put(this);
				world().schedule(new Runnable(){
					public void run(){
						step(where, ondone, dist, ident);
					}
				}, WALKDELAY);
			}
		}
	}

	private GameThing following;
	// Mm .. can't go: final Runnable tracker = .. and have it reference itself.
	private Runnable tracker;
	public void follow(final GameThing g){
		following = g;
		tracker = new Runnable(){
			public void run(){
				if(g == following){
					Location l = g.location();
					if(l instanceof Level.Location && moveTo((Level.Location)l, 1, null, true))
						g.track(tracker);
				}
			}
		};
		g.track(tracker);
		tracker.run();
	}

	public boolean moveTo(Level.Location where){
		return moveTo(where, null);
	}
}
