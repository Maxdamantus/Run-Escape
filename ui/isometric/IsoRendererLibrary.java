package ui.isometric;

import java.awt.image.BufferedImage;
import java.util.Map;

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
	
	/**
	 * Get the appropriate image for a given renderer name
	 * @param renderer
	 * @return
	 */
	public BufferedImage imageForRendererName(String renderer) {
		return null; // TODO: implement
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
	 * @return
	 */
	public IsoImage newImageFromGameThing(GameThing thing) {
		IsoImage tmp = new IsoImage(this.imageForRendererName(thing.renderer()));
		tmp.setGameThing(thing);
		return tmp;
	}
}
