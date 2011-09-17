package ui.isometric;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import util.Direction;
import util.Resources;

import clientinterface.GameThing;

/**
 * 
 * A class for getting the correct information needed for an IsoCanvas to display from data provided by a GamModel
 * 
 * @author melby
 *
 */
public class IsoRendererLibrary {
	public static final String RENDERER = "renderer";
	public static final String RENDERER_ISOMETRIC = "isometric";
	public static final String RENDERER_ISOMETRIC_LEVEL = "level";
	
	private static Map<String, Map<Direction, BufferedImage>> renderers = null;
	
	private static void loadImages() { // TODO: load from XML
		synchronized(IsoRendererLibrary.class) {
			renderers = new HashMap<String, Map<Direction, BufferedImage>>();
			
			renderers.put("ground_grey_1", loadImageAll("ground_grey_1"));
			renderers.put("ground_grey_2", loadImageAll("ground_grey_2"));
			
			renderers.put("ground_grey_dark_circle_1", loadImageAll("ground_grey_dark_circle_1"));
			
			renderers.put("ground_grey_dark_dots_1", loadImageAll("ground_grey_dark_dots_1"));
			renderers.put("ground_grey_green_dots_1", loadImageAll("ground_grey_green_dots_1"));
			renderers.put("ground_grey_red_dots_1", loadImageAll("ground_grey_red_dots_1"));
			
			renderers.put("ground_grey_greenish_1", loadImageAll("ground_grey_greenish_1"));
			renderers.put("ground_grey_greenish_2", loadImageAll("ground_grey_greenish_2"));
			
			renderers.put("ground_grey_mushrooms_1", loadImageAll("ground_grey_mushrooms_1"));
			renderers.put("ground_grey_mushrooms_2", loadImageAll("ground_grey_mushrooms_2"));
			renderers.put("ground_grey_mushrooms_3", loadImageAll("ground_grey_mushrooms_3"));
			renderers.put("ground_grey_mushrooms_4", loadImageAll("ground_grey_mushrooms_4"));
			renderers.put("ground_grey_mushrooms_5", loadImageAll("ground_grey_mushrooms_5"));
			renderers.put("ground_grey_mushrooms_6", loadImageAll("ground_grey_mushrooms_6"));
			renderers.put("ground_grey_mushrooms_7", loadImageAll("ground_grey_mushrooms_7"));
			renderers.put("ground_grey_mushrooms_8", loadImageAll("ground_grey_mushrooms_8"));
			
			renderers.put("ground_grey_patch_1", loadImageAll("ground_grey_patch_1"));
			
			renderers.put("ground_grey_pool_1", loadImageAll("ground_grey_pool_1"));
			renderers.put("ground_grey_pools_1", loadImageAll("ground_grey_pools_1"));
			renderers.put("ground_grey_pools_2", loadImageAll("ground_grey_pools_2"));
			
			renderers.put("ground_grey_rock_1", loadImageAll("ground_grey_rock_1"));
			renderers.put("ground_grey_rock_2", loadImageAll("ground_grey_rock_2"));
			renderers.put("ground_grey_rock_3", loadImageAll("ground_grey_rock_3"));
			
			renderers.put("ground_grey_spikes_1", loadImageAll("ground_grey_spikes_1"));
			renderers.put("ground_grey_spikes_2", loadImageAll("ground_grey_spikes_2"));
			renderers.put("ground_grey_spikes_3", loadImageAll("ground_grey_spikes_3"));
			
			renderers.put("ground_grey_stones_1", loadImageAll("ground_grey_stones_1"));
			
			renderers.put("ground_grey_trash_1", loadImageAll("ground_grey_trash_1"));
			
			renderers.put("ground_grey_water_island_1", loadImageAll("ground_grey_water_island_1"));
			renderers.put("ground_grey_water_rock_1", loadImageAll("ground_grey_water_rock_1"));
			
			renderers.put("ground_grey_water_corner", loadImage4("ground_grey_water_corner"));
			renderers.put("ground_grey_water_one_side", loadImage4("ground_grey_water_one_side"));
			renderers.put("ground_grey_water_two_sides", loadImage4("ground_grey_water_two_sides"));
			
			renderers.put("water_1", loadImageAll("water_1"));
		}
	}
	
	private static Map<Direction, BufferedImage> loadImageAll(String resourceName) {
		Map<Direction, BufferedImage> map = new HashMap<Direction, BufferedImage>();
		
		BufferedImage image = Resources.readImageResourceUnfliped("/resources/isotiles/"+resourceName+".png");
		map.put(Direction.NORTH, image);
		map.put(Direction.EAST, image);
		map.put(Direction.WEST, image);
		map.put(Direction.SOUTH, image);
		
		return map;
	}
	
	private static Map<Direction, BufferedImage> loadImage4(String resourceName) {
		Map<Direction, BufferedImage> map = new HashMap<Direction, BufferedImage>();
		
		map.put(Direction.NORTH, Resources.readImageResourceUnfliped("/resources/isotiles/"+resourceName+"_n.png"));
		map.put(Direction.EAST, Resources.readImageResourceUnfliped("/resources/isotiles/"+resourceName+"_e.png"));
		map.put(Direction.WEST, Resources.readImageResourceUnfliped("/resources/isotiles/"+resourceName+"_w.png"));
		map.put(Direction.SOUTH, Resources.readImageResourceUnfliped("/resources/isotiles/"+resourceName+"_s.png"));
		
		return map;
	}
	
	/**
	 * Get the appropriate image for a given renderer name
	 * @param renderer
	 * @param viewDirection
	 * @return
	 */
	public BufferedImage imageForRendererName(String rendererName, Direction viewDirection) {
		if(renderers == null) {
			IsoRendererLibrary.loadImages();
		}
		
		Map<Direction, BufferedImage> renderer = renderers.get(rendererName);
		if(renderer != null) {
			return renderer.get(viewDirection);
		}
		else {
			return null;
		}
	}

	/**
	 * Get the level a IsoImage should be displayed at from the user arguments stored by a GameModel
	 * @param userArguments
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int levelFromArguments(Map<String, Map<String, Object>> userArguments) {
		Object tmp = userArguments.get(RENDERER).get(RENDERER_ISOMETRIC);
		
		if(tmp instanceof Map) {
			Map<String, Object> iso = (Map<String, Object>)tmp;
			tmp = iso.get(RENDERER_ISOMETRIC_LEVEL);
			
			if(tmp instanceof Number) {
				Number i = (Number)tmp;
				return i.intValue();
			}
		}
		
		return 0; // TODO: Default level?
	}

	/**
	 * Get a new IsoImage representing the given GameThing
	 * @param thing
	 * @param viewDirection
	 * @return
	 */
	public IsoImage newImageFromGameThing(GameThing thing, Direction viewDirection) {
		IsoImage tmp = new IsoImage(this.imageForRendererName(thing.renderer(), thing.direction().compose(viewDirection)));
		tmp.setGameThing(thing);
		return tmp;
	}
}
