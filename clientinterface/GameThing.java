package clientinterface;

import util.*;

import java.util.*;

public interface GameThing {
	public Position position();
	public Position position(Position set);
	public Direction direction();
	public Direction direction(Direction set);
	// relative to the position and direction; should not change during the lifetime of the Thing
	public Area area();
	public String renderer();
	public int gid();
	public List<String> interactions();
	public Map<String, Object> userArguments();
	public String defaultInteraction();
}
