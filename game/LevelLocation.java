package game;

import util.*;

public class LevelLocation implements Location {
	private final Position position;
	private final Direction direction;

	public LevelLocation(Position p, Direction d){
		position = p; direction = d;
	}

	public Position position(){
		return position;
	}

	public Direction direction(){
		return direction;
	}
}
