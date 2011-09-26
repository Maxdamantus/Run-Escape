package client.model;

import util.*;

public class LevelLocation implements common.LevelLocation<GameThing> {
	private final Position position;
	private final Direction direction;
	private final GameWorld world;
	private final int level;

	public static class ReaderS implements Reader<LevelLocation> {
		private final GameWorld world;

		public ReaderS(GameWorld w){
			world = w;
		}

		public LevelLocation read(Tree in){
			return new LevelLocation(world,
				serialization.util.Serializers.Serialize_Integer.read(in.find("level")),
				Position.READER.read(in.find("position")),
				Direction.READER.read(in.find("direction")));
		}
	}

	public LevelLocation(GameWorld w, int l, Position p, Direction d){
		position = p; direction = d; world = w; level = l;
	}

	public Position position(){
		return position;
	}

	public Direction direction(){
		return direction;
	}

	public void put(GameThing gt){
		if(gt.location() != null)
			gt.location().remove(gt);
		level().put(position, direction, gt);
	}

	public void remove(GameThing gt){
		level().remove(position, direction, gt);
	}

	public Level level(){
		return world.level(level);
	}

	public int levelInt(){
		return level;
	}

	public boolean equals(Object o){
		if(o instanceof LevelLocation){
			LevelLocation p = (LevelLocation)o;
			return position.equals(p.position) && direction.equals(p.direction) && world == p.world && level == p.level;
		}
		return false;
	}

	public int hashCode(){
		return position.hashCode() ^ direction.hashCode() ^ level;
	}
}
