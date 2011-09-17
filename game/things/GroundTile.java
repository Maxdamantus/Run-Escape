package game.things;

import game.*;
import util.*;

public class GroundTile extends AbstractGameThing {
	private final String renderer;

	public GroundTile(GameModel model, String name){
		super(model);
		renderer = name;
	}

	public GroundTile(GameModel model){
		this(model, "ground_grey_1");
	}

	public String renderer(){
		return renderer;
	}
}
