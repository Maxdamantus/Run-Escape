package client.model;

import util.*;

public interface GameModel {
	public Iterable<GameThing> thingsInRect(Area a);
	public GameThing thingWithGID(int gid);
}
