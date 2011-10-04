package game.things;

import game.*;

import serialization.*;

import java.util.*;

// might want to subclass Player by this later, so Player and Enemy are both "Characters".

public class Character extends AbstractGameThing {
	private String renderer;
	private int health;
	private final static int WALKDELAY = 50;
	private final static List<String> interactions;
	private final Container inventory;
	static {
		interactions = new LinkedList<String>();
		interactions.add("follow");
		interactions.add("attack");
	}

	public Character(GameWorld world, String r){
		super(world);
		renderer = r;
		update();
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

	public String renderer(){
	//	return "character_" + renderer + "_" + rendererState();
		return "wall_brown_1_door_closed";
	}
	
	private String rendererState() { // TODO: depends on equipped state
		return "empty";
	}

	public int health(){
		return health;
	}

	public int health(int s){
		return health = s;
	}

	public boolean moveTo(final Level.Location where, final int dist, final Runnable ondone, final boolean keepfollow){
		if(!keepfollow){
			following = null;
			attacking = null;
		}
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
	public void follow(final GameThing g, final int dist){
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

	public void follow(GameThing g){
		follow(g, 0);
	}

	private GameThing attacking;
	private Runnable attacker;
	public void attack(final GameThing g){
		attacking = g;
		attacker = new Runnable(){
			public void run(){
				if(attacking == g){
					Location l = g.location();
					if(l instanceof Level.Location){
						Location ml = location();
						if(ml instanceof Level.Location && ((Level.Location)l).dist((Level.Location)ml) <= 2){
							animate(renderer() + "_punch");
							damage(g, 10);
						}
					}
					world().schedule(attacker, 500);
				}
			}
		};
		follow(g, 2);
		attacker.run();
	}
	
	public void damage(GameThing g, int amount){
		if(g instanceof Character){
			((Character)g).health -= amount;
			System.out.println(name() + " damages " + g.name() + " and his health is now " + ((Character)g).health);
		}
	}

	public boolean moveTo(Level.Location where){
		return moveTo(where, null);
	}
}