package ui.isometric.datasource;

import java.util.Set;

import game.Level;
import ui.isometric.IsoTransform;
import ui.isometric.abstractions.IsoSquare;
import util.Direction;
import util.Position;


/**
 * 
 * An interface specifying a datasource for IsoCanvas to use for rendering
 * 
 * @author melby
 *
 */
public interface IsoDataSource {
	/**
	 * A data structure to declare the position of lights
	 * 
	 * @author melby
	 *
	 */
	public class Light {
		private double radius;
		private Position position;
		
		/**
		 * Create a light with the given radius and position
		 * @param radius
		 * @param position
		 */
		public Light(double radius, Position position) {
			this.radius = radius;
			this.position = position;
		}
		
		/**
		 * Get the radius of this light
		 * @return
		 */
		public double radius() {
			return radius;
		}
		
		/**
		 * Get the position of this light in the map
		 * @return
		 */
		public Position position() {
			return position;
		}
	}
	
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
	
	/**
	 * Get weather this level is dark or not
	 * @return
	 */
	public boolean levelIsDark();
	
	/**
	 * Get the set of all lights.
	 * Note, can't be null
	 * @return
	 */
	public Set<Light> lights();
}
