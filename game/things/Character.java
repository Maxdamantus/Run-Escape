package game.things;

import game.*;

import java.util.*;

import util.*;

// might want to subclass Player by this later, so Player and Enemy are both "Characters".

public abstract class Character extends AbstractGameThing {
	private String renderer;
	private int health;
	private int maxhealth;
	private int attack;
	private int strength;
	private int defence, delay;
	private boolean dying = false; /*couldnt work around the scheduling issues with attacking,
	so boolean was the best way to stop animate being scheduled over*/

	public Character(GameWorld world, String r){
		super(world);
		renderer = r;
		maxhealth(1000);
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
		return "character_" + renderer + "_" + rendererState();
	}
	
	public String rendererState() { // TODO: depends on equipped state
		return "empty";
	}

	public void setStats(int at, int st, int de, int dl){
		attack = at;
		strength = st;
		defence = de;
		delay = dl;
	}
	
	public String type(){
		return renderer;
	}

	public int health(){ return health; }
	public int health(int s){ return health = s; }
	public int maxhealth(){ return maxhealth; }
	public int maxhealth(int s){ return maxhealth = s; }

	public int walkdelay(){
		return 50;
	}

	public int escapedelay(){
		return 250;
	}

	public boolean moveTo(final Location lwhere, final int dist, final Runnable ondone, final boolean keepfollow){
		if(!keepfollow){
			following = null;
			attackIdent = null;
		}
		Location l = location();
		if(lwhere instanceof Level.Location && l instanceof Level.Location){
			final Level.Location where = (Level.Location)lwhere;
			Level.Location to = ((Level.Location)l).nextTo(where, this, dist);
			if(to == null)
				return false;
			world().schedule(new Runnable(){
				public void run(){
					step(where, ondone, dist, stepIdent = new Object());
				}
			}, attacked()? escapedelay() : walkdelay());
			face(l);
			return true;
			
		}
		return false;
	}

	public boolean moveTo(Location where, int dist, Runnable ondone){
		return moveTo(where, dist, ondone, false);
	}

	public boolean moveTo(Location where, Runnable ondone){
		return moveTo(where, 0, ondone);
	}

	public boolean busy(){
		return stepIdent != null || attackIdent != null;
	}

	private Object stepIdent;
	private void step(final Level.Location where, final Runnable ondone, final int dist, final Object ident){
		if(stepIdent != ident)
			return;
		Location l = location();
		if(!(l instanceof Level.Location))
			return;
		if(((Level.Location)l).level().equals(where.level()) && where.dist((Level.Location)l) <= dist){
			stepIdent = null;
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
				}, attacked()? escapedelay() : walkdelay());
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

	private Object attackIdent;
	private Runnable attacker;
	public void attack(final GameThing g){
		if(this.health > 0 && g instanceof Character && ((Character)g).health() > 0){
			final Character thischar = this;
			final Object ident = new Object();
			attackIdent = ident;
			attacker = new Runnable(){
				public void run(){
					if(attackIdent == ident){
						System.out.println(ident);
						Location l = g.location();
						if(l instanceof Level.Location){
							Location ml = location();
							if(ml instanceof Level.Location && ((Level.Location)l).dist((Level.Location)ml) <= 2){
								face(l);
								if((g instanceof Character) && ((Character) g).health() > 0 && thischar.health > 0){
									animate(renderer() + "_attack");
									hurt(g);
								}
							}
						}
						world().schedule(attacker, delay*50);
					}else if(g instanceof Character)
						((Character)g).stopAttackedBy(Character.this);
				}
			};
			follow(g, 2);
			world().schedule(attacker, 500);
		}
	}
	
	public void face(Location l){
		System.out.println("** START");
		new RuntimeException("taoehaoe").printStackTrace();
		Location ml = location();
		Level.Location closest = null;
		for(Direction d : Direction.values()){
			Level.Location p = ((Level.Location)ml).next(d);
			if(closest == null || p.dist((Level.Location)l) < ((Level.Location)l).dist(closest))
				closest = p;
		}
		System.out.println("face: closest = " + closest);
		location(((Level.Location)ml).direct(closest.direction()));
		System.out.println("** END");
	}

	private Set<GameThing> attackedBy = new HashSet<GameThing>();
	public void hurt(GameThing other){
		int maxamt = (int) (10 * (double)(1 + strength / 100));
		int minamt = (int) (30 * (double)(1 + strength / 100));
		int damageamt = (int) (minamt + (double)1/attack * (maxamt - minamt));
			((Character)other).damage(damageamt,this);
		attackedBy.add(other);
	}

	public boolean attacked(){
		return !attackedBy.isEmpty();
	}

	private void stopAttackedBy(GameThing other){
		attackedBy.remove(other);
	}
	
	public void damage(int amt, Character from){
		if(!dying()){
			world().emitEmitSound(this, "character_" + renderer + "_ow");
			health -= (int)(amt - (10*(double)(1+defence/100)));
			System.out.println(from.name() + " hurts " + name() + " and his health is now " + health);
			if(health <= 0){
				dying(true);
				animate(renderer() + "_die");
				attackIdent = null;
			}
			update();
		}
	}

	public boolean moveTo(Level.Location where){
		return moveTo(where, null);
	}
	
	public void interact(String name, game.things.Player who){
		super.interact(name, who);
	}

	public boolean dying(){ return dying; }
	public boolean dying(boolean s){ return dying = s; }
	public int attack(){ return attack; }
	public int attack(int s){ return attack = s; }
	public int strength(){ return strength; }
	public int strength(int s){ return strength = s; }
	public int defence(){ return attack; }
	public int defence(int s){ return attack = s; }
	public int delay(){ return attack; }
	public int delay(int s){ return attack = s; }

	@Override
	public int renderLevel() {
		return ui.isometric.abstractions.IsoSquare.CHARACTER;
	}

	public Map<String, String> info(){
		if(maxhealth() <= health())
			return super.info();
		Map<String, String> out = new HashMap<String, String>(super.info());
		out.put("health", String.valueOf((double)health/maxhealth));
		return out;
	}
}
