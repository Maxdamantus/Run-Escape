package game.things;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ui.isometric.IsoRendererLibrary;
import util.Area;
import util.Direction;
import util.Position;

import game.*;

public class Player extends AbstractGameThing {
	private String renderer;

	public Player(GameWorld world, String renderer){
		super(world);
		HashMap<String,Object> map = new HashMap<String, Object>();
		// map.put(IsoRendererLibrary.RENDERER_ISOMETRIC_LEVEL, 1);
		this.userArguments().put(IsoRendererLibrary.RENDERER_ISOMETRIC, map);
		this.renderer = renderer;
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
		where.put(this);
		if(ondone != null)
			ondone.run();
		return true;
	}

	public boolean moveTo(Level.Location where){
		return moveTo(where, null);
	}
}
