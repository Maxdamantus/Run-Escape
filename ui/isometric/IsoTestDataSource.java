package ui.isometric;

import java.awt.Point;

import util.Area;
import util.Direction;
import util.Position;

/**
 * 
 * A test implementation of IsoDataSource so that IsoCanvas can be tested
 * 
 * @author melby
 *
 */
public class IsoTestDataSource implements IsoDataSource {
	private IsoImage floor = new IsoImage("/resources/isotiles/test/floor.png");
	private IsoImage floorpool = new IsoImage("/resources/isotiles/test/floorpool.png");
	private IsoImage floorpost = new IsoImage("/resources/isotiles/test/floorpost.png");
	private IsoImage floorstone = new IsoImage("/resources/isotiles/test/floorstone.png");
	private IsoImage floortree = new IsoImage("/resources/isotiles/test/floortree.png");
	private IsoImage wally = new IsoImage("/resources/isotiles/test/wally.png");
	private IsoImage wallx = new IsoImage("/resources/isotiles/test/wallx.png");
	private IsoImage wallcross = new IsoImage("/resources/isotiles/test/wallcross.png");
	
	/**
	 * Dummy transform class that doesn't do anything
	 * @author melby
	 *
	 */
	private class IsoTransformDummy implements IsoTransform {
		@Override
		public Area querryArea() {
			return new Area(0, 0, 100, 100);
		}

		@Override
		public Position transformMapPosition(Position pos) {
			return pos;
		}

		@Override
		public Point transformRelitivePoint(Point p) {
			return p;
		}

		@Override
		public Point smoothOrigin(Point origin) {
			return new Point(0, 0);
		}
	}
	
	@Override
	public IsoSquare squareAt(int x, int y) {
		IsoSquare s = new IsoSquare();
		
		if(x % 6 == 0 && y % 6 == 0) {
			s.addImageForLevel(floor, IsoSquare.FLOOR);
			s.addImageForLevel(wallcross, IsoSquare.WALL);
		}
		else if(x % 6 == 0) {
			s.addImageForLevel(floor, IsoSquare.FLOOR);
			s.addImageForLevel(wallx, IsoSquare.WALL);
		}
		else if(y % 6 == 0) {
			s.addImageForLevel(floor, IsoSquare.FLOOR);
			s.addImageForLevel(wally, IsoSquare.WALL);
		}
		else {
			int n = (int) (Math.random() * 5);
						
			switch(n) {
			case 0:
				s.addImageForLevel(floor, IsoSquare.FLOOR);
				s.addImageForLevel(floortree, IsoSquare.FLOOR);
				break;
			case 1:
				s.addImageForLevel(floor, IsoSquare.FLOOR);
				break;
			case 2:
				s.addImageForLevel(floorpool, IsoSquare.FLOOR);
				break;
			case 3:
				s.addImageForLevel(floor, IsoSquare.FLOOR);
				s.addImageForLevel(floorpost, IsoSquare.FLOOR);
				break;
			case 4:
				s.addImageForLevel(floorstone, IsoSquare.FLOOR);
				break;
			}
		}
		
		return s;
	}

	@Override
	public void setViewableRect(int x, int y, int w, int h, Direction viewDirection) {
		// Don't need this info atm
	}

	@Override
	public void update() {
		// Don't need to do anything
	}

	@Override
	public IsoTransform transform() {
		return new IsoTransformDummy();
	}
}
