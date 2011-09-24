package clientinterface;

import util.*;

import java.util.*;

public class GameThing {
	private Area area = src.area();
	private String renderer = src.renderer();
	private final int gid = src.gid();
	private List<String> interactions = new ArrayList<String>(src.interactions());
	private Location location;

	public GameThing(int g, Location l, Area a, String r, List<String> i){
		gid = g; location = l; area = a; renderer = r; interactions = i;
	}

/*
	public void update(GameThing from){
		update(from.gid(), from.position(), from.direction(), from.area(), from.renderer(), from.interactions());
	}

	private void update(int g, Position p, Direction d, Area a, String r, List<String> i){
		if(g != gid)
			throw new RuntimeException("wtf");
		position = p; direction = d; area = a; renderer = r; interactions = i;
	}
	*/

	public Location location(){
		return location;
	}

	public Location location(Location s){
		return location = s;
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
}
