package clientinterface;

import util.*;

import java.util.*;

public class Conversions {
	public static GameThing fromServerGameThing(final game.GameThing src){
		return new GameThing(){
			private Position position = src.position();
			private Direction direction = src.direction();
			private Area area = src.area();
			private String renderer = src.renderer();
			private final int gid = src.gid();
			private List<String> interactions = new ArrayList<String>(src.interactions());

			public Position position(){
				return position;
			}

			public Position position(Position s){
				return position = s;
			}

			public Direction direction(){
				return direction;
			}

			public Direction direction(Direction s){
				return direction = s;
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

	public static GameModel fromServerGameModel(final game.GameModel src){
		return new GameModel(){
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

