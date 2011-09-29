package game.things;

import game.*;
import util.*;

import java.util.*;

public class GroundTile extends AbstractGameThing {
	private final String renderer;
	private final List<String> interactions;
	{
		interactions = new LinkedList<String>();
		interactions.add("walk here");
	}

	public GroundTile(GameWorld world, String name){
		super(world);
		renderer = name;
	}

	public GroundTile(GameWorld world){
		this(world, "ground_grey_1");
	}

	public List<String> interactions(){
		return interactions;
	}

	public void interact(String inter, Player who){
		if(inter.equals("walk here")){
			if(location() instanceof Level.Location)
				who.moveTo((Level.Location)location());
		}
	}

	public String renderer(){
		return renderer;
	}
}
