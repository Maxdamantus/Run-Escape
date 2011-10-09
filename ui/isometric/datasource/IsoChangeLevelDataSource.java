package ui.isometric.datasource;

import game.GameWorld;
import game.Level;

/**
 * A data source which allows you to change the level it is drawing
 * 
 * @author melby
 *
 */
public class IsoChangeLevelDataSource extends IsoGameModelDataSource {
	private Level level;
	private GameWorld world;
	
	/**
	 * Create a IsoChangeLevelDataSource with the default level (0)
	 * @param model
	 */
	public IsoChangeLevelDataSource(GameWorld model) {
		this(model, 0);
	}
	
	/**
	 * Create a IsoChangeLevelDataSource with the given world and level
	 * @param model
	 * @param startLevel
	 */
	public IsoChangeLevelDataSource(GameWorld model, int startLevel) {
		super(model);
		level = model.level(startLevel);
		world = model;
	}

	@Override
	public Level level() {
		return level;
	}

	/**
	 * Set the level to display
	 * @param l
	 */
	public void setLevel(Level l) {
		level = l;
	}
	
	/**
	 * Set the level to display
	 * @param l
	 */
	public void setLevel(int l) {
		level = world.level(l);
	}
}
