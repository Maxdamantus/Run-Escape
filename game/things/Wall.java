package game.things;

import java.util.Collections;
import java.util.List;

import util.Area;
import util.Direction;
import util.Position;
import game.*;

public class Wall extends AbstractGameThing {
	private final String renderer;
	
	private Position pos;
	private Direction dir;
	private final static Area singleSpot = new Area(new Position(0, 0), 1, 1);
	private final int gid;

	public Wall(GameModel model, String name){
		super(model);
		pos = new Position(0, 0);
		dir = Direction.NORTH;
		gid = model.introduce(this);
		renderer = name;
	}
	
	public Wall(GameModel model){
		this(model, "wallcross");
	}

	public String renderer(){
		return renderer;
	}

	public int gid(){
		return gid;
	}

	//To be done
	public List<String> interactions(){
		return Collections.emptyList();
	}

	//To be done
	public void interact(String inter, GamePlayer who){}

	//To be done
	public boolean canWalkInto(Position p, Direction d, GamePlayer who){
		return true;
	}

	//To be done
	public boolean didWalkInto(Position p, Direction d, GamePlayer who){
		return true;
	}

	//To be done
	public String toString(){
		return "(" + renderer() + ")";
	}
}