package ui.isometric.builder.things;

import game.GameThing;
import game.GameWorld;

import java.awt.image.BufferedImage;
import java.util.*;

import ui.isometric.libraries.IsoRendererLibrary;
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
			return IsoRendererLibrary.imageForRendererName(renderer, Direction.NORTH).image();
		}
		
		@Override
		public Set<String> rendererNames() {
			return new HashSet<String>(){private static final long serialVersionUID = 1L;{add(renderer);}};
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
			return player;
		}

		@Override
		public BufferedImage previewImage() {
			return IsoRendererLibrary.imageForRendererName("character_"+renderer+"_empty", Direction.NORTH).image();
		}
		
		@Override
		public Set<String> rendererNames() {
			return new HashSet<String>(){private static final long serialVersionUID = 1L;
				{
					add("character_"+renderer+"_empty");
					add("character_"+renderer+"_empty_attack");
					add("character_"+renderer+"_empty_die");
				}};
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
		 * Create a wall with a given renderer
		 * @param rendererName
		 */
		public WallCreator(String rendererName) {
			renderer = rendererName;
		}
		
		@Override
		public GameThing createThing(GameWorld w) {
			game.things.Wall wall = new game.things.Wall(w, renderer);
			return wall;
		}

		@Override
		public BufferedImage previewImage() {
			return IsoRendererLibrary.imageForRendererName(renderer, Direction.NORTH).image();
		}
		
		@Override
		public Set<String> rendererNames() {
			return new HashSet<String>(){private static final long serialVersionUID = 1L;{add(renderer);}};
		}
	}
	
	/**
	 * A class that generates doors
	 * @author melby
	 *
	 */
	public static class DoorCreator implements ThingCreator {
		private String openR;
		private String closedR;
		private boolean open;
		
		/**
		 * Create a door with given renderer + open state
		 * @param closedR - closed renderer
		 * @param openR - open renderer
		 * @param open - open state
		 */
		public DoorCreator(String closedR, String openR, boolean open) {
			this.openR = openR;
			this.closedR = closedR;
			this.open = open;
		}
		
		@Override
		public GameThing createThing(GameWorld w) {
			game.things.Door door = new game.things.Door(w, closedR, openR, open);
			return door;
		}

		@Override
		public BufferedImage previewImage() {
			return IsoRendererLibrary.imageForRendererName(open?openR:closedR, Direction.NORTH).image();
		}
		
		@Override
		public Set<String> rendererNames() {
			return new HashSet<String>(){private static final long serialVersionUID = 1L;{add(openR);add(closedR);}};
		}
	}
	
	/**
	 * A class that generates spawn points
	 * @author melby
	 *
	 */
	public static class SpawnPointCreator implements ThingCreator {
		@Override
		public GameThing createThing(GameWorld w) {
			game.things.SpawnPoint spawn = new game.things.SpawnPoint(w);
			return spawn;
		}

		@Override
		public BufferedImage previewImage() {
			return IsoRendererLibrary.imageForRendererName("spawn_point", Direction.NORTH).image();
		}
		
		@Override
		public Set<String> rendererNames() {
			return new HashSet<String>(){private static final long serialVersionUID = 1L;{add("spawn_point");}};
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
				creators.add(new GroundCreator("ground_grey_pool_1", true));
				creators.add(new GroundCreator("ground_grey_pools_1", true));
				creators.add(new GroundCreator("ground_grey_pools_2", true));
				creators.add(new GroundCreator("ground_grey_rock_1", true));
				creators.add(new GroundCreator("ground_grey_rock_2", true));
				creators.add(new GroundCreator("ground_grey_rock_3", true));
				creators.add(new GroundCreator("ground_grey_stones_1"));
				creators.add(new GroundCreator("ground_grey_spikes_1", true));
				creators.add(new GroundCreator("ground_grey_spikes_2", true));
				creators.add(new GroundCreator("ground_grey_spikes_3", true));
				
				creators.add(new GroundCreator("ground_grey_water_corner", true));
				creators.add(new GroundCreator("ground_grey_water_two_sides", true));
				creators.add(new GroundCreator("ground_grey_water_one_side", true));
				creators.add(new GroundCreator("ground_grey_water_island_1", true));
				creators.add(new GroundCreator("ground_grey_water_rock_1", true));
				
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
				
				creators.add(new PlayerCreator("cordi"));
				creators.add(new PlayerCreator("bob"));
				
				creators.add(new WallCreator("wall_brown_1_corner"));
				creators.add(new WallCreator("wall_brown_1_x"));
				creators.add(new WallCreator("wall_brown_1_t"));
				creators.add(new WallCreator("wall_brown_1_straight"));
				
				creators.add(new DoorCreator("wall_brown_1_door_closed", "wall_brown_1_door_open", false));
				
				creators.add(new WallCreator("wall_grey_1_corner"));
				creators.add(new WallCreator("wall_grey_1_x"));
				creators.add(new WallCreator("wall_grey_1_t"));
				creators.add(new WallCreator("wall_grey_1_straight"));
				
				creators.add(new DoorCreator("wall_grey_1_door_closed", "wall_grey_1_door_open", false));
				
				creators.add(new WallCreator("wall_grey_2_corner"));
				creators.add(new WallCreator("wall_grey_2_x"));
				creators.add(new WallCreator("wall_grey_2_t"));
				creators.add(new WallCreator("wall_grey_2_straight"));
				
				creators.add(new DoorCreator("wall_grey_2_door_closed", "wall_grey_2_door_open", false));
				
				creators.add(new WallCreator("wall_grey_3_corner"));
				creators.add(new WallCreator("wall_grey_3_x"));
				creators.add(new WallCreator("wall_grey_3_t"));
				creators.add(new WallCreator("wall_grey_3_straight"));
				
				creators.add(new DoorCreator("wall_grey_3_door_closed", "wall_grey_3_door_open", false));
				
				creators.add(new WallCreator("wall_grey_4_corner"));
				creators.add(new WallCreator("wall_grey_4_x"));
				creators.add(new WallCreator("wall_grey_4_t"));
				creators.add(new WallCreator("wall_grey_4_straight"));
				
				creators.add(new DoorCreator("wall_grey_4_door_closed", "wall_grey_4_door_open", false));
				
				creators.add(new GroundCreator("ground_grey_tile_2_corner"));
				creators.add(new GroundCreator("ground_grey_tile_2_one_side"));
				creators.add(new GroundCreator("ground_grey_tile_2_two_sides"));
				creators.add(new GroundCreator("ground_grey_tile_2_loose_1"));
				creators.add(new GroundCreator("ground_grey_tile_2_loose_2"));
				creators.add(new GroundCreator("ground_tile_2"));
				creators.add(new GroundCreator("ground_tile_2_2"));
				creators.add(new GroundCreator("ground_tile_2_greenish_1"));
				creators.add(new GroundCreator("ground_tile_2_trash_1"));
				creators.add(new GroundCreator("ground_tile_2_gravel_1"));
				creators.add(new GroundCreator("ground_tile_2_green_dots_1"));
				creators.add(new GroundCreator("ground_tile_2_red_dots_1"));
				
				creators.add(new GroundCreator("ground_tile_1_tile_2_corner"));
				creators.add(new GroundCreator("ground_tile_1_tile_2_one_side"));
				creators.add(new GroundCreator("ground_tile_1_tile_2_two_sides"));
				
				// TODO: stairs creator
				
				creators.add(new SpawnPointCreator());
				
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
