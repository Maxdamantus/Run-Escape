package game;

import util.*;
import java.util.*;

public abstract class AbstractGameThing implements GameThing {
	private Position pos;
	private Direction dir;
	private final static Area singleSpot = new Area(new Position(0, 0), 1, 1);
	private final int gid;

	public AbstractGameThing(GameModel model, Position p){
		pos = p;
		gid = model.introduce(this);
	}

	public Position position(){
		return pos;
	}

	public Position position(Position s){
		return pos = s;
	}

	public Direction direction(){
		return dir;
	}

	public Direction direction(Direction s){
		return dir = s;
	}

	public Area area(){
		return singleSpot;
	}

	public String renderer(){
		return "null";
	}

	public int gid(){
		return 0;
	}

	public List<String> interactions(){
		return Collections.emptyList();
	}

	public void interact(String inter, GamePlayer who){}

	public boolean canWalkInto(Position p, Direction d, GamePlayer who){
		return true;
	}

	public boolean didWalkInto(Position p, Direction d, GamePlayer who){
		return true;
	}
}
