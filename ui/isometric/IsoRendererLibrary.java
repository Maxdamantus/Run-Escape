package ui.isometric;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
	public static final String RENDERER_ISOMETRIC = "renderer.isometric";
	public static final String RENDERER_ISOMETRIC_LEVEL = "level";
	
	private static Map<String, Map<Direction, BufferedImage>> renderers = null;
	
	/**
	 * Get the renderers, if null create them
	 * @return
	 */
	private static Map<String, Map<Direction, BufferedImage>> renderers() {
		if(renderers == null) {
			loadImages();
		}
		
		return renderers;
	}

	/**
	 * Load all the images from disk into our internal data structures
	 */
	private static void loadImages() { // TODO: load from XML
		synchronized(IsoRendererLibrary.class) {
			if(renderers == null) {
				renderers = new HashMap<String, Map<Direction, BufferedImage>>();
				
				renderers.put("ground_grey_1", loadImage1("ground_grey_1"));
				renderers.put("ground_grey_2", loadImage1("ground_grey_2"));
				
				renderers.put("ground_grey_dark_circle_1", loadImage1("ground_grey_dark_circle_1"));
				
				renderers.put("ground_grey_dark_dots_1", loadImage1("ground_grey_dark_dots_1"));
				renderers.put("ground_grey_green_dots_1", loadImage1("ground_grey_green_dots_1"));
				renderers.put("ground_grey_red_dots_1", loadImage1("ground_grey_red_dots_1"));
				
				renderers.put("ground_grey_greenish_1", loadImage1("ground_grey_greenish_1"));
				renderers.put("ground_grey_greenish_2", loadImage1("ground_grey_greenish_2"));
				
				renderers.put("ground_grey_mushrooms_1", loadImage1("ground_grey_mushrooms_1"));
				renderers.put("ground_grey_mushrooms_2", loadImage1("ground_grey_mushrooms_2"));
				renderers.put("ground_grey_mushrooms_3", loadImage1("ground_grey_mushrooms_3"));
				renderers.put("ground_grey_mushrooms_4", loadImage1("ground_grey_mushrooms_4"));
				renderers.put("ground_grey_mushrooms_5", loadImage1("ground_grey_mushrooms_5"));
				renderers.put("ground_grey_mushrooms_6", loadImage1("ground_grey_mushrooms_6"));
				renderers.put("ground_grey_mushrooms_7", loadImage1("ground_grey_mushrooms_7"));
				renderers.put("ground_grey_mushrooms_8", loadImage1("ground_grey_mushrooms_8"));
				
				renderers.put("ground_grey_patch_1", loadImage1("ground_grey_patch_1"));
				
				renderers.put("ground_grey_pool_1", loadImage1("ground_grey_pool_1"));
				renderers.put("ground_grey_pools_1", loadImage1("ground_grey_pools_1"));
				renderers.put("ground_grey_pools_2", loadImage1("ground_grey_pools_2"));
				
				renderers.put("ground_grey_rock_1", loadImage1("ground_grey_rock_1"));
				renderers.put("ground_grey_rock_2", loadImage1("ground_grey_rock_2"));
				renderers.put("ground_grey_rock_3", loadImage1("ground_grey_rock_3"));
				
				renderers.put("ground_grey_spikes_1", loadImage1("ground_grey_spikes_1"));
				renderers.put("ground_grey_spikes_2", loadImage1("ground_grey_spikes_2"));
				renderers.put("ground_grey_spikes_3", loadImage1("ground_grey_spikes_3"));
				
				renderers.put("ground_grey_stones_1", loadImage1("ground_grey_stones_1"));
				
				renderers.put("ground_grey_trash_1", loadImage1("ground_grey_trash_1"));
				
				renderers.put("ground_grey_water_island_1", loadImage1("ground_grey_water_island_1"));
				renderers.put("ground_grey_water_rock_1", loadImage1("ground_grey_water_rock_1"));
				
				renderers.put("ground_grey_water_corner", loadImage4("ground_grey_water_corner"));
				renderers.put("ground_grey_water_one_side", loadImage4("ground_grey_water_one_side"));
				renderers.put("ground_grey_water_two_sides", loadImage4("ground_grey_water_two_sides"));
				
				renderers.put("water_1", loadImage1("water_1"));
				
				
				
				renderers.put("ground_grey_road_corner_1", loadImage4("ground_grey_road_corner_1"));
				renderers.put("ground_grey_road_end_1", loadImage4("ground_grey_road_end_1"));
				
				renderers.put("ground_grey_road_straight_1", loadImage2("ground_grey_road_straight_1"));
				
				renderers.put("ground_grey_road_t_1", loadImage4("ground_grey_road_t_1"));
				
				renderers.put("ground_grey_road_x_1", loadImage1("ground_grey_road_x_1"));
				
				renderers.put("ground_grey_tile_1_corner_1", loadImage4("ground_grey_tile_1_corner_1"));
				renderers.put("ground_grey_tile_1_one_side_1", loadImage4("ground_grey_tile_1_one_side_1"));
				renderers.put("ground_grey_tile_1_two_sides_1", loadImage4("ground_grey_tile_1_two_sides_1"));
				
				renderers.put("ground_tile_1_greenish_1", loadImage1("ground_tile_1_greenish_1"));
				renderers.put("ground_tile_1", loadImage1("ground_tile_1"));
				
				renderers.put("character_cordi_empty", loadImage4("character_cordi_empty"));
			}
		}
	}
	
	/**
	 * Create the data structure needed for an image by having the same image for all 4 directions.
	 * Note, assumes images are png and adds the extension automatically
	 * @param resourceName
	 * @return
	 */
	private static Map<Direction, BufferedImage> loadImage1(String resourceName) {
		Map<Direction, BufferedImage> map = new HashMap<Direction, BufferedImage>();
		
		BufferedImage image = Resources.readImageResourceUnfliped("/resources/isotiles/"+resourceName+".png");
		map.put(Direction.NORTH, image);
		map.put(Direction.EAST, image);
		map.put(Direction.WEST, image);
		map.put(Direction.SOUTH, image);
		
		return map;
	}
	
	/**
	 * Create the data structure for an image by loading the images suffixed _n _e _s _w into the appropriate places
	 * Note, assumes images are png and adds the extension automatically
	 * @param resourceName
	 * @return
	 */
	private static Map<Direction, BufferedImage> loadImage4(String resourceName) {
		Map<Direction, BufferedImage> map = new HashMap<Direction, BufferedImage>();
		
		map.put(Direction.NORTH, Resources.readImageResourceUnfliped("/resources/isotiles/"+resourceName+"_n.png"));
		map.put(Direction.EAST, Resources.readImageResourceUnfliped("/resources/isotiles/"+resourceName+"_e.png"));
		map.put(Direction.WEST, Resources.readImageResourceUnfliped("/resources/isotiles/"+resourceName+"_w.png"));
		map.put(Direction.SOUTH, Resources.readImageResourceUnfliped("/resources/isotiles/"+resourceName+"_s.png"));
		
		return map;
	}
	
	/**
	 * Create the data structure for an image by loading the images suffixed _ns _ew into the appropriate places
	 * Note, assumes images are png and adds the extension automatically
	 * @param resourceName
	 * @return
	 */
	private static Map<Direction, BufferedImage> loadImage2(String resourceName) {
		Map<Direction, BufferedImage> map = new HashMap<Direction, BufferedImage>();
		
		BufferedImage ns = Resources.readImageResourceUnfliped("/resources/isotiles/"+resourceName+"_ns.png");
		BufferedImage ew = Resources.readImageResourceUnfliped("/resources/isotiles/"+resourceName+"_ew.png");
		
		map.put(Direction.NORTH, ns);
		map.put(Direction.EAST, ew);
		map.put(Direction.WEST, ns);
		map.put(Direction.SOUTH, ew);
		
		return map;
	}
	
	/**
	 * Get the appropriate image for a given renderer name
	 * @param renderer
	 * @param viewDirection
	 * @return
	 */
	public static BufferedImage imageForRendererName(String rendererName, Direction viewDirection) {
		Map<Direction, BufferedImage> renderer = renderers().get(rendererName);
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
	public static int levelFromArguments(Map<String, Object> userArguments) {				
		Object tmp = userArguments.get(RENDERER_ISOMETRIC);;
		
		if(tmp != null && tmp instanceof Map) {
			Map<String, Object> iso = (Map<String, Object>)tmp;
			tmp = iso.get(RENDERER_ISOMETRIC_LEVEL);
			
			if(tmp instanceof Number) {
				Number i = (Number)tmp;
				return i.intValue();
			}
		}
		
		return IsoSquare.FLOOR;
	}

	/**
	 * Get a new IsoImage representing the given GameThing on a given square
	 * @param square
	 * @param thing
	 * @param viewDirection
	 * @return
	 */
	public static IsoImage newImageFromGameThing(IsoSquare square, GameThing thing, Direction viewDirection) {
		IsoImage tmp = new IsoImage(imageForRendererName(thing.renderer(), thing.direction().compose(viewDirection)), square);
		tmp.setGameThing(thing);
		return tmp;
	}

	/**
	 * Get all the renderers supported
	 * @return
	 */
	public static Set<String> allRendererNames() {
		return renderers().keySet();
	}
}
