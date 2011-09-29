package ui.isometric.builder.things;

import game.GameThing;
import game.GameWorld;

import java.awt.image.BufferedImage;
import java.util.*;

import ui.isometric.IsoRendererLibrary;
import util.Direction;

public class ThingLibrary {
	public static class GroundCreator implements ThingCreator {
		private String renderer;
		
		public GroundCreator(String rendererName) {
			renderer = rendererName;
		}
		
		@Override
		public GameThing createThing(GameWorld w) {
			return new game.things.GroundTile(w, renderer);
		}

		@Override
		public BufferedImage image() {
			return IsoRendererLibrary.imageForRendererName(renderer, Direction.NORTH);
		}
		
		@Override
		public String rendererName() {
			return renderer;
		}
	}
	
	private static List<ThingCreator> creators = null;
	private static List<ThingCreator> unmodifiable = null;
	
	private static void setupCreators() {
		synchronized(ThingLibrary.class) {
			if(creators == null) {
				creators = new ArrayList<ThingCreator>();
				unmodifiable = Collections.unmodifiableList(creators);
				
				creators.add(new GroundCreator("ground_grey_1"));
			}
		}
	}
	
	public static List<ThingCreator> creators() {
		synchronized(ThingLibrary.class) {
			if(creators == null) {
				setupCreators();
			}
		}
		
		return unmodifiable;
	}
}
