package game;

import util.*;
import java.util.*;

public interface GameThing {
	public int gid();
	public Location location();
	public Location location(Location set);
	public Area area();
	public String renderer();
	public Map<String, Object> userArguments();
}
