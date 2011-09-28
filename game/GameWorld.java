package game;

import java.util.*;

public class GameWorld {
	private final Map<Integer, GameThing> allThings = new HashMap<Integer, GameThing>();
	private final Map<Integer, Level> levels = new HashMap<Integer, Level>();
	private final Set<DeltaWatcher> watchers = new HashSet<DeltaWatcher>();

	public static interface DeltaWatcher {
		public void delta(WorldDelta d);
	}

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
		do
			r = (int)(Math.random()*((1 << 31) - 1));
		while(allThings.containsKey(r));
		return introduce(gt, r);
	}

	public void forget(GameThing gt){
		gt.forget();
		allThings.remove(gt.gid());
	}

	// only use on the client
	public int introduce(GameThing gt, int gid){
		allThings.put(gid, gt);
		return gid;
	}

	// if a requested level doesn't exist, I'll just create it.
	public Level level(int n){
		if(!levels.containsKey(n))
			levels.put(n, new Level(this, n));
		return levels.get(n);
	}

	public Set<Integer> levels(){
		return levels.keySet();
	}
	
	public serialization.Tree serialize(){
		return new serialization.Tree();
	}

	public void emitPut(GameThing gt, Location where){
		emit(new WorldDelta(new WorldDelta.Put(gt.gid(), where)));
	}

	public void emit(WorldDelta wd){
		for(DeltaWatcher dw : watchers)
			dw.delta(wd);
	}

	public void addDeltaWatcher(DeltaWatcher dw){
		watchers.add(dw);
	}

	public void removeDeltaWatcher(DeltaWatcher dw){
		watchers.remove(dw);
	}

	// spam!
	{
		addDeltaWatcher(new DeltaWatcher(){
			public void delta(WorldDelta wd){
				WorldDelta.serializer(GameWorld.this).write(wd).print();
			}
		});
	}
}
