package ui.isometric;

import java.util.Map;

public class IsoRendererLibrary {
	public static final String RENDERER = "renderer";
	public static final String RENDERER_ISOMETRIC = "isometric";
	public static final String RENDERER_ISOMETRIC_LEVEL = "level";
	
	public IsoImage imageForRendererName(String renderer) {
		return null; // TODO: implement
	}

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
