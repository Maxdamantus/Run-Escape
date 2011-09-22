package game;

import util.*;
import java.util.*;

public class Level {
	QuadTree<GameThing> map = new QuadTree<GameThing>();

	public void put(Position p, Direction d, GameThing gt){
		for(Position bit : gt.area().translated(p))
			map.put(bit, gt);
		gt.position(p);
	}

	public void put(Position p, GameThing gt){
		put(p, Direction.NORTH, gt);
	}

	public void remove(GameThing gt){
		for(Position bit : gt.area().translated(gt.position()))
			map.remove(bit, gt);
	}

	// convenience, maybe
	public void move(Position to, GameThing gt){
		remove(gt);
		put(to, gt);
	}

	public void rotate(Direction to, GameThing gt){
		direct(gt.direction().compose(to), gt);
	}

	public void direct(Direction to, GameThing gt){
		put(gt.position(), to, gt);
	}

	public boolean contains(GameThing gt){
		for(GameThing g : portion(gt.position(), gt.position()))
			if(gt == g)
				return true;
		return false;
	}

	public Iterable<GameThing> portion(Position min, Position max){
		Set<GameThing> res = new HashSet<GameThing>();
		for(Map.Entry<Position, GameThing> kv : map.portion(min, max))
			res.add(kv.getValue());
		return res;
	}

	public Iterable<GameThing> portion(Area a){
		return portion(a.position(), new Position(a.position().x() + a.width(), a.position().y() + a.height()));
	}
}
