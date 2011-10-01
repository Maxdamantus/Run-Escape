package ui.isometric;

import java.awt.Point;

import util.Area;
import util.Position;

/**
 * 
 * An interface for specifying classes that translate data based on IsoMetric rotation and translation
 * 
 * @author melby
 *
 */
public interface IsoTransform {
	/**
	 * The square area (diamond in respect to the view) of the map that is visible
	 * @return
	 */
	public Area querryArea();

	/**
	 * Perform translation/rotation on a map position into view coordinates
	 * @param pos
	 * @return
	 */
	public Position transformMapPosition(Position pos);
	
	/**
	 * Transform a view position to map position
	 * @param pos
	 * @return
	 */
	public Position transformViewPosition(Position pos);
	
	/**
	 * Perform translation/rotation on a given relative or delta Point, eg mouse drags
	 * @param p
	 * @return
	 */
	public Point transformRelativePoint(Point p);

	/**
	 * Given a pixel accurate origin, calculate the offset that tiles should be rendered at
	 * @param origin
	 * @return
	 */
	public Point smoothOrigin(Point origin);
}
