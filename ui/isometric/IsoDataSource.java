package ui.isometric;

import game.Level;
import ui.isometric.abstractions.IsoSquare;
import util.Direction;


/**
 * 
 * An interface specifying a datasource for IsoCanvas to use for rendering
 * 
 * @author melby
 *
 */
public interface IsoDataSource {
	/**
	 * Get a square at a given location, relative to the view origin.
	 * 0x0 is the top left corner, y+ is top right, y- is bottom left, x increses towards the bottom right
	 * @param x - the x coord
	 * @param y - the y coord
	 * @return
	 */
	public IsoSquare squareAt(int x, int y);
	
	/**
	 * Set the current viewable rect, this allows caching to be done more efficiently if needed.
	 * This method must be called when the view changes, as a call to squareAt outside
	 * this rect will result in an exception
	 * Note: the parameters are window pixel sizes, not tile sizes
	 * Note: this method may clear any cached content, so should be followed by a call to update (this will not be done automatically)
	 * @param x - origin x
	 * @param y - origin y
	 * @param w - width
	 * @param h - height
	 * @param viewDirection - the direction the view is viewing from
	 */
	public void setViewableRect(int x, int y, int w, int h, Direction viewDirection);
	
	/**
	 * Tell the datasource to update, ie next frame or the world has changed in some way
	 */
	public void update();
	
	/**
	 * Get a transform object to be able to transform such things as mouse drags relative to the dataSource
	 * @return
	 */
	public IsoTransform transform();

	/**
	 * The level currently being displayed
	 * @return
	 */
	public Level level();
}
