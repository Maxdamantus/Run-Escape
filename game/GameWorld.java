package game;

import java.util.*;

import serialization.*;

public class GameWorld {
	private final Map<Integer, GameThing> allThings = new HashMap<Integer, GameThing>();
	private final Map<Integer, Level> levels = new HashMap<Integer, Level>();
	private final Set<DeltaWatcher> watchers = new HashSet<DeltaWatcher>();
	private final Timer scheduler = new Timer();

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

	public void emitIntroduce(GameThing gt){
		emit(new WorldDelta(new WorldDelta.Introduce(gt.gid())));
	}

	public void emitUpdate(GameThing gt){
		emit(new WorldDelta(new WorldDelta.Update(new DumbGameThing(gt))));
	}

	public void emitSay(GameThing gt, String what){
		emit(new WorldDelta(new WorldDelta.Say(gt.gid(), what)));
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

	public void schedule(final Runnable r, long d){
		scheduler.schedule(new TimerTask(){
			public void run(){
				GameWorld.this.run(r);
			}
		}, d);
	}

	public synchronized void run(Runnable r){
		r.run();
	}

//	// spam!
//	{
//		addDeltaWatcher(new DeltaWatcher(){
//			public void delta(WorldDelta wd){
//				WorldDelta.serializer(GameWorld.this).write(wd).print();
//				System.out.println();
//			}
//		});
//	}

	public void allDeltas(DeltaWatcher dw){
		for(GameThing gt : allThings.values()){
			dw.delta(new WorldDelta(new WorldDelta.Introduce(gt.gid())));
			dw.delta(new WorldDelta(new WorldDelta.Update(new DumbGameThing(gt))));
			emit(new WorldDelta(new WorldDelta.Put(gt.gid(), gt.location())));
		}
	}

	public Tree toTree(){
		Serializer<GameThing> gts = ThingsS.makeSerializer(this);
		List<Map.Entry<Location, GameThing>> map = new LinkedList<Map.Entry<Location, GameThing>>();
		for(GameThing gt : allThings.values())
			map.add(new AbstractMap.SimpleImmutableEntry<Location, GameThing>(gt.location(), gt));
		return new Serializers.List<Map.Entry<Location, GameThing>>(new Serializers.MapEntry<Location, GameThing>(LocationS.s(null), gts)).write(map);
	}

	public void fromTree(Tree in){
		allThings.clear();
		levels.clear();
		Serializer<GameThing> gts = ThingsS.makeSerializer(this);
		for(Map.Entry<Location, GameThing> lgt : new Serializers.List<Map.Entry<Location, GameThing>>(new Serializers.MapEntry<Location, GameThing>(LocationS.s(this), gts)).read(in))
			lgt.getKey().put(lgt.getValue());
	}
}
