package client;

import util.*;
import java.util.*;

public interface GameThing {
	public Position position();
	// relative to the position; should not change during the lifetime of the Thing
	public Area area();
	public String renderer();
	public int gid();
	public List<String> interactions();
}
