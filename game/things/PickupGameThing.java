package game.things;

import game.*;

import java.util.*;

import serialization.*;

/**
 * Abstract classes for GameThings which are able to be picked up
 * @author wheelemaxw and zerzoumax
 *
 */
public abstract class PickupGameThing extends AbstractGameThing {
	public PickupGameThing(GameWorld w) {
		super(w);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Are able to be examined,picked up or dropped(depending on location)
	 */
	public List<String> interactions(){
		List<String> out = new LinkedList<String>();
		// assuming if it's not in a level, it must be droppable.
		if(location() instanceof Level.Location || location() instanceof game.Container && ((Container)location()).owner() == null)
			out.add("pick up");
		else{
			out.add("drop");
			out.add("send");
		}
		out.add("examine");
		return out;
	}
	
	/**
	 * Calls the appropriate interaction method in the Player
	 */
	public void interact(String name, Player who){
		if(name.equals("pick up"))
			who.pickup(this);
		else if(name.equals("send"))
			who.send(this);
		else if(name.equals("drop")){
			who.drop(this);
		}
		else super.interact(name, who);
	}

	/**
	 * Setter
	 * @param l - new Location
	 */
	public Location location(Location l){
		Location r = super.location(l);
		update();
		return r;
	}
	
	/**
	 * Returns the Renderer level for the specific object,
	 * ie at ground level, on top of ground, above all else.
	 */
	public int renderLevel() {
		return ui.isometric.abstractions.IsoSquare.PICKUP_ITEM;
	}
}
