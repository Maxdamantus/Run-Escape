package ui.isometric;

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
	 * @param x - the x coord
	 * @param y - the y coord
	 * @return
	 */
	public IsoSquare squareAt(int x, int y);
	
	/**
	 * Set the current viewable rect, this allows caching to be done more efficiently if needed.
	 * This method must be called when the view changes, as a call to squareAt outside
	 * this rect will result in an exception
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
}
