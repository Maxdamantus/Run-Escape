package ui.isometric.builder.things;

import game.GameThing;
import game.GameWorld;

import java.awt.image.BufferedImage;
import java.util.*;

import ui.isometric.IsoRendererLibrary;
import ui.isometric.IsoSquare;
import util.Direction;

/**
 * A class to manage all the ThingCreators
 * 
 * @author melby
 *
 */
public class ThingLibrary {
	/**
	 * A class that generates new GroundThings
	 * @author melby
	 *
	 */
	public static class GroundCreator implements ThingCreator {
		private String renderer;
		private boolean willBlock;
		
		/**
		 * Create a ground tile with a given renderer
		 * @param rendererName
		 * @param block
		 */
		public GroundCreator(String rendererName, boolean block) {
			renderer = rendererName;
			willBlock = block;
		}

		public GroundCreator(String rendererName) {
			this(rendererName, false);
		}
		
		@Override
		public GameThing createThing(GameWorld w) {
			return new game.things.GroundTile(w, renderer, willBlock);
		}

		@Override
		public BufferedImage previewImage() {
			return IsoRendererLibrary.imageForRendererName(renderer, Direction.NORTH);
		}
		
		@Override
		public String rendererName() {
			return renderer;
		}
	}
	
	/**
	 * A class that generates new Players
	 * @author melby
	 *
	 */
	public static class PlayerCreator implements ThingCreator {
		private String renderer;
		
		/**
		 * Create a player with a given renderer
		 * @param rendererName
		 */
		public PlayerCreator(String rendererName) {
			renderer = rendererName;
		}
		
		@Override
		public GameThing createThing(GameWorld w) {
			game.things.Player player = new game.things.Player(w, renderer);
			IsoRendererLibrary.setLevelInArguments(player.userArguments(), IsoSquare.PLAYER);
			return player;
		}

		@Override
		public BufferedImage previewImage() {
			return IsoRendererLibrary.imageForRendererName(renderer, Direction.NORTH);
		}
		
		@Override
		public String rendererName() {
			return renderer;
		}
	}
	
	/**
	 * A class that generates walls
	 * @author melby
	 *
	 */
	public static class WallCreator implements ThingCreator {
		private String renderer;
		
		/**
		 * Create a player with a given renderer
		 * @param rendererName
		 */
		public WallCreator(String rendererName) {
			renderer = rendererName;
		}
		
		@Override
		public GameThing createThing(GameWorld w) {
			game.things.Wall wall = new game.things.Wall(w, renderer);
			IsoRendererLibrary.setLevelInArguments(wall.userArguments(), IsoSquare.WALL);
			return wall;
		}

		@Override
		public BufferedImage previewImage() {
			return IsoRendererLibrary.imageForRendererName(renderer, Direction.NORTH);
		}
		
		@Override
		public String rendererName() {
			return renderer;
		}
	}
	
	private static List<ThingCreator> creators = null;
	private static List<ThingCreator> unmodifiable = null;
	
	/**
	 * Initialize all the internal ThingCreators
	 */
	private static void setupCreators() {
		synchronized(ThingLibrary.class) {
			if(creators == null) {
				creators = new ArrayList<ThingCreator>();
				unmodifiable = Collections.unmodifiableList(creators);
				
				creators.add(new GroundCreator("ground_grey_1"));
				creators.add(new GroundCreator("ground_grey_2"));
				creators.add(new GroundCreator("ground_grey_trash_1"));
				creators.add(new GroundCreator("ground_grey_green_dots_1"));
				creators.add(new GroundCreator("ground_grey_patch_1"));
				creators.add(new GroundCreator("ground_grey_dark_dots_1"));
				creators.add(new GroundCreator("ground_grey_dark_circle_1"));
				creators.add(new GroundCreator("ground_grey_red_dots_1"));
				creators.add(new GroundCreator("ground_grey_greenish_1"));
				creators.add(new GroundCreator("ground_grey_greenish_2"));
				creators.add(new GroundCreator("ground_grey_pool_1"));
				creators.add(new GroundCreator("ground_grey_pools_1"));
				creators.add(new GroundCreator("ground_grey_pools_2"));
				creators.add(new GroundCreator("ground_grey_rock_1"));
				creators.add(new GroundCreator("ground_grey_rock_2"));
				creators.add(new GroundCreator("ground_grey_rock_3"));
				creators.add(new GroundCreator("ground_grey_stones_1"));
				creators.add(new GroundCreator("ground_grey_spikes_1"));
				creators.add(new GroundCreator("ground_grey_spikes_2"));
				creators.add(new GroundCreator("ground_grey_spikes_3"));
				
				creators.add(new GroundCreator("ground_grey_water_corner", true));
				creators.add(new GroundCreator("ground_grey_water_two_sides", true));
				creators.add(new GroundCreator("ground_grey_water_one_side", true));
				creators.add(new GroundCreator("ground_grey_water_island_1"));
				creators.add(new GroundCreator("ground_grey_water_rock_1"));
				
				creators.add(new GroundCreator("ground_grey_mushrooms_1"));
				creators.add(new GroundCreator("ground_grey_mushrooms_2"));
				creators.add(new GroundCreator("ground_grey_mushrooms_3"));
				creators.add(new GroundCreator("ground_grey_mushrooms_4"));
				creators.add(new GroundCreator("ground_grey_mushrooms_5"));
				creators.add(new GroundCreator("ground_grey_mushrooms_6"));
				creators.add(new GroundCreator("ground_grey_mushrooms_7"));
				creators.add(new GroundCreator("ground_grey_mushrooms_8"));
				
				creators.add(new GroundCreator("water_1", true));
				
				creators.add(new GroundCreator("ground_grey_road_corner_1"));
				creators.add(new GroundCreator("ground_grey_road_end_1"));
				creators.add(new GroundCreator("ground_grey_road_straight_1"));
				creators.add(new GroundCreator("ground_grey_road_t_1"));
				creators.add(new GroundCreator("ground_grey_road_x_1"));
				
				creators.add(new GroundCreator("ground_grey_tile_1_corner_1"));
				creators.add(new GroundCreator("ground_grey_tile_1_one_side_1"));
				creators.add(new GroundCreator("ground_grey_tile_1_two_sides_1"));
				creators.add(new GroundCreator("ground_tile_1"));
				creators.add(new GroundCreator("ground_tile_1_greenish_1"));
				
				creators.add(new PlayerCreator("character_cordi_empty"));
				
				creators.add(new WallCreator("wall_brown_1_corner"));
				creators.add(new WallCreator("wall_brown_1_door_closed"));
				creators.add(new WallCreator("wall_brown_1_door_open"));
				creators.add(new WallCreator("wall_brown_1_x"));
				creators.add(new WallCreator("wall_brown_1_t"));
				creators.add(new WallCreator("wall_brown_1_straight"));
				
				ThingCreatorChecker.check();
			}
		}
	}
	
	/**
	 * Get all the ThingCreators
	 * @return - an immutable list of ThingCreators
	 */
	public static List<ThingCreator> creators() {
		synchronized(ThingLibrary.class) {
			if(creators == null) {
				setupCreators();
			}
		}
		
		return unmodifiable;
	}
}
