package clientinterface;

import util.*;

import java.util.*;

public class GameThing {
	private Area area;
	private String renderer;
	private final int gid;
	private List<String> interactions = new ArrayList<String>();
	private Location location;
	private Map<String, Object> userArguments;

	public GameThing(int g, Location l, Area a, String r, List<String> i, Map<String, Object> u){
		gid = g; location = l; area = a; renderer = r; interactions = i; userArguments = u;
	}


	public void update(GameThing from){
		if(from.gid() != gid)
			throw new RuntimeException("wtf");
		location = from.location(); area = from.area(); renderer = from.renderer(); from.interactions(); from.userArguments();
//		update(from.gid(), from.position(), from.direction(), from.area(), from.renderer(), from.interactions());
	}
/*
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
		return userArguments;
	}

	public String defaultInteraction() {
		return "nothing"; // TODO: max implement
	}
}
