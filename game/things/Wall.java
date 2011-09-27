package game.things;

import java.util.Collections;
import java.util.List;

import util.Area;
import util.Direction;
import util.Position;
import game.*;

public class Wall extends AbstractGameThing {
	private final String renderer;

	public Wall(GameWorld world, String name){
		super(world);
		renderer = name;
	}
	
	public Wall(GameWorld world){
		this(world, "wallcross");
	}

	public String renderer(){
		return renderer;
	}
}
