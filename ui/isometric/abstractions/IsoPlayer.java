package ui.isometric.abstractions;

import ui.isometric.IsoInterface;
import game.Container;
import game.GameThing;
import game.GameWorld;
import game.WorldDelta;

/**
 * Wrapper around the needlessly complex methods of getting info about the current player
 * 
 * @author melby
 *
 */
public class IsoPlayer {
	private Container inventory;
	private GameWorld world;
	private GameThing thing;
	private IsoInterface inter;
	
	/**
	 * Create an IsoPlayer with a given world, player GameThing and interface
	 * @param world
	 * @param thing
	 */
	public IsoPlayer(final GameWorld world, GameThing thing, IsoInterface inter) {
		this.world = world;
		this.thing = thing;
		this.inter = inter;
		
		world.addDeltaWatcher(new GameWorld.DeltaWatcher() {
			@Override
			public void delta(WorldDelta delta) {
				WorldDelta.Action action = delta.action();
				if(action instanceof WorldDelta.ShowContainer){
					WorldDelta.ShowContainer show = (WorldDelta.ShowContainer)action;
					if(show.what().equals("Inventory")) {
						inventory = show.which(world);
					}
				}
			}
		});
		
		inter.performActionOn("_showinventory", thing);
	}
	
	/**
	 * This players inventory
	 * @return
	 */
	public Container inventory() {
		return inventory;
	}
	
	/**
	 * The raw GameThing that backs this
	 * @return
	 */
	public GameThing thing() {
		return thing;
	}
	
	/**
	 * The world this player is in
	 * @return
	 */
	public GameWorld world() {
		return world;
	}

	/**
	 * Get the name of the character this player is using
	 * @return
	 */
	public String characterName() {
		// TODO: actually get this to work
		return "bob";
	}
}
