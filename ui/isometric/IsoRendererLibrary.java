package ui.isometric;

import java.util.Map;

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
	 * Get the appropriate IsoImage for a given renderer name
	 * @param renderer
	 * @return
	 */
	public IsoImage imageForRendererName(String renderer) {
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
}
