package game.things;

import game.*;

import java.util.*;

public class Light extends AbstractGameThing implements Luminant {
	private int luminance;

	public Light(GameWorld w, int l){
		super(w);
		luminance = l;
	}

	public String name(){
		return "Light";
	}

	public String renderer(){
		return "empty";
	}

	public Map<String, String> info(){
		Map<String, String> out = new HashMap<String, String>(super.info());
		out.put("luminance", String.valueOf(luminance));
		return out;
	}

	public int luminance(){
		return luminance;
	}

	public int luminance(int s){
		return luminance = s;
	}

	public int renderLevel(){
		return 9000;
	}
}
