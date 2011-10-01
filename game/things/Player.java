package game.things;

import game.*;

import serialization.*;

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
				return out;
			}

			public GameThing read(Tree in){
				// arghoaehdaoet
				return new Player(world, in.find("type").value());
			}
		});
	}

	private String renderer;
	private final String name;
	private final static int WALKDELAY = 50;

	public Player(GameWorld world, String renderer, String n){
		super(world);
		this.renderer = renderer;
		name = n;
		update();
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

	public boolean moveTo(final Level.Location where, final int dist, final Runnable ondone){
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

	public boolean moveTo(Level.Location where, Runnable ondone){
		return moveTo(where, 0, ondone);
	}

	private Object stepIdent;
	public void step(final Level.Location where, final Runnable ondone, final int dist, final Object ident){
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

	public boolean moveTo(Level.Location where){
		return moveTo(where, null);
	}
}
