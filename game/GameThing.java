package game;

import util.*;
import java.util.*;

public interface GameThing extends common.GameThing<Location> {
	public String renderer();
	// TODO: not this!
	public Level getLevel();
	public List<String> interactions();
	public void interact(String inter, game.things.Player who);
	public boolean canWalkInto(Position p, Direction d, game.things.Player who);
	public boolean didWalkInto(Position p, Direction d, game.things.Player who);
}
