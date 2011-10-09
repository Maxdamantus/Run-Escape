package ui.isometric.datasource;

import ui.isometric.abstractions.IsoPlayer;
import game.GameWorld;
import game.Level;

/**
 * A IsoGameModelDataSource that shows the level of the current player
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
}
