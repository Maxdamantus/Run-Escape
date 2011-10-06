package ui.isometric.abstractions;

import ui.isometric.IsoInterface;
import game.Container;
import game.GameThing;
import game.GameWorld;
import game.WorldDelta;
import game.things.EquipmentGameThing;

/**
 * Wrapper around the needlessly complex methods for getting info about the current player
 * 
 * @author melby
 *
 */
public class IsoPlayer {
	private Container inventory;
	private Container equipment;
	private GameWorld world;
	private GameThing thing;
	
	/**
	 * Create an IsoPlayer with a given world, player GameThing and interface
	 * @param world
	 * @param thing
	 */
	public IsoPlayer(final GameWorld world, GameThing thing, IsoInterface inter) {
		this.world = world;
		this.thing = thing;
		
		world.addDeltaWatcher(new GameWorld.DeltaWatcher() {
			@Override
			public void delta(WorldDelta delta) {
				WorldDelta.Action action = delta.action();
				if(action instanceof WorldDelta.ShowContainer){
					WorldDelta.ShowContainer show = (WorldDelta.ShowContainer)action;
					if(show.what().equals("Inventory")) {
						inventory = show.which(world);
					}
					if(show.what().equals("Equipment")) {
						equipment = show.which(world);
					}
				}
			}
		});
		
		inter.performActionOn("_showinventory", thing);
		inter.performActionOn("_showequipment", thing);
	}
	
	/**
	 * This players inventory
	 * @return
	 */
	public Container inventory() {
		return inventory;
	}
	
	/**
	 * Get the GameThing for a given slot
	 * @param slot
	 * @return
	 */
	public GameThing getEquipmentForSlot(EquipmentGameThing.Slot slot) {
		if(equipment != null) {
			for(GameThing g : equipment) {
				String value = g.info().get(EquipmentGameThing.SLOT);
				if(value != null) {
					EquipmentGameThing.Slot got = EquipmentGameThing.Slot.valueOf(value);
					if(got.equals(slot)) {
						return g;
					}
				}
			}
		}
		
		return null;
	}
	
	/**
	 * This players equipment
	 * @return
	 */
	public Container equipment() {
		return equipment;
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
