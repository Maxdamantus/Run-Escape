package game.things;

import game.*;
import util.*;

import java.util.*;

public class GOL {
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
				c.forget();
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
				if(x != 0 && y != 0)
					out.add(ll.level().location(new Position(ll.position().x() + x, ll.position().y() + y), Direction.NORTH));
		return out;
	}

	public static class Controller extends AbstractGameThing {
		private final long id;

		public Controller(GameWorld world){
			super(world);
			id = GameWorld.someUnusedID(allCells);
			allCells.put(id, new HashSet<Cell>());
		}

		public List<String> interactions(){
			List<String> out = new LinkedList<String>();
			out.add("tick");
			out.add("get cell");
			out.addAll(super.interactions());
			return out;
		}

		public void interact(String name, final Player who){
			if(name.equals("get cell")){
				Location l = location();
				if(l instanceof Level.Location)
					who.moveTo((Level.Location)l, 1, new Runnable(){
						public void run(){
							who.receiveItem(new Cell(world(), id));
						}
					});
			}else if(name.equals("tick")){
				Location l = location();
				if(l instanceof Level.Location)
					who.moveTo((Level.Location)l, 1, new Runnable(){
						public void run(){
							tick(world(), id);
						}
					});
			}else
				super.interact(name, who);
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
		private final long id;

		public Cell(GameWorld world, long i){
			super(world);
			id = i;
			if(!allCells.containsKey(id))
				allCells.put(id, new HashSet<Cell>());
			allCells.get(id).add(this);
		}

		public String renderer(){
			return "armour_tunic";
		}

		public String name(){
			return "Golcell(" + id + ")";
		}

		public void forget(){
			allCells.get(id).remove(this);
			super.forget();
		}

		public int renderLevel(){
			return ui.isometric.abstractions.IsoSquare.PICKUP_ITEM;
		}
	}
}
