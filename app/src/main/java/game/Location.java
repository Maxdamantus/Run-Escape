package game;

/**
 * Represents a place that a thing can be. Any thing in a world has one of these.
 */
public interface Location {
	/**
	 * Put the given thing into this location. Responsible for calling the thing's previous location's remove method.
	 */
	public void put(GameThing gt);
	public Iterable<GameThing> contents();
	/**
	 * Remove the given thing from this location. Caller must reset the thing's location following this call.
	 */
	public void remove(GameThing gt);
}
