package game.things;

import java.util.*;

import util.*;
import game.*;

import serialization.*;

public class TestPickUp extends PickupGameThing {
	public static void makeSerializer(SerializerUnion<GameThing> union, final GameWorld world){
		union.addIdentifier(new SerializerUnion.Identifier<GameThing>(){
			public String type(GameThing g){
				return g instanceof TestPickUp? "testpickup" : null;
			}
		});

		union.addSerializer("door", new Serializer<GameThing>(){
			public Tree write(GameThing o){
				TestPickUp in = (TestPickUp)o;
				Tree out = new Tree();
				out.add(new Tree.Entry("name", new Tree(in.renderer)));
				return out;
			}

			public GameThing read(Tree in){
				return new TestPickUp(world, in.find("name").value());
			}
		});
	}

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
	
	public void interact(String name, game.things.Player who){
		super.interact(name, who);
	}
	
	@Override
	public boolean canWalkInto(Direction d, Character p) {
		return true;
	}
}
