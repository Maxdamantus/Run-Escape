package game;

import game.things.*;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import serialization.*;

public class Container implements Iterable<GameThing>, Location {
	public static Serializer<Container> serializer(final Serializer<GameThing> gts, final GameWorld w){
		return new Serializer<Container>(){
			public Tree write(Container in){
				return Serializers.set(gts).write(in.set);
			}

			public Container read(Tree in) throws ParseException {
				Container c = new Container(w);
				for(GameThing gt : Serializers.set(gts).read(in))
					c.put(gt);
				return c;
			}
		};
	}

	private final long cid;
	private final Set<GameThing> set = new HashSet<GameThing>(); 
	private Player owner;
	private final GameWorld world;
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private Container parent;

	public Container(GameWorld w){
		world = w;
		cid = w.introduceContainer(this);
		w.emitIntroduceContainer(this);
	}

	public Container(GameWorld w, long g){
		cid = g;
		world = w;
		w.introduceContainer(this, g);
		w.emitIntroduceContainer(this);
	}

	public long cid(){
		return cid;
	}
		
	public Iterator<GameThing> iterator(){
		return set.iterator();
	}

	public void put(GameThing gt){
		gt.location().remove(gt);
		lock.writeLock().lock();
		try { set.add(gt); }
		finally { lock.writeLock().unlock(); }
		gt.location(this);
	}

	public Iterable<GameThing> contents(){
		return this;
	}

	public Iterable<GameThing> snapshot(){
		return new HashSet<GameThing>(set);
	}

	public boolean contains(GameThing gt){
		return set.contains(gt);
	}

	public void remove(GameThing gt){
		lock.writeLock().lock();
		try { set.remove(gt); }
		finally { lock.writeLock().unlock(); }
	}
	
	public boolean hasParent(Container c){
		return this == c || parent != null && parent.hasParent(c);
	}

	public Container parent(){ return parent; }
	public Container parent(Container s){ return parent = s; }

	public Player owner(){ return owner; }
	public Player owner(Player s){ return owner = s; }

	public GameWorld world(){
		return world;
	}
	
	public ReadWriteLock lock() {
		return lock;
	}
}
