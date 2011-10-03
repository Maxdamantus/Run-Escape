package game.things;

import game.GameThing;

import java.util.HashSet;
import java.util.Set;

public class Container {
	private HashSet<GameThing> container; 
		
	public Container(){
		container = new HashSet<GameThing>();
	}
	
	public Set<GameThing> getContainer(){
		return container;
	}
	
	public void setContainer(HashSet<GameThing> set){
		container = set;
	}
}
