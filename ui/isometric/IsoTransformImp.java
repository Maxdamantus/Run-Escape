package ui.isometric;

import java.awt.Point;

import util.Area;
import util.Direction;
import util.Position;

/**
 * 
 * A full implementation of the IsoTransform interface, has rotation/translation etc
 * Goes from view pixel coordinates to map coordinates
 * 
 * @author melby
 *
 */
public class IsoTransformImp implements IsoTransform {
	private Direction viewDirection;
	private Area querryArea;
	
	private Position viewOrigin;
	private Position bottomRight;
	private Position topRight;
	private Position bottomLeft;
	
	/**
	 * Create a IsoTransformImp with a views pixel coordinates
	 * @param xOrigin - the x pixel offset of the origin
	 * @param yOrigin - the y pixel offset of the origin
	 * @param width - the pixel width of the view
	 * @param height - the pixel height of the view
	 * @param direction - the direction the view is being viewed towards
	 */
	public IsoTransformImp(int xOrigin, int yOrigin, int width, int height, Direction direction) {
		int minyy = -height/IsoCanvas.TILE_Y;
		int maxyy = width/IsoCanvas.TILE_X + 1;
		int maxxx = width/IsoCanvas.TILE_X + height/IsoCanvas.TILE_Y;
		int maxxy = width/IsoCanvas.TILE_X - height/IsoCanvas.TILE_Y;
		int maxyx = width/IsoCanvas.TILE_X;
		int minyx = height/IsoCanvas.TILE_Y;
		
		bottomRight = new Position(maxxx, maxxy);
		topRight = new Position(maxyx, maxyy);
		bottomLeft = new Position(minyx, minyy);
		
		int xDivide;
		int yDivide;
		if(direction == Direction.NORTH || direction == Direction.SOUTH) {
			xDivide = IsoCanvas.TILE_X;
			yDivide = IsoCanvas.TILE_Y;
		}
		else {
			xDivide = IsoCanvas.TILE_Y;
			yDivide = IsoCanvas.TILE_X;
		}
		viewOrigin = new Position(xOrigin/xDivide-yOrigin/yDivide, yOrigin/yDivide+xOrigin/xDivide);
		
		querryArea = new Area(xOrigin/IsoCanvas.TILE_X, minyy, maxxx, maxyy-minyy);
		viewDirection = direction;
	}

	@Override
	public Area querryArea() {
		return querryArea;
	}

	@Override
	public Position transformMapPosition(Position pos) {
		int x = pos.x() - viewOrigin.x();
		int y = pos.y() - viewOrigin.y();
		
		switch(viewDirection) {
			case NORTH:
				return new Position(x, y);
			case EAST:
				return new Position(bottomLeft.x()-y, bottomLeft.y()+x);
			case SOUTH:
				return new Position(bottomRight.x()-x, bottomRight.y()-y);
			case WEST:
				return new Position(topRight.x()+y, topRight.y()-x);
		}
		
		return pos; // Can't do anything anyway
	}
	
	@Override
	public Point transformRelitivePoint(Point p) {
		int x = (int)p.getX();
		int y = (int)p.getY();
		
		switch(viewDirection) {
			case NORTH:
				return p;
			case EAST:
				return new Point(-y, x);
			case SOUTH:
				return new Point(-x, -y);
			case WEST:
				return new Point(+y, -x);
		}
		
		return p; // Nothing else to do
	}

	@Override
	public Point smoothOrigin(Point origin) {
		int x1 = -(int) (origin.getX()%IsoCanvas.TILE_X);
		int y1 = (int) (origin.getY()%IsoCanvas.TILE_Y);
		int x2 = -(int) (origin.getX()%IsoCanvas.TILE_Y);
		int y2 = (int) (origin.getY()%IsoCanvas.TILE_X);
		
		switch(viewDirection) {
			case NORTH:
				return new Point(x1, y1);
			case EAST:
				return new Point(y2, -x2);
			case SOUTH:
				return new Point(-x1, -y1);
			case WEST:
				return new Point(-y2, x2);
		}
		
		return new Point(0, 0); // No other options
	}
}
