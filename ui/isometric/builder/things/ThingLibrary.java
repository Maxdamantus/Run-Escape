package ui.isometric.builder.things;

import game.GameThing;
import game.GameWorld;
import game.Location;

import java.awt.image.BufferedImage;
import java.util.*;

import ui.isometric.libraries.IsoInventoryImageLibrary;
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
		
		/**
		 * Create a GroundCreator with a given renderer name
		 * @param rendererName
		 */
		public GroundCreator(String rendererName) {
			this(rendererName, false);
		}
		
		@Override
		public GameThing createThing(GameWorld w, Location l) {
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

		@Override
		public String description() {
			return "Ground: "+renderer;
		}
	}
	
	/**
	 * A class that generates new Players
	 * @author melby
	 *
	 */
	public static class PlayerCreator implements ThingCreator {
		private String characterName;
		
		/**
		 * Create a player with a given renderer
		 * @param characterName
		 */
		public PlayerCreator(String characterName) {
			this.characterName = characterName;
		}
		
		@Override
		public GameThing createThing(GameWorld w, Location l) {
			game.things.Player player = new game.things.Player(w, characterName);
			return player;
		}

		@Override
		public BufferedImage previewImage() {
			return IsoRendererLibrary.imageForRendererName("npc_"+characterName+"_empty", Direction.NORTH).image();
		}
		
		@Override
		public Set<String> rendererNames() {
			return new HashSet<String>(){private static final long serialVersionUID = 1L;
				{
					add("npc_"+characterName+"_empty");
					add("npc_"+characterName+"_empty_attack");
					add("npc_"+characterName+"_empty_die");
					add("npc_"+characterName+"_sword");
					add("npc_"+characterName+"_sword_attack");
					add("npc_"+characterName+"_sword_die");
				}};
		}

		@Override
		public String description() {
			return "Player: "+characterName;
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
		public GameThing createThing(GameWorld w, Location l) {
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

		@Override
		public String description() {
			return "Wall: "+renderer;
		}
	}

	/**
	 * A class that generates Game of Life controllers.
	 * @author maz
	 *
	 */
	public static class GoltrollerCreator implements ThingCreator {
		@Override
		public GameThing createThing(GameWorld w, Location l) {
			return new game.things.GOL.Controller(w);
		}

		@Override
		public BufferedImage previewImage() {
			return IsoRendererLibrary.imageForRendererName("chest_3_open", Direction.NORTH).image();
		}
		
		@Override
		public Set<String> rendererNames() {
			Set<String> out = new HashSet<String>();
			out.add("chest_3_open");
			out.add("armour_tunic");
			return out;
		}

		@Override
		public String description() {
			return "Goltroller";
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
		private String doorcode;
		
		/**
		 * Create a door with given renderer + open state
		 * @param closedR - closed renderer
		 * @param openR - open renderer
		 * @param open - open state
		 * @param doorcode - unlock code
		 */
		public DoorCreator(String closedR, String openR, boolean open, String doorcode) {
			this.openR = openR;
			this.closedR = closedR;
			this.open = open;
			this.doorcode = doorcode;
		}
		
		@Override
		public GameThing createThing(GameWorld w, Location l) {
			game.things.Door door = new game.things.Door(w, closedR, openR, open, doorcode);
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

		@Override
		public String description() {
			return "Door ("+(open?"open":"closed")+"): "+openR;
		}
	}
	
	/**
	 * A class that generates spawn points
	 * @author melby
	 *
	 */
	public static class SpawnPointCreator implements ThingCreator {
		@Override
		public GameThing createThing(GameWorld w, Location l) {
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

		@Override
		public String description() {
			return "Spawn Point";
		}
	}

	/**
	 * A class that generates pouches
	 * @author maz
	 *
	 */
	public static class PouchCreator implements ThingCreator {
		@Override
		public GameThing createThing(GameWorld w, Location l) {
			return new game.things.Pouch(w);
		}

		@Override
		public BufferedImage previewImage() {
			return IsoRendererLibrary.imageForRendererName("armour_tunic", Direction.NORTH).image();
		}
		
		@Override
		public Set<String> rendererNames() {
			Set<String> out = new HashSet<String>();
			out.add("armour_tunic");
			return out;
		}

		@Override
		public String description() {
			return "Pouch";
		}
	}

	/**
	 * A class that generates openable furniture
	 * @author melby
	 *
	 */
	public static class OpenableFurnitureCreator implements ThingCreator {
		private String renderer;
		private boolean open;
		private String doorcode;
		
		/**
		 * Create a OpenableFurnitureCreator with the given renderer and open state
		 * @param renderer
		 * @param open
		 * @param doorcode - code for unlocking
		 */
		public OpenableFurnitureCreator(String renderer, boolean open, String doorcode) {
			this.renderer = renderer;
			this.open = open;
			this.doorcode = doorcode;
			
		}
		
		@Override
		public GameThing createThing(GameWorld w, Location l) {
			game.things.OpenableFurniture fur = new game.things.OpenableFurniture(w, renderer, open, null, doorcode);
			return fur;
		}

		@Override
		public BufferedImage previewImage() {
			return IsoRendererLibrary.imageForRendererName(renderer+"_closed", Direction.NORTH).image();
		}
		
		@Override
		public Set<String> rendererNames() {
			return new HashSet<String>(){private static final long serialVersionUID = 1L;
				{
					add(renderer+"_open");
					add(renderer+"_closed");
				}
			};
		}

		@Override
		public String description() {
			return "Openable Furniture ("+(open?"open":"closed")+"): "+renderer;
		}
	}
	
	/**
	 * A class that generates equipment
	 * @author melby
	 *
	 */
	public static class EquipmentCreator implements ThingCreator {
		private String renderer;
		private int attack;
		private game.things.EquipmentGameThing.Slot type;
		private int strength;
		private int defense;
		private int delay;
		private String name;
		
		/**
		 * Create an EquipmentCreator with the given parameters
		 * @param renderer
		 * @param attack
		 * @param strength
		 * @param defense
		 * @param delay
		 * @param name
		 * @param type
		 */
		public EquipmentCreator(String renderer, int attack, int strength, int defense, int delay, String name, game.things.EquipmentGameThing.Slot type) {
			this.renderer = renderer;
			this.attack = attack;
			this.strength = strength;
			this.defense = defense;
			this.delay = delay;
			this.name = name;
			this.type = type;
		}
		
		@Override
		public GameThing createThing(GameWorld w, Location l) {
			game.things.EquipmentGameThing equip = new game.things.EquipmentGameThing(w, attack, strength, defense, delay, type, name, renderer);
			return equip;
		}

		@Override
		public BufferedImage previewImage() {
			return IsoInventoryImageLibrary.imageForName(renderer);
		}
		
		@Override
		public Set<String> rendererNames() {
			return new HashSet<String>(){private static final long serialVersionUID = 1L;{add(renderer);}};
		}

		@Override
		public String description() {
			return name+" (attack:"+attack+" strength:"+strength+" defense:"+defense+" delay:"+((float)defense/1000.0f)+"s)";
		}
	}
	
	/**
	 * A class that generates Valuable game things
	 * @author melby
	 *
	 */
	public static class ValuableThingCreator implements ThingCreator {
		private String renderer;
		private String name;
		private int value;
		
		/**
		 * Create a ValuableThingCreator with the given renderer, name and value
		 * @param renderer
		 * @param name
		 * @param value
		 */
		public ValuableThingCreator(String renderer, String name, int value) {
			this.renderer = renderer;
			this.name = name;
			this.value = value;
		}
		
		@Override
		public GameThing createThing(GameWorld w, Location l) {
			game.things.PickupGameThing pick = new game.things.Valuable(w, name, renderer, value);
			return pick;
		}

		@Override
		public BufferedImage previewImage() {
			return IsoRendererLibrary.imageForRendererName(renderer, Direction.NORTH).image();
		}
		
		@Override
		public Set<String> rendererNames() {
			return new HashSet<String>(){private static final long serialVersionUID = 1L;{add(renderer);}};
		}

		@Override
		public String description() {
			return name+" worth "+value;
		}
	}
	
	/**
	 * A class that generates Coins
	 * @author melby
	 *
	 */
	public static class CoinThingCreator implements ThingCreator {
		private int amount;
		
		/**
		 * Create a CoinThingCreator with the amount of coins
		 * @param amount
		 */
		public CoinThingCreator(int amount) {
			this.amount = amount;
		}
		
		@Override
		public GameThing createThing(GameWorld w, Location l) {
			game.things.Coins coin = new game.things.Coins(w, amount);
			return coin;
		}

		@Override
		public BufferedImage previewImage() {
			return IsoRendererLibrary.imageForRendererName("coins_gold", Direction.NORTH).image();
		}
		
		@Override
		public Set<String> rendererNames() {
			return new HashSet<String>(){private static final long serialVersionUID = 1L;
				{
					add("coins_gold");
					add("coins_bronze");
					add("coins_silver");
				}
			};
		}

		@Override
		public String description() {
			return amount+" "+(amount>1?"coins":"coin");
		}
	}
	
	/**
	 * A class that generates Potions
	 * @author wheelemaxw
	 *
	 */
	
	public static class PotionThingCreator implements ThingCreator {
		private int amount;
		
		/**
		 * Create a PotionCreator with the amount of potions
		 * @param amount
		 */
		public PotionThingCreator(int amount) {
			this.amount = amount;
		}
		
		@Override
		public GameThing createThing(GameWorld w, Location l) {
			game.things.Potion pot = new game.things.Potion(w, amount);
			return pot;
		}

		@Override
		public BufferedImage previewImage() {
			return IsoRendererLibrary.imageForRendererName("potion", Direction.NORTH).image();
		}
		
		@Override
		public Set<String> rendererNames() {
			return new HashSet<String>(){private static final long serialVersionUID = 1L;
				{
					add("potion");
				}
			};
		}
		
		@Override
		public String description() {
			return amount+" "+(amount>1?"potions":"potion");
		}
	}
	
	/**
	 * A class that Creates NPC's
	 * @author melby
	 *
	 */
	public static class NPCCreator implements ThingCreator {		
		private String renderer;
		private String name;
		private int distance;
		private int radius;
		private boolean aggressive;

		/**
		 * Create a NPCCreator
		 * @param renderer
		 * @param name
		 * @param distance
		 * @param aggressive 
		 * @param radius 
		 */
		public NPCCreator(String renderer, String name, int distance, boolean aggressive, int radius) {
			this.renderer = renderer;
			this.name = name;
			this.distance = distance;
			this.aggressive = aggressive;
			this.radius = radius;
		}
		
		@Override
		public GameThing createThing(GameWorld w, Location l) {
			game.things.Enemy enemy = new game.things.Enemy(w, renderer, name, l, distance, null,aggressive,radius);
			return enemy;
		}

		@Override
		public BufferedImage previewImage() {
			return IsoRendererLibrary.imageForRendererName("npc_"+renderer, Direction.NORTH).image(); // TODO: fix naming
		}
		
		@Override
		public Set<String> rendererNames() {
			return new HashSet<String>(){private static final long serialVersionUID = 1L;
				{
					add("npc_"+renderer);
					add("npc_"+renderer+"_attack");
					add("npc_"+renderer+"_die");
				}
			};
		}

		@Override
		public String description() {
			return "NPC: "+name;
		}
	}
	
	/**
	 * A class that creates Shopkeepers
	 * @author melby
	 *
	 */
	public static class ShopkeeperCreator implements ThingCreator {		
		private String renderer;
		private String name;

		/**
		 * Create a ShopkeeperCreator with a given renderer and name
		 * @param renderer
		 * @param name
		 */
		public ShopkeeperCreator(String renderer, String name) {
			this.renderer = renderer;
			this.name = name;
		}
		
		@Override
		public GameThing createThing(GameWorld w, Location l) {
			game.things.ShopKeeper shop = new game.things.ShopKeeper(w, renderer, name);
			return shop;
		}

		@Override
		public BufferedImage previewImage() {
			return IsoRendererLibrary.imageForRendererName("npc_"+renderer, Direction.NORTH).image(); // TODO: fix naming
		}
		
		@Override
		public Set<String> rendererNames() {
			return new HashSet<String>(){private static final long serialVersionUID = 1L;{add("npc_"+renderer);}};
		}

		@Override
		public String description() {
			return "Shopkeeper: "+name;
		}
	}
	
	/**
	 * A class that creates Stairs
	 * @author melby
	 *
	 */
	public static class StairCreator implements ThingCreator {		
		private String renderer;
		private int levels;
		private Direction direction;

		/**
		 * Create a StairCreator with a given renderer, number of levels and direction to enter from
		 * @param renderer
		 * @param levels
		 * @param direction
		 */
		public StairCreator(String renderer, int levels, Direction direction) {
			this.renderer = renderer;
			this.levels = levels;
			this.direction = direction;
		}
		
		@Override
		public GameThing createThing(GameWorld w, Location l) {
			int up = levels>0?levels:0;
			int down = levels<0?-levels:0;
			game.things.Stairs stair = new game.things.Stairs(w, renderer, up, down, direction);
			return stair;
		}

		@Override
		public BufferedImage previewImage() {
			return IsoRendererLibrary.imageForRendererName(renderer, Direction.NORTH).image();
		}
		
		@Override
		public Set<String> rendererNames() {
			return new HashSet<String>(){private static final long serialVersionUID = 1L;{add(renderer);}};
		}

		@Override
		public String description() {
			return "Stairs: "+renderer;
		}
	}
	
	/**
	 * A class that creates Lights
	 * @author melby
	 *
	 */
	public static class LightCreator implements ThingCreator {
		private int radius;
		
		/**
		 * Create a LightCreator
		 * @param radius
		 */
		public LightCreator(int radius) {
			this.radius = radius;
		}
		
		@Override
		public GameThing createThing(GameWorld w, Location l) {
			game.things.Light light = new game.things.Light(w, "light", radius);
			return light;
		}

		@Override
		public BufferedImage previewImage() {
			return IsoRendererLibrary.imageForRendererName("light", Direction.NORTH).image();
		}
		
		@Override
		public Set<String> rendererNames() {
			return new HashSet<String>(){private static final long serialVersionUID = 1L;{add("light");}};
		}

		@Override
		public String description() {
			return "Light";
		}
	}
	
	/**
	 * A class that creates Keys
	 * @author melby
	 *
	 */
	public static class KeysCreator implements ThingCreator {
		@Override
		public GameThing createThing(GameWorld w, Location l) {
			game.things.Key key = new game.things.Key(w, "key", "key");
			return key;
		}

		@Override
		public BufferedImage previewImage() {
			return IsoRendererLibrary.imageForRendererName("key", Direction.NORTH).image();
		}
		
		@Override
		public Set<String> rendererNames() {
			return new HashSet<String>(){private static final long serialVersionUID = 1L;{add("key");}};
		}

		@Override
		public String description() {
			return "Key";
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
				
				creators.add(new WallCreator("wall_brown_1_corner"));
				creators.add(new WallCreator("wall_brown_1_x"));
				creators.add(new WallCreator("wall_brown_1_t"));
				creators.add(new WallCreator("wall_brown_1_straight"));
				
				creators.add(new DoorCreator("wall_brown_1_door_closed", "wall_brown_1_door_open", false, null));
				
				creators.add(new WallCreator("wall_grey_1_corner"));
				creators.add(new WallCreator("wall_grey_1_x"));
				creators.add(new WallCreator("wall_grey_1_t"));
				creators.add(new WallCreator("wall_grey_1_straight"));
				
				creators.add(new DoorCreator("wall_grey_1_door_closed", "wall_grey_1_door_open", false, null));
				
				creators.add(new WallCreator("wall_grey_2_corner"));
				creators.add(new WallCreator("wall_grey_2_x"));
				creators.add(new WallCreator("wall_grey_2_t"));
				creators.add(new WallCreator("wall_grey_2_straight"));
				
				creators.add(new DoorCreator("wall_grey_2_door_closed", "wall_grey_2_door_open", false, null));
				
				creators.add(new WallCreator("wall_grey_3_corner"));
				creators.add(new WallCreator("wall_grey_3_x"));
				creators.add(new WallCreator("wall_grey_3_t"));
				creators.add(new WallCreator("wall_grey_3_straight"));
				
				creators.add(new DoorCreator("wall_grey_3_door_closed", "wall_grey_3_door_open", false, null));
				
				creators.add(new WallCreator("wall_grey_4_corner"));
				creators.add(new WallCreator("wall_grey_4_x"));
				creators.add(new WallCreator("wall_grey_4_t"));
				creators.add(new WallCreator("wall_grey_4_straight"));
				
				creators.add(new DoorCreator("wall_grey_4_door_closed", "wall_grey_4_door_open", false, null));
				
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
				
				creators.add(new WallCreator("plant_1"));
				creators.add(new WallCreator("plant_2"));
				creators.add(new WallCreator("plant_3"));
				creators.add(new WallCreator("plant_4"));
				creators.add(new WallCreator("plant_5"));
				creators.add(new WallCreator("plant_6"));
				
				creators.add(new WallCreator("stake_skull_1"));
				creators.add(new WallCreator("stake_skull_2"));
				creators.add(new WallCreator("stake_skull_3"));
				
				creators.add(new WallCreator("ground_grey_obelisk_1"));
				creators.add(new WallCreator("ground_grey_obelisk_2"));
				creators.add(new WallCreator("ground_grey_tree_1"));
				creators.add(new WallCreator("ground_grey_tree_2"));
				creators.add(new WallCreator("ground_grey_tree_3"));
				creators.add(new WallCreator("ground_grey_tree_4"));
				creators.add(new WallCreator("ground_grey_tree_5"));
				
				creators.add(new OpenableFurnitureCreator("barrel_1", false, null));
				creators.add(new OpenableFurnitureCreator("chest_1", false, null));
				creators.add(new OpenableFurnitureCreator("chest_2", false, null));
				creators.add(new OpenableFurnitureCreator("chest_3", false, null));
				creators.add(new OpenableFurnitureCreator("cupboard_1", false, null));
				
				creators.add(new WallCreator("barrel_2"));
				creators.add(new WallCreator("barrel_3"));
				
				creators.add(new EquipmentCreator("sword_1", 2, 2, 0, 1, "Weak Sword", game.things.EquipmentGameThing.Slot.WEAPON)); // TODO: name
				creators.add(new EquipmentCreator("sword_2", 3, 3, 0, 1, "Pseudo Weak Sword", game.things.EquipmentGameThing.Slot.WEAPON)); // TODO: name
				creators.add(new EquipmentCreator("sword_3", 4, 4, 0, 1, "Slightly Weak Sword", game.things.EquipmentGameThing.Slot.WEAPON)); // TODO: name
				creators.add(new EquipmentCreator("sword_4", 5, 5, 0, 2, "Almost Medium Sword", game.things.EquipmentGameThing.Slot.WEAPON)); // TODO: name
				creators.add(new EquipmentCreator("sword_5", 6, 6, 0, 2, "Medium Sword", game.things.EquipmentGameThing.Slot.WEAPON)); // TODO: name
				creators.add(new EquipmentCreator("sword_6", 7, 7, 0, 2, "Medium-Good Sword", game.things.EquipmentGameThing.Slot.WEAPON)); // TODO: name
				creators.add(new EquipmentCreator("sword_7", 8, 8, 0, 3, "Good Sword", game.things.EquipmentGameThing.Slot.WEAPON)); // TODO: name
				creators.add(new EquipmentCreator("sword_8", 9, 9, 0, 3, "DoubleGood Sword", game.things.EquipmentGameThing.Slot.WEAPON)); // TODO: name
				creators.add(new EquipmentCreator("sword_9", 10, 10, 0, 4, "Great Sword", game.things.EquipmentGameThing.Slot.WEAPON)); // TODO: name
				creators.add(new EquipmentCreator("sword_10", 11, 11, 0, 4, "Greater Sword", game.things.EquipmentGameThing.Slot.WEAPON)); // TODO: name
				creators.add(new EquipmentCreator("sword_11", 12, 12, 0, 4, "Epic Sword", game.things.EquipmentGameThing.Slot.WEAPON)); // TODO: name
				creators.add(new EquipmentCreator("sword_12", 15, 15, 0, 1, "Ultima", game.things.EquipmentGameThing.Slot.WEAPON)); // TODO: name
				
				creators.add(new EquipmentCreator("shield_bronze", 0, 0, 4, 1, "Bronze Shield", game.things.EquipmentGameThing.Slot.SHIELD));
				creators.add(new EquipmentCreator("shield_long", 0, 0, 5, 1, "Long Shield", game.things.EquipmentGameThing.Slot.SHIELD));
				creators.add(new EquipmentCreator("shield_plate", 0, 0, 6, 1, "Plate Shield", game.things.EquipmentGameThing.Slot.SHIELD));
				creators.add(new EquipmentCreator("shield_wood", 0, 0, 3, 1, "Wood Shield", game.things.EquipmentGameThing.Slot.SHIELD));
				
				creators.add(new EquipmentCreator("helmet_iron", 0, 0, 2, 1, "Iron Helmet", game.things.EquipmentGameThing.Slot.HELMET));
				creators.add(new EquipmentCreator("helmet_leather", 0, 0, 1, 1, "Leather Helmet", game.things.EquipmentGameThing.Slot.HELMET));
				
				creators.add(new EquipmentCreator("gauntlets_iron", 0, 0, 3, 1, "Iron Gauntlets", game.things.EquipmentGameThing.Slot.GAUNTLET));
				creators.add(new EquipmentCreator("gauntlets_leather", 0, 0, 2, 1, "Leather Gauntlets", game.things.EquipmentGameThing.Slot.GAUNTLET));
				creators.add(new EquipmentCreator("gauntlets_silk", 0, 0, 1, 1, "Silk Gauntlets", game.things.EquipmentGameThing.Slot.GAUNTLET));
				
				// TODO: cloak
				
				creators.add(new EquipmentCreator("boots_leather_shoes", 0, 0, 0, 0, "Leather Shoes", game.things.EquipmentGameThing.Slot.BOOTS));
				creators.add(new EquipmentCreator("boots_leather", 0, 0, 1, 1, "Leather Boots", game.things.EquipmentGameThing.Slot.BOOTS));
				creators.add(new EquipmentCreator("boots_steel", 0, 0, 2, 1, "Steel Boots", game.things.EquipmentGameThing.Slot.BOOTS));
				
				creators.add(new EquipmentCreator("armour_chain", 0, 0, 4, 1, "Chainmail", game.things.EquipmentGameThing.Slot.ARMOUR));
				creators.add(new EquipmentCreator("armour_leather", 0, 0, 3, 1, "Leather Armour", game.things.EquipmentGameThing.Slot.ARMOUR));
				creators.add(new EquipmentCreator("armour_plate", 0, 0, 6, 1, "Plate Armour", game.things.EquipmentGameThing.Slot.ARMOUR));
				creators.add(new EquipmentCreator("armour_steel", 0, 0, 5, 1, "Steel Armour", game.things.EquipmentGameThing.Slot.ARMOUR));
				creators.add(new EquipmentCreator("armour_tunic", 0, 0, 2, 1, "Tunic", game.things.EquipmentGameThing.Slot.ARMOUR));
				
				// TODO: stairs creator
				
				creators.add(new ValuableThingCreator("crystal_green", "Green Crystal", 50));
				creators.add(new ValuableThingCreator("herbs_1", "Smelly Herbs", 1));
				creators.add(new ValuableThingCreator("herbs_2", "Stoner Herbs", 5));
				creators.add(new ValuableThingCreator("herbs_3", "Epic Herbs", 10));
				creators.add(new ValuableThingCreator("ruby", "Ruby", 100));
				creators.add(new ValuableThingCreator("bar_gold", "Gold Bar", 100));
				creators.add(new ValuableThingCreator("bar_steel", "Steel Bar", 10));
				creators.add(new ValuableThingCreator("emerald", "Emerald", 40));
				creators.add(new ValuableThingCreator("amber", "Amber", 20));
				
				creators.add(new CoinThingCreator(1));
				creators.add(new PotionThingCreator(1));
				
				creators.add(new NPCCreator("blue", "Sir Robert", 10, false, 0));
				creators.add(new ShopkeeperCreator("shopkeeper", "Shop Keeper"));
				
				creators.add(new StairCreator("stairs_brown_up_1", 1, Direction.SOUTH));
				creators.add(new StairCreator("stairs_brown_down_1", -1, Direction.SOUTH));
				creators.add(new StairCreator("stairs_grey_up_1", 1, Direction.SOUTH));
				creators.add(new StairCreator("stairs_grey_down_1", -1, Direction.SOUTH));
				creators.add(new StairCreator("stairs_grey_up_2", 1, Direction.SOUTH));
				creators.add(new StairCreator("stairs_grey_down_2", -1, Direction.SOUTH));
				creators.add(new StairCreator("stairs_grey_up_3", 1, Direction.SOUTH));
				creators.add(new StairCreator("stairs_grey_down_3", -1, Direction.SOUTH));
				
				creators.add(new LightCreator(1));
				
				creators.add(new KeysCreator());
				
				creators.add(new SpawnPointCreator());

				creators.add(new GoltrollerCreator());

				creators.add(new PouchCreator());
				
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
