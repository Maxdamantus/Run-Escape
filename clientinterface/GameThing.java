package clientinterface;

import util.*;

import java.util.*;

public interface GameThing {
	public Position position();
	public Direction direction();
	// relative to the position and direction; should not change during the lifetime of the Thing
	public Area area();
	public String renderer();
	public int gid();
	public List<String> interactions();
	public Map<String, Map<String, Object>> userArguments();
	public String defaultInteraction();
}
