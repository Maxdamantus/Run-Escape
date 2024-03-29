package game;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import serialization.*;

/**
 * A class representing the entire state and behaviours of the game.
 * @author maz
 */

public class GameWorld { // TODO: try/finally for locks
	private final Map<Long, GameThing> allThings = new HashMap<Long, GameThing>();
	private final Map<Long, Container> allContainers = new HashMap<Long, Container>();
	private final Map<Integer, Level> levels = new HashMap<Integer, Level>();
	private final Set<DeltaWatcher> watchers = new HashSet<DeltaWatcher>();
	private final Map<String, game.things.Player> players = new HashMap<String, game.things.Player>();
	private final List<game.things.SpawnPoint> spawnpoints = new LinkedList<game.things.SpawnPoint>();
	
	private final ReentrantReadWriteLock allThingsLock = new ReentrantReadWriteLock();
	private final ReentrantReadWriteLock allContainersLock = new ReentrantReadWriteLock();
	private final ReentrantReadWriteLock levelsLock = new ReentrantReadWriteLock();
	private final ReentrantReadWriteLock watchersLock = new ReentrantReadWriteLock();
	private final ReentrantReadWriteLock playersLock = new ReentrantReadWriteLock();
	private final ReentrantReadWriteLock spawnpointsLock = new ReentrantReadWriteLock();

	/**
	 * An interface for WorldDelta observers.
	 * @author maz
	 */
	public static interface DeltaWatcher {
		public void delta(WorldDelta d);
	}

	/**
	 * Associates a given name with a given Player object.
	 */
	public void setPlayer(String name, game.things.Player gt){
		playersLock.writeLock().lock();
		players.put(name, gt);
		playersLock.writeLock().unlock();
	}

	/**
	 * Gets a player associated with a given name; if one doesn't exist, one is created.
	 * @return The player now associated with the given name.
	 */
	public game.things.Player getPlayer(String name, String character){
		game.things.Player player;
		
		if(!players.containsKey(name)){
			player = new game.things.Player(this, character , name);
		}
		
		player = players.get(name);
		
		return player;
	}
	
	/**
	 * Determines whether or not a player is associated with the given name, in the system.
	 * @return true if and only if the player exists.
	 */
	public boolean checkPlayer(String name) {
		if(players.containsKey(name))
			return true;
		else
			return false;
	}

	/**
	 * Registers a spawn point as existing in the world.
	 */
	public void addSpawnPoint(game.things.SpawnPoint sp){
		spawnpointsLock.writeLock().lock();
		spawnpoints.add(sp);
		spawnpointsLock.writeLock().unlock();
	}

	/**
	 * Gets a random spawn point from the ones added with addSpawnPoint.
	 */
	public game.things.SpawnPoint getSpawnPoint(){
		game.things.SpawnPoint spawnpoint = null;
		spawnpointsLock.readLock().lock();
		if(spawnpoints.size() > 0)
			spawnpoint = spawnpoints.get((int)(Math.random()*spawnpoints.size()));
		spawnpointsLock.readLock().unlock();
		return spawnpoint;
	}

	/**
	 * Gets a spawn point closest (by walk distance) to the given location.
	 */
	public game.things.SpawnPoint getSpawnPoint(Location l, game.things.Player who){
		if(!(l instanceof Level.Location))
			return getSpawnPoint();
		Level.Location ll = (Level.Location)l;
		game.things.SpawnPoint closest = null;
		int dist = 42;
		int[] tmp = new int[1];
		for(game.things.SpawnPoint sp : spawnpoints){
			Location sl = sp.location();
			if(sl instanceof Level.Location){
				Level.Location sll = (Level.Location)sl;
				if(ll.nextTo(sll, who, 0, tmp) != null && (closest == null || tmp[0] < dist)){
					closest = sp;
					dist = tmp[0];
				}
			}
		}
		return closest == null? getSpawnPoint() : closest;
	}

	/**
	 * Unregisters the given spawn point; the complement of addSpawnPoint.
	 * @return true if and only if one was removed.
	 */
	public boolean removeSpawnPoint(game.things.SpawnPoint sp){
		spawnpointsLock.writeLock().lock();
		boolean r = spawnpoints.remove(sp);
		spawnpointsLock.writeLock().unlock();
		return r;
	}

	/**
	 * Gets the GameThing with the given GID.
	 */
	public GameThing thingWithGID(long gid){
		return allThings.get(gid);
	}

