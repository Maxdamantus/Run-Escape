package ui.isometric.datasource;

import ui.isometric.abstractions.IsoObject;
import ui.isometric.abstractions.IsoPlayer;
import ui.isometric.abstractions.IsoSquare;
import ui.isometric.libraries.IsoRendererLibrary;
import util.Direction;
import game.GameThing;
import game.GameWorld;
import game.Level;

/**
 * A IsoGameModelDataSource that shows the level of the current player
 * and highlights it in the world
 * 
 * @author melby
 *
 */
public class IsoPlayerDataSource extends IsoGameModelDataSource {
	private IsoPlayer player;
	
	/**
	 * Create a IsoPlayerDataSource with a given player to use the level from
	 * @param model
	 * @param p
	 */
	public IsoPlayerDataSource(GameWorld model, IsoPlayer p) {
		super(model);
		player = p;
	}

	@Override
	public Level level() {
		return player.location().level();
	}
	
	@Override
	protected void configureSquare(IsoSquare square, GameThing thing) {
		if(thing == player.thing()) { // TODO: Always safe?
			square.addImageForLevel(new IsoObject(thing, IsoRendererLibrary.imageForRendererName("player_highlight", Direction.NORTH), square, 0), IsoSquare.GROUND_HIGHLIGHT);
		}
		
		super.configureSquare(square, thing);
	}
}
