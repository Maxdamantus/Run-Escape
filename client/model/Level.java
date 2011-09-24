package client.model;

import util.*;
import java.util.*;

public class Level implements Location {
	QuadTree<GameThing> map = new QuadTree<GameThing>();

	public void put(Position p, Direction d, GameThing gt){
		for(Position bit : gt.area().translated(p))
			map.put(bit, gt);
		gt.location(new LevelLocation(p, d));
	}

	public void put(Position p, GameThing gt){
		put(p, Direction.NORTH, gt);
	}

	public void remove(GameThing gt){
		Location l = gt.location();
		if(l instanceof LevelLocation)
			for(Position bit : gt.area().translated(((LevelLocation)l).position()))
				map.remove(bit, gt);
		else
			throw new RuntimeException("wtf");
	}

	// convenience, maybe
	public void move(Position to, GameThing gt){
		remove(gt);
		put(to, gt);
	}

	public void rotate(Direction to, GameThing gt){
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
			gt.location(new LevelLocation(((LevelLocation)l).position(), to));
		}else
			throw new RuntimeException("wtf");
	}

	public boolean contains(GameThing gt){
		Location l = gt.location();
		if(l instanceof LevelLocation){
			for(GameThing g : portion(((LevelLocation)l).position(), ((LevelLocation)l).position()))
				if(gt == g)
					return true;
			return false;
		}
		throw new RuntimeException("wtf");
	}

	public Iterable<GameThing> portion(Position min, Position max){
		Set<GameThing> res = new HashSet<GameThing>();
		for(Map.Entry<Position, GameThing> kv : map.portion(min, max))
			res.add(kv.getValue());
		return res;
	}

	public Iterable<GameThing> portion(Area a){
		return portion(a.position(), new Position(a.position().x() + a.width() - 1, a.position().y() + a.height() - 1));
	}
}
