package game.things;

import game.*;

public class Player extends AbstractGameThing {
	private String renderer;

	public Player(GameWorld world, String renderer){
		super(world);
		this.renderer = renderer;
		update();
	}

	public Player(GameWorld world){
		this(world, "character_cordi_empty");
	}

	public String renderer(){
		return this.renderer;
	}

	public boolean moveTo(Level.Location where, Runnable ondone){
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
			Level.Location to = ((Level.Location)l).nextTo(where, this);
			to.put(this);
			return true;
		}
		return false;
	}

	public boolean moveTo(Level.Location where){
		return moveTo(where, null);
	}
}
