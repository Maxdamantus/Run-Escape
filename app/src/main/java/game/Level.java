package game;

import util.*;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import serialization.*;

/**
 * Maintains the state for a single level in a game.
 * @author maz
 */
public class Level implements Iterable<GameThing>, Luminant { // TODO: try/finally for locks
	private final GameWorld world;
	private final int level;
	private int luminance = -1;
	private QuadTree<GameThing> map = new QuadTree<GameThing>();

	private final ReentrantReadWriteLock mapLock = new ReentrantReadWriteLock();

	/**
	 * Read-only object that represents a location on a level in a game. Consists of a level, position on that level, and a facing direction.
	 */
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

				public Location read(Tree in) throws ParseException {
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

		/**
		 * The number of the level this location is in.
		 */
		public int z(){
			return level.level;
		}

		/**
		 * Creates a new location the same as this one, but with the specified facing direction.
		 */
		public Location direct(Direction d){
			return new Location(level, position, d);
		}

		/**
		 * Creates a new location the same as this one, but with the direction rotated by the given direction.
		 */
		public Location rotate(Direction d){
			return direct(direction.compose(d));
		}

		/**
		 * The location the given amount of squares in the given direction.
		 * @param l Amount of squares away
		 */
		public Location next(Direction d, int l){
			return new Location(level, new Position(position.x() + d.dx()*l, position.y() + d.dy()*l), l < 0? d.compose(Direction.SOUTH) : d);
		}

		/**
		 * next(d, 1)
		 */
		public Location next(Direction d){
			return next(d, 1);
		}

		/**
		 * next(direction(), l)
		 */
		public Location next(int l){
			return next(direction, l);
		}

		/**
		 * next(direction())
		 */
		public Location next(){
			return next(direction);
		}

		/**
		 * The location the given number of levels above this one.
		 */
		public Location above(int amt){
			return new Location(level.world.level(level.level + amt), position, direction);
		}

		/**
		 * above(1)
		 */
		public Location above(){
			return above(1);
		}

		/**
		 * above(-amt)
		 */
		public Location below(int amt){
			return above(-amt);
		}

		/**
		 * below(1)
		 */
		public Location below(){
			return below(1);
		}

		/**
		 * Distance from this location to the given one, by non-hypotenuse sides of the triangle with the hypotenuse the direct line between them.
		 */
		public int dist(Location l){
			return Math.abs(l.position().x() - position.x()) + Math.abs(l.position().y() - position.y());
		}

		/**
		 * nextTo(where, who, dest, null)
		 */
		public Location nextTo(Location where, game.things.Character who, int dest){
			return nextTo(where, who, dest, null);
		}

		/**
		 * Find the shortest walking path to the given location.
		 * @param who The player to pathfind with
		 * @param dist The distance away from the destination that the final location in the path is allowed to be
		 * @param len An array whose, if not null, first element will be set to the length of the path found
		 */
		public Location nextTo(final Location where, final game.things.Character who, final int dist, int[] len){
		/*
			try{
				java.io.FileWriter fw = new java.io.FileWriter("dbg.g");
				where.level.map.toDot(fw);
				fw.close();
			}catch(java.io.IOException e){}
			if(5 == 5)
				throw new RuntimeException("halting");
				*/
			if(where.equals(this))
				return this;
			Find.Node<Location> cur = Find.dijkstra(this, new Find.Nextator<Location>(){
				private int x = 0;

				public Iterable<Find.Node<Location>> next(Find.Node<Location> n){
					List<Find.Node<Location>> out = new LinkedList<Find.Node<Location>>();
					if(x++ < 10000)
						for(Direction d : Direction.values()){
							Location p = n.value().next(d);
							if(p.canWalkInto(d, who)){
								out.add(n.next(p, 1 + Math.abs(n.value().position.x() - where.position.x()) + Math.abs(n.value().position.y() - where.position.y())));
							}
						}
					return out;
				}

				public boolean end(Find.Node<Location> p){
					return p.value().level.equals(level) && where.dist(p.value()) <= dist;
				}
			});
			if(cur == null)
				return null;
			if(len != null)
				len[0] = cur.length();
			// eh .. someone's going to be put back in teh same place. Meh.
			if(cur.value().equals(this))
				return this;
			while(!cur.from().value().equals(this))
				cur = cur.from();
			return cur.value();
		}

		public Iterable<GameThing> contents(){
			return level.portion(position, position);
		}

		/**
		 * Logical conjunction of all elements on this location's canWalkInto(d, w)s.
		 */
		public boolean canWalkInto(Direction d, game.things.Character w){
			return canWalkInto(d, w, false);
		}

		private boolean canWalkInto(Direction d, game.things.Character w, boolean second){
			for(GameThing gt : contents())
				if(!gt.canWalkInto(d, w))
					return false;
			return second || next(d.compose(Direction.SOUTH)).canWalkInto(d.compose(Direction.SOUTH), w, true);
		}

		public void put(GameThing gt){
			gt.location().remove(gt);
			level.put(position, direction, gt);
			gt.location(this);
		}

		public void remove(GameThing gt){
			level.remove(gt, position);
		}

		public int hashCode(){
			return level.hashCode() ^ position.hashCode() ^ -882774422;
		}

		/**
		 * Equality disregarding facing direction.
		 */
		public boolean equals(Object o){
			return this == o || o instanceof Location && level.equals(((Location)o).level) && position.equals(((Location)o).position);
		}

		public String toString(){
			return "Level.Location(" + level.level + ", " + position + ", " + direction + ")"; 
		}
	}

