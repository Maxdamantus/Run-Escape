package common;

import util.*;
import java.util.*;

public class Level<T extends GameThing> {
	private final QuadTree<T> map = new QuadTree<T>();

	public void put(Position p, Direction d, T gt){
		// TODO: rotate area!!!
		for(Position bit : gt.area().translated(p))
			map.put(bit, gt);
	}

	public void put(Position p, T gt){
		put(p, Direction.NORTH, gt);
	}

	public void remove(T gt, Position pos){
		// TODO: rotate area!!!
		for(Position bit : gt.area().translated(pos))
			map.remove(bit, gt);
	}
/*
	// convenience, maybe
	public void move(Position to, GameThing gt){
		remove(gt);
		put(to, gt);
	}

	public void rotate(Direction to, gameThing gt){
		Location l = gt.location();
		if(l instanceof LevelLocation){
			direct(((LevelLocation)l).direction().compose(to), gt);
		}else
			throw new RuntimeException("wtf");
	}

	public void direct(Direction to, GameThing gt){
		Location l = gt.location();
		if(l instanceof LevelLocation){
			put(((LevelLocation)l).position(), to, gt);
			gt.location(new LevelLocation(world, level, ((LevelLocation)l).position(), to));
		}else
			throw new RuntimeException("wtf");
	}
*/
/*
	public boolean contains(T gt){
		Location l = gt.location();
		if(l instanceof LevelLocation && ((LevelLocation)l).level() == this)
			for(T g : portion(((LevelLocation)l).position(), ((LevelLocation)l).position()))
				if(gt == g)
					return true;
		return false;
	}
	*/

	public Iterable<T> portion(Position min, Position max){
		Set<T> res = new HashSet<T>();
		for(Map.Entry<Position, T> kv : map.portion(min, max))
			res.add(kv.getValue());
		return res;
	}

	public Iterable<T> portion(Area a){
		return portion(a.position(), new Position(a.position().x() + a.width() - 1, a.position().y() + a.height() - 1));
	}
}
