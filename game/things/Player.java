package game.things;

import java.util.Collections;
import java.util.List;

import util.Area;
import util.Direction;
import util.Position;
import game.AbstractGameThing;
import game.GameModel;
import game.GamePlayer;

public class Player extends AbstractGameThing {
	public Player(GameModel model, Position pos, Direction dir, String renderer){
		super(model);
	}
}
