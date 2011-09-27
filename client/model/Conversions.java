package client.model;

import util.*;

import java.util.*;

/**
 * A temporary set of conversions for server objects to client ones; somewhat mimics the functionality that the network should offer in the future.
 *
 * @author maz
 */

public class Conversions {
	public static GameThing fromServerGameThing(GameWorld world, game.GameThing src){
		return GameThing.make(world, src.gid(), fromServerLocation(src.location()), src.area(), src.renderer(), src.interactions(), new HashMap<String, Object>());
	}

	public static Location fromServerLocation(game.Location src){
		if(src instanceof game.LevelLocation)
			return new LevelLocation(((game.LevelLocation)src).position(), ((game.LevelLocation)src).direction());
		throw new RuntimeException("wtf");
	}
/*
	public static GameThing fromServerGameThing(final game.GameThing src){
		return new GameThing(){
			private Location location = src.location();
			private Area area = src.area();
			private String renderer = src.renderer();
			private final int gid = src.gid();
			private List<String> interactions = new ArrayList<String>(src.interactions());

			public Location location(){
				return location;
			}

			public Location location(Location s){
				return location = s;
			}

			public Area area(){
				return area;
			}

			public String renderer(){
				return renderer;
			}

			public int gid(){
				return gid;
			}

			public List<String> interactions(){
				return interactions;
			}

			public Map<String, Object> userArguments(){
				return src.userArguments();
			}

			public String defaultInteraction() {
				return "nothing"; // TODO: max implement
			}
		};
	}
	*/

	public static GameWorld fromServerGameModel(final game.GameModel src){
		return new GameWorld(){
			public Iterable<GameThing> thingsInRect(Area a){
				Set<GameThing> out = new HashSet<GameThing>();
				for(game.GameThing ggt : src.level(0).portion(a))
					out.add(fromServerGameThing(ggt));
		//		System.out.println("thingsInRect -> " + out);
				return out;
			}

			public GameThing thingWithGID(int gid){
				return fromServerGameThing(src.thingWithGID(gid));
			}
		};
	}
}

