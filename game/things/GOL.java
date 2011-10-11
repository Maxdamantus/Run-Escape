package game.things;

import game.*;
import util.*;
import serialization.*;

import java.util.*;

public class GOL {
	public static void makeSerializer(SerializerUnion<GameThing> union, GameWorld world){
		Controller.makeSerializer(union, world);
		Cell.makeSerializer(union, world);
	}

	private static Map<Long, Set<Cell>> allCells = new HashMap<Long, Set<Cell>>();

	public static void tick(GameWorld world, long id){
		if(!allCells.containsKey(id))
			return;
		Set<Level.Location> places = new HashSet<Level.Location>();
		for(Cell c : new HashSet<Cell>(allCells.get(id))){
			Location l = c.location();
			if(l instanceof Level.Location){
				places.add((Level.Location)l);
				LocationS.NOWHERE.put(c);
				world.forget(c);
			}
		}
		Set<Level.Location> done = new HashSet<Level.Location>();
		Set<Level.Location> out = new HashSet<Level.Location>();
		for(Level.Location ll : places){
			int n = 0;
			for(Level.Location neigh : neighs(ll)){
				if(places.contains(neigh))
					n++;
				else if(!done.contains(neigh)){
					int m = 0;
					for(Level.Location neigh2 : neighs(neigh))
						if(places.contains(neigh2))
							m++;
					if(m == 3)
						out.add(neigh);
					done.add(neigh);
				}
			}
			if(n == 2 || n == 3)
				out.add(ll);
		}
		for(Level.Location ll : out)
			ll.put(new Cell(world, id));
	}

	private static Iterable<Level.Location> neighs(Level.Location ll){
		List<Level.Location> out = new LinkedList<Level.Location>();
		for(int x = -1; x < 2; x++)
			for(int y = -1; y < 2; y++)
				if(x != 0 || y != 0)
					out.add(ll.level().location(new Position(ll.position().x() + x, ll.position().y() + y), Direction.NORTH));
		return out;
	}

	public static class Controller extends AbstractGameThing {
		public static void makeSerializer(SerializerUnion<GameThing> union, final GameWorld world){
			union.addIdentifier(new SerializerUnion.Identifier<GameThing>(){
				public String type(GameThing g){
					return g instanceof Controller? "goltroller" : null;
				}
			});

			union.addSerializer("goltroller", new Serializer<GameThing>(){
				public Tree write(GameThing o){
					Controller in = (Controller)o;
					Tree out = new Tree();
					out.add(new Tree.Entry("id", Serializers.Serializer_Long.write(in.id)));
					return out;
				}

				public GameThing read(Tree in) throws ParseException {
					return new Controller(world, Serializers.Serializer_Long.read(in.find("id")));
				}
			});
		}

		private final long id;
		private boolean toggle;

		public Controller(GameWorld world){
			this(world, GameWorld.someUnusedID(allCells));
		}

		private Controller(GameWorld world, long i){
			super(world);
			id = i;
			allCells.put(id, new HashSet<Cell>());
			update();
		}

		public List<String> interactions(){
			List<String> out = new LinkedList<String>();
			if(toggle){
				out.add("tick");
				out.add("get cell");
			}else{
				out.add("get cell");
				out.add("tick");
			}
			out.add("toggle");
			out.add("clear");
			out.addAll(super.interactions());
			return out;
		}

		public void interact(final String name, final Player who){
			Location l = location();
			if(l instanceof Level.Location)
				who.moveTo((Level.Location)l, 1, new Runnable(){
					public void run(){
						if(name.equals("get cell"))
							who.receiveItem(new Cell(world(), id));
						else if(name.equals("tick"))
							tick(world(), id);
						else if(name.equals("toggle")){
							toggle ^= true;
							update();
						}else if(name.equals("clear")){
							for(Cell c : new HashSet<Cell>(allCells.get(id)))
								if(c.location() instanceof Level.Location){
									LocationS.NOWHERE.put(c);
									world().forget(c);
								}
						}else
							Controller.super.interact(name, who);
					}
				});
		}

		public String renderer(){
			return "chest_3_open";
		}

		public boolean canWalkInto(Direction d, Character w){
			return false;
		}

		public String name(){
			return "Goltroller (" + id + ")";
		}

		public int renderLevel(){
			return ui.isometric.abstractions.IsoSquare.FURNATURE;
		}
	}

	public static class Cell extends PickupGameThing {
		public static void makeSerializer(SerializerUnion<GameThing> union, final GameWorld world){
			union.addIdentifier(new SerializerUnion.Identifier<GameThing>(){
				public String type(GameThing g){
					return g instanceof Cell? "golcell" : null;
				}
			});

			union.addSerializer("golcell", new Serializer<GameThing>(){
				public Tree write(GameThing o){
					Cell in = (Cell)o;
					Tree out = new Tree();
					out.add(new Tree.Entry("id", Serializers.Serializer_Long.write(in.id)));
					return out;
				}

				public GameThing read(Tree in) throws ParseException {
					return new Cell(world, Serializers.Serializer_Long.read(in.find("id")));
				}
			});
		}

		private final long id;

		public Cell(GameWorld world, long i){
			super(world);
			id = i;
			if(!allCells.containsKey(id))
				allCells.put(id, new HashSet<Cell>());
			allCells.get(id).add(this);
			update();
		}

		public String renderer(){
			return "armour_tunic";
		}

		public String name(){
			return "Golcell (" + id + ")";
		}

		public void forget(){
			allCells.get(id).remove(this);
			super.forget();
		}
	}
}
