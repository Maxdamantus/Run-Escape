package client.model;

import util.*;

public interface GameModel {
	public Iterable<GameThing> thingsInRect(Area a);
	public GameThing thingWithGID(int gid);
	
	//please put these in, thanks, tom
	
	public void updateModel (serialization.Tree tree);
	
	//in the class, please put a constructor that takes an update tree as a parameter 
}
