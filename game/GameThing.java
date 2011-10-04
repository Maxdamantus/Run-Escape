package game;

import util.*;

import java.io.Serializable;
import java.util.*;

public interface GameThing {
	public long gid();
	public Location location();
	public Location location(Location set);
	public Area area();
	public String renderer();
	public void interact(String name, game.things.Player who);
	public boolean canWalkInto(Direction d, game.things.Character who);
	public boolean didWalkInto(Direction d, game.things.Character who);
	public String defaultInteraction();
	public List<String> interactions();
	public Map<String, Serializable> userArguments();
	public boolean forgotten();
	public void forget();
	public String name();
	public void track(Runnable r);
	public GameWorld world();
}
