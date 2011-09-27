package game;

import util.*;
import java.util.*;

import serialization.*;
import serialization.util.Serializers;

public class Level {
	private final GameWorld world;
	private final int level;
	private final QuadTree<GameThing> map = new QuadTree<GameThing>();

	public static class Location implements game.Location {
		private final Level level;
		private final Position position;
		private final Direction direction;

		public static Serializer<Location> serializer(final GameWorld w){
			return new Serializer<Location>(){
				public Tree write(Location in){
					Tree out = new Tree();
					out.add(new Tree.Entry("level", Serializers.Serializer_Integer.write(in.level.level)));
					out.add(new Tree.Entry("position", Position.SERIALIZER.write(in.position)));
					out.add(new Tree.Entry("direction", Direction.SERIALIZER.write(in.direction)));
					return out;
				}

				public Location read(Tree in){
					return new Location(
						w.level(Serializers.Serializer_Integer.read(in.find("level"))),
						Position.SERIALIZER.read(in.find("position")),
						Direction.SERIALIZER.read(in.find("direction")));
				}
			};
		}

		public Location(Level l, Position p, Direction d){
			level = l; position = p; direction = d;
		}

		public Position position(){
			return position;
		}

		public Direction direction(){
			return direction;
		}

		public Level level(){
			return level;
		}

		public Location direct(Direction d){
			return new Location(level, position, d);
		}

		public Location rotate(Direction d){
			return direct(direction.compose(d));
		}

		public Location nextTo(Location where){
			/* A* */
			return null;
		}

		public void put(GameThing gt){
			game.Location old = gt.location();
			if(old != null)
				old.remove(gt);
			level.put(position, direction, gt);
			gt.location(this);
		}

		public void remove(GameThing gt){
			level.remove(gt, position);
			gt.location(null);
		}
	}

	public Level(GameWorld w, int l){
		world = w; level = l;
	}

	public Location location(Position p, Direction d){
		return new Location(this, p, d);
	}

	private void put(Position p, Direction d, GameThing gt){
		// TODO: rotate area!!!
		for(Position bit : gt.area().translated(p))
			map.put(bit, gt);
	}

	private void put(Position p, GameThing gt){
		put(p, Direction.NORTH, gt);
	}

	private void remove(GameThing gt, Position pos){
		// TODO: rotate area!!!
		for(Position bit : gt.area().translated(pos))
			map.remove(bit, gt);
	}

/*
	// convenience, maybe
	private void move(Position to, GameThing gt){
		gt.location().remove(gt);
		put(to, gt);
	}
	*/
/*
	private void rotate(Direction to, GameThing gt){
		Location l = gt.location();
		if(l instanceof LevelLocation){
			direct(((LevelLocation)l).direction().compose(to), gt);
		}else
			throw new RuntimeException("wtf");
	}

	private void direct(Direction to, GameThing gt){
		Location l = gt.location();
		if(l instanceof LevelLocation){
			put(((LevelLocation)l).position(), to, gt);
			gt.location(new LevelLocation(world, level, ((LevelLocation)l).position(), to));
		}else
			throw new RuntimeException("wtf");
	}
	*/

	public boolean contains(GameThing gt){
		game.Location l = gt.location();
		if(l instanceof Location && ((Location)l).level() == this)
			for(GameThing g : portion(((Location)l).position(), ((Location)l).position()))
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
		return portion(a.position(), new Position(a.position().x() + a.width() - 1, a.position().y() + a.height() - 1));
	}
}
