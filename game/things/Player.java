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
	private Position pos;
	private Direction dir;
	private final static Area singleSpot = new Area(new Position(0, 0), 1, 1);
	private final int gid;
	private String renderer;

	public Player(GameModel model, Position pos, Direction dir, String renderer){
		super(model);
		this.pos = pos;
		this.dir = dir;
		gid = model.introduce(this);
		this.renderer = renderer;
	}

	public Area area(){
		return singleSpot;
	}

	public String renderer(){
		return renderer;
	}

	public int gid(){
		return 0;
	}

	
	public List<String> interactions(){
		return Collections.emptyList();
	}

	public void interact(String inter, GamePlayer who){}

	//???
	public boolean canWalkInto(Position p, Direction d, GamePlayer who){
		return true;
	}

	//???
	public boolean didWalkInto(Position p, Direction d, GamePlayer who){
		return true;
	}

	//To override later
	public String toString(){
		return "(" + renderer() + ")";
	}
}
