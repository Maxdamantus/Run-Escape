package clientinterface;

import util.*;

import java.util.*;

public class Conversions {
	public static GameThing fromServerGameThing(final game.GameThing src){
		return new GameThing(){
			public Position position(){
				return src.position();
			}

			public Direction direction(){
				return src.direction();
			}

			public Area area(){
				return src.area();
			}

			public String renderer(){
				return src.renderer();
			}

			public int gid(){
				return src.gid();
			}

			public List<String> interactions(){
				return src.interactions();
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
				System.out.println("thingsInRect -> " + out);
				return out;
			}

			public GameThing thingWithGID(int gid){
				return fromServerGameThing(src.thingWithGID(gid));
			}
		};
	}
}

