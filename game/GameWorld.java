package game;

import java.util.*;

import serialization.*;

public class GameWorld {
	private final Map<Long, GameThing> allThings = new HashMap<Long, GameThing>();
	private final Map<Integer, Level> levels = new HashMap<Integer, Level>();
	private final Set<DeltaWatcher> watchers = new HashSet<DeltaWatcher>();
	private final Map<String, game.things.Player> players = new HashMap<String, game.things.Player>();

	public static interface DeltaWatcher {
		public void delta(WorldDelta d);
	}

	public void setPlayer(String name, game.things.Player gt){
		players.put(name, gt);
	}

	public game.things.Player getPlayer(String name){
		if(!players.containsKey(name))
			return new game.things.Player(this, "character_cordi_empty", name);
		return players.get(name);
	}

	public GameThing thingWithGID(long gid){
		return allThings.get(gid);
	}

	public Level getLevelFor(GameThing gt){
		for(Map.Entry<Integer, Level> l : levels.entrySet())
			if(l.getValue().contains(gt))
				return l.getValue();
		return null;
	}

	public long introduce(GameThing gt){
		long r;
		do
			r = (long)(Math.random()*((1l << 63) - 1));
		while(allThings.containsKey(r));
		return introduce(gt, r);
	}

	public void forget(GameThing gt){
		gt.forget();
		allThings.remove(gt.gid());
	}

	// only use on the client
	public long introduce(GameThing gt, long gid){
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
		new Timer().schedule(new TimerTask(){
			public void run(){
				double a = Math.random();
				synchronized(GameWorld.this){
					r.run();
				}
			}
		}, d);
	}

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
