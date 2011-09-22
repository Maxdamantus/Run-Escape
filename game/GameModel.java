package game;

import java.util.*;

public class GameModel {
	private final Map<Integer, GameThing> allThings = new HashMap<Integer, GameThing>();
	private final Map<Integer, Level> levels = new HashMap<Integer, Level>();

	public GameThing thingWithGID(int gid){
		return allThings.get(gid);
	}

	public Level getLevelFor(GameThing gt){
		for(Map.Entry<Integer, Level> l : levels.entrySet())
			if(l.getValue().contains(gt))
				return l.getValue();
		return null;
	}

	public int introduce(GameThing gt){
		int r;
		// slow, probably, should use PRNG
		do
			r = (int)(Math.random()*(1 << 31));
		while(allThings.containsKey(r));
		allThings.put(r, gt);
		return r;
	}

	// if a requested level doesn't exist, I'll just create it.
	public Level level(int n){
		if(!levels.containsKey(n))
			levels.put(n, new Level());
		return levels.get(n);
	}

	public Set<Integer> levels(){
		return levels.keySet();
	}
}
