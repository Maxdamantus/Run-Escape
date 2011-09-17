package game;

import util.*;
import java.util.*;

public interface GameThing {
	public Position position();
	public Position position(Position set);
	public Direction direction();
	public Direction direction(Direction set);
	public Area area();
	public String renderer();
	public int gid();
	public List<String> interactions();
	public void interact(String inter, GamePlayer who);
	public boolean canWalkInto(Position p, Direction d, GamePlayer who);
	public boolean didWalkInto(Position p, Direction d, GamePlayer who);
}
