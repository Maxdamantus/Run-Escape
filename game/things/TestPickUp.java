package game.things;

import java.util.List;

import util.Direction;
import game.Container;
import game.GameThing;
import game.GameWorld;
import game.PickupGameThing;

public class TestPickUp extends PickupGameThing {

	private final String renderer;
	

	
	//For reading in (serializer to be completed)
	// MaxZ's note: things won't know they might be instantiated inside a container; the container will just create them afterwards, put them inside it.
	public TestPickUp(GameWorld world, String name, Container cont){
		super(world);
		renderer = name;
		update();
	}
	
	//For empty chest
	public TestPickUp(GameWorld world, String name){
		super(world);
		renderer = name;
		update();
	}
	
	//need the default renderer for a Test object, at the moment looks like a wall...
	public TestPickUp(GameWorld world){
		this(world, "wall_brown_1_x");
	}
	
	public List<String> interactions(){
		return super.interactions();
	}
	
	public String renderer(){
		return renderer;
	}

	public String name(){
		return "TestPickUp";
	}
	
	@Override
	public boolean canWalkInto(Direction d, Character p) {
		return true;
	}
}
