package game;

public interface Location {
	public void put(GameThing gt);
	public Iterable<GameThing> contents();
	public void remove(GameThing gt);
}