	/**
	 * Gets the Container with the given CID.
	 */
	public Container containerWithCID(long cid){
		return allContainers.get(cid);
	}

	/**
	 * Given a Map&lt,Long, ?&gt;, m, find some long value v such that !m.containsKey(v).
	 */
	public static long someUnusedID(Map<Long, ?> m){
		long r;
		do
			r = (long)(Math.random()*((1l << 63) - 1));
		while(m.containsKey(r));
		return r;
	}

	/**
	 * Introduces a GameThing to the world, creating a GID for it.
	 * @return The newly introduced GameThing's GID
	 */
	public long introduce(GameThing gt){
		return introduce(gt, someUnusedID(allThings));
	}

	/**
	 * Introduces a container to the world, creating a CID for it.
	 * @return The newly introduced Container's CID
	 */
	public long introduceContainer(Container ct){
		return introduceContainer(ct, someUnusedID(allContainers));
	}

	/**
	 * Introduces a container to the world, with the given CID.
	 * @return cid
	 */
	public long introduceContainer(Container ct, long cid){
		allContainersLock.writeLock().lock();
		allContainers.put(cid, ct);
		allContainersLock.writeLock().unlock();
		return cid;
	}

	/**
	 * Unregister a GameThing's existance from this world.
	 */
	public void forget(GameThing gt){
		gt.forget();
		allThingsLock.writeLock().lock();
		allThings.remove(gt.gid());
		allThingsLock.writeLock().unlock();
	}

	/**
	 * Introduces a GameThing to the world, with the given GID: intended mostly to be only used on the client, where, with a valid server, GIDs will not collide.
	 */
	public long introduce(GameThing gt, long gid){
		allThingsLock.writeLock().lock();
		allThings.put(gid, gt);
		allThingsLock.writeLock().unlock();
		return gid;
	}

	/**
	 * Gets or creates a level object for the given floor number.
	 * @param n Level number
	 */
	public Level level(int n){
		if(!levels.containsKey(n)) {
			levelsLock.writeLock().lock();
			levels.put(n, new Level(this, n));
			levelsLock.writeLock().unlock();
		}
		return levels.get(n);
	}

	/**
	 * Gets a superset of the non-empty level numbers.
	 */
	public Set<Integer> levels(){
		return new HashSet<Integer>(levels.keySet());
	}
	
	public void emitPut(GameThing gt, Location where){
		emit(new WorldDelta(new WorldDelta.Put(gt.gid(), where), -1));
	}

	public void emitIntroduce(GameThing gt){
		emit(new WorldDelta(new WorldDelta.Introduce(gt.gid()), -1));
	}

	public void emitIntroduceContainer(Container gt){
		emit(new WorldDelta(new WorldDelta.IntroduceContainer(gt.cid()), -1));
	}

	public void emitForget(GameThing gt){
		emit(new WorldDelta(new WorldDelta.Forget(gt.gid()), -1));
	}

	public void emitUpdate(GameThing gt){
		emit(new WorldDelta(new WorldDelta.Update(new DumbGameThing(gt)), -1));
	}

	/**
	 * Emits a Say worldDelta with the specified message
	 * If the destination or source are null it sends to everyone,
	 * otherwise to a specific user(GID)
	 * @author wheelemaxw
	 * @param gt Source GameThing
	 * @param dest - Destination GameThing
	 * @param what - message
	 */
	public void emitSay(GameThing gt, GameThing dest, String what){
		if(gt == null || dest == null)
			emit(new WorldDelta(new WorldDelta.Say(-1, what), -1));
		else
			emit(new WorldDelta(new WorldDelta.Say(gt.gid(), what), dest.gid()));
	}

	public void emitAnimate(GameThing gt, String what){
		emit(new WorldDelta(new WorldDelta.Animate(gt.gid(), what), -1));
	}

	public void emitShowContainer(Container ct, String name, GameThing gt){
		emit(new WorldDelta(new WorldDelta.ShowContainer(ct.cid(), name), gt.gid()));
	}

	public void emitHideContainer(Container ct, GameThing gt){
		emit(new WorldDelta(new WorldDelta.HideContainer(ct.cid()), gt.gid()));
	}

	public void emitEmitSound(GameThing gt, String what){
		emit(new WorldDelta(new WorldDelta.EmitSound(gt.gid(), what), -1));
	}

	public void emit(WorldDelta wd){
		watchersLock.readLock().lock();
		for(DeltaWatcher dw : watchers)
			dw.delta(wd);
		watchersLock.readLock().unlock();
	}

