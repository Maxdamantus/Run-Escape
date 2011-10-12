package game.things;

import game.*;

import java.util.*;

/**
 * 
 * @author zerzoumax
 *
 */
public class Light extends AbstractGameThing implements Luminant {
	private int luminance;
	private final String renderer;

	public Light(GameWorld w, String r, int l){
		super(w);
		renderer = r;
		luminance = l;
		update();
	}

	@Override
	public String name(){
		return "Light";
	}

	@Override
	public String renderer(){
		return renderer;
	}

	@Override
	public Map<String, String> info(){
		Map<String, String> out = new HashMap<String, String>(super.info());
		out.put("luminance", String.valueOf(luminance));
		return out;
	}

	/**
	 * Getter
	 * @return luminance level of the light
	 */
	public int luminance(){
		return luminance;
	}

	/**
	 * Setter
	 * @return luminance level of the light
	 */
	public int luminance(int s){
		luminance = s;
		update();
		return s;
	}

	/**
	 * Returns the maximum render level
	 */
	public int renderLevel(){
		return 9000;
	}
}
