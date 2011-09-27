package common;

import util.*;
import java.util.*;

public interface GameThing<L extends Location> {
	public int gid();
	public L location();
	public L location(L set);
	public Area area();
	public Map<String, Object> userArguments();
}
