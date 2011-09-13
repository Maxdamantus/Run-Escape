package game;

import util.*;
import java.util.*;

public class Level {
	QuadTree<GameThing> map = new QuadTree<GameThing>();

	public void put(Position p, GameThing gt){
		for(Position bit : gt.area().translated(p))
			map.put(bit, gt);
		gt.position(p);
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

	public Iterable<GameThing> portion(Position min, Position max){
		Set<GameThing> res = new HashSet<GameThing>();
		for(Map.Entry<Position, GameThing> kv : map.portion(min, max))
			res.add(kv.getValue());
		return res;
	}
}
