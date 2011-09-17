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

			public Map<String, Map<String, Object>> userArguments(){
				return null;
			}
		};
	}
}

