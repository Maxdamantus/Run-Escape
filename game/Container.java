package game;

import java.util.*;

import serialization.*;

public class Container implements Iterable<GameThing>, Location {
	public static Serializer<Container> serializer(final Serializer<GameThing> gts, final GameWorld w){
		return new Serializer<Container>(){
			public Tree write(Container in){
				return Serializers.set(gts).write(in.set);
			}

			public Container read(Tree in){
				Container c = new Container(w);
				for(GameThing gt : Serializers.set(gts).read(in))
					c.put(gt);
				return c;
			}
		};
	}

	private final long cid;
	private final Set<GameThing> set = new HashSet<GameThing>(); 

	public Container(GameWorld w){
		cid = w.introduceContainer(this);
		w.emitIntroduceContainer(this);
	}

	public Container(GameWorld w, long g){
		cid = g;
		w.introduceContainer(this, g);
		w.emitIntroduceContainer(this);
	}

	public long cid(){
		return cid;
	}
		
	public Iterator<GameThing> iterator(){
		return Collections.unmodifiableSet(set).iterator();
	}

	public void put(GameThing gt){
		gt.location().remove(gt);
		set.add(gt);
		gt.location(this);
	}

	public Iterable<GameThing> contents(){
		return this;
	}

	public void remove(GameThing gt){
		set.remove(gt);
	}
}