	public void addDeltaWatcher(DeltaWatcher dw){
		watchersLock.writeLock().lock();
		watchers.add(dw);
		watchersLock.writeLock().unlock();
	}

	public void removeDeltaWatcher(DeltaWatcher dw){
		watchersLock.writeLock().lock();
		watchers.remove(dw);
		watchersLock.writeLock().unlock();
	}

	private final Timer timer = new Timer();
	/**
	 * Schedules a task to be run in some time.
	 * @param r What to run
	 * @param d The delay in miliseconds before it is run
	 */
	public void schedule(final Runnable r, long d){
		timer.schedule(new TimerTask(){
			public void run(){
				try{
					synchronized(GameWorld.this){
						r.run();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}, d);
	}

	/**
	 * Pushes all deltas required to get from an empty state to the current one for a client.
	 * @param dw Whom to send it to
	 */
	public void allDeltas(DeltaWatcher dw){
		allContainersLock.readLock().lock();
		for(Container ct : allContainers.values())
			dw.delta(new WorldDelta(new WorldDelta.IntroduceContainer(ct.cid()), -1));
		allContainersLock.readLock().unlock();
		allThingsLock.readLock().lock();
		for(GameThing gt : allThings.values()){
			dw.delta(new WorldDelta(new WorldDelta.Introduce(gt.gid()), -1));
			dw.delta(new WorldDelta(new WorldDelta.Update(new DumbGameThing(gt)), -1));
			dw.delta(new WorldDelta(new WorldDelta.Put(gt.gid(), gt.location()), -1));
		}
		allThingsLock.readLock().unlock();
	}

	/**
	 * Serialize the world, so it can be reloaded into a near-equivalent state.
	 * @return The tree representing the current state
	 */
	public Tree toTree(){
		Serializer<GameThing> gts = ThingsS.makeSerializer(this);
		Serializer<Map<Integer, Integer>> mii = Serializers.map(Serializers.Serializer_Integer, Serializers.Serializer_Integer);
		Tree out = new Tree();
		List<Map.Entry<Location, GameThing>> map = new LinkedList<Map.Entry<Location, GameThing>>();
		levelsLock.readLock().lock();
		for(Level level : levels.values()) {
			level.thingLock().readLock().lock();
			for(GameThing gt : level) {
				if(!(gt instanceof game.things.Player))
					map.add(new AbstractMap.SimpleImmutableEntry<Location, GameThing>(gt.location(), gt));
			}
			level.thingLock().readLock().unlock();
		}
		levelsLock.readLock().unlock();
		playersLock.readLock().lock();
		for(game.things.Player player : players.values())
			map.add(new AbstractMap.SimpleImmutableEntry<Location, GameThing>(LocationS.NOWHERE, player));
		playersLock.readLock().unlock();
		out.add(new Tree.Entry("things", new Serializers.List<Map.Entry<Location, GameThing>>(Serializers.mapEntry(LocationS.s(null), gts)).write(map)));
		Map<Integer, Integer> levlumi = new HashMap<Integer, Integer>();
		for(Map.Entry<Integer, Level> kv : levels.entrySet())
			if(kv.getValue().luminance() != -1)
				levlumi.put(kv.getKey(), kv.getValue().luminance());
		out.add(new Tree.Entry("luminances", mii.write(levlumi)));
		return out;
	}

	/**
	 * Clear this world, and load the state described by the given tree.
	 */
	public void fromTree(Tree in) throws ParseException {
		levelsLock.writeLock().lock();
		allThingsLock.writeLock().lock();
		for(GameThing gt : allThings.values()){
			LocationS.NOWHERE.put(gt);
			gt.forget();
		}
		allThings.clear();
		allThingsLock.writeLock().unlock();
//		for(Level l : levels.values())
//			l.clear();
		levels.clear();
		spawnpoints.clear();
		for(game.things.Player p : players.values())
			p.clear();
		levelsLock.writeLock().unlock();
		Serializer<GameThing> gts = ThingsS.makeSerializer(this);
		for(Map.Entry<Location, GameThing> lgt : new Serializers.List<Map.Entry<Location, GameThing>>(new Serializers.MapEntry<Location, GameThing>(LocationS.s(this), gts)).read(in.find("things")))
			lgt.getKey().put(lgt.getValue());
		for(Map.Entry<Integer, Integer> kv : Serializers.map(Serializers.Serializer_Integer, Serializers.Serializer_Integer).read(in.find("luminances")).entrySet())
			level(kv.getKey()).luminance(kv.getValue());

	}


}