	public Level(GameWorld w, int l){
		world = w; level = l;
	}

	/**
	 * Creates a location at the given position with the given direction, on this level.
	 */
	public Location location(Position p, Direction d){
		return new Location(this, p, d);
	}

	private void put(Position p, Direction d, GameThing gt){
		// TODO: rotate area!!!
		mapLock.writeLock().lock();
		for(Position bit : gt.area().translated(p))
			map.put(bit, gt);
		mapLock.writeLock().unlock();
	}

	private void remove(GameThing gt, Position pos){
		// TODO: rotate area!!!
		mapLock.writeLock().lock();
		for(Position bit : gt.area().translated(pos))
			map.remove(bit, gt);
		mapLock.writeLock().unlock();
	}

	public int luminance(){
		return luminance;
	}

	public int luminance(int s){
		luminance = s;
		for(GameThing g : this)
			if(g instanceof game.things.Player)
				((game.things.Player)g).update();
		return s;
	}

	/**
	 * A static portion of the things between the given positions, assuming min.x() &lt;= max.x() &amp;&amp; min.y() &lt;= max.y().
	 */
	public Iterable<GameThing> portion(Position min, Position max){
		List<GameThing> res = new LinkedList<GameThing>();
		mapLock.readLock().lock();
		for(Map.Entry<Position, GameThing> kv : map.portion(min, max))
			res.add(kv.getValue());
		mapLock.readLock().unlock();
		return res;
	}

	/**
	 * A static portion of the things in the given area.
	 */
	public Iterable<GameThing> portion(Area a){
		return portion(a.position(), new Position(a.position().x() + a.width() - 1, a.position().y() + a.height() - 1));
	}

	public Iterator<GameThing> iterator(){
		Set<GameThing> res = new HashSet<GameThing>();
		mapLock.readLock().lock();
		for(Map.Entry<Position, GameThing> kv : map)
			res.add(kv.getValue());
		mapLock.readLock().unlock();
		return res.iterator();
	}

	public ReadWriteLock thingLock() {
		return mapLock;
	}

	public boolean equals(Object o){
		if(o instanceof Level){
			Level l = (Level)o;
			return l.level == level && l.world == world;
		}
		return false;
	}

	public int z(){
		return level;
	}
/*
	public int hashCode(){
		return world.hashCode() ^ level ^ 0xabcde123;
	}
	*/
}
