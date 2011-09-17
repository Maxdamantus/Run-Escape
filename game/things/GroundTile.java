package game.things;

import game.*;
import util.*;

public class GroundTile extends AbstractGameThing {
	public GroundTile(GameModel model, Position p){
		super(model, p);
	}

	public String renderer(){
		return "ground_grey_1";
	}
}
