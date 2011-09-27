package game.things;

import game.*;
import util.*;

public class GroundTile extends AbstractGameThing {
	private final String renderer;

	public GroundTile(GameWorld world, String name){
		super(world);
		renderer = name;
	}

	public GroundTile(GameWorld world){
		this(world, "ground_grey_1");
	}

	public String renderer(){
		return renderer;
	}
}
