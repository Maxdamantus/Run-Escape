package ui.isometric.builder.things;

import java.util.HashSet;
import java.util.Set;

import ui.isometric.libraries.IsoRendererLibrary;

public class ThingCreatorChecker {
	public static void main(String[] args) {
		check();
	}
	
	public static void check() {
		Set<String> allRenderers = IsoRendererLibrary.allRendererNames();
		Set<String> usedRenderers = new HashSet<String>();
		
		for(ThingCreator c : ThingLibrary.creators()) {
			usedRenderers.addAll(c.rendererNames());
		}
		
		boolean ok = true;
		for(String r : allRenderers) {
			if(!usedRenderers.contains(r) && !r.equals(IsoRendererLibrary.EMPTY_TILE_NAME)) {
				ok = false;
				System.out.println("Renderer " + r + " unused");
			}
		}
		
		for(String r : usedRenderers) {
			if(!allRenderers.contains(r) && !r.equals(IsoRendererLibrary.EMPTY_TILE_NAME)) {
				ok = false;
				System.out.println("Renderer " + r + " used and doesn't exist");
			}
		}
		
		if(ok) {
			System.out.println("No errors detected!");
		}
	}
}
