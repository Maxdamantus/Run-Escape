package ui.isometric;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import util.Area;
import util.Direction;
import util.Position;

import clientinterface.GameModel;
import clientinterface.GameThing;

/**
 * 
 * A class that provides data from a GameModel to an IsoCanvas
 * 
 * @author melby
 *
 */
public class IsoGameModelDataSource implements IsoDataSource {
	private GameModel gameModel;
	private Area querryArea;
	private Position viewOrigin;
	private IsoSquare[][] squares = null;
	private ReentrantReadWriteLock cacheChange = new ReentrantReadWriteLock();
	private IsoRendererLibrary rendererLibrary = new IsoRendererLibrary();
	private IsoSquare emptySquare = new IsoSquare();
	private Direction viewDirection;
	
	private Position bottomRight;
	private Position topRight;
	private Position bottomLeft;
	
	private int arrayPaddingY = 100; // TODO: calculate
	private int arrayPaddingX = 100; // TODO: calculate
	
	/**
	 * Create a IsoGameModelDataSource with a given GameModel
	 * @param model
	 */
	public IsoGameModelDataSource(GameModel model) {
		gameModel = model;
	}
	
	@Override
	public IsoSquare squareAt(int x, int y) {
		cacheChange.readLock().lock();
		IsoSquare tmp;
		try {
			tmp = squares[x+arrayPaddingX][y+arrayPaddingY];
		}
		catch (IndexOutOfBoundsException e) {
			tmp = null;
		}
		cacheChange.readLock().unlock();
		if(tmp == null) {
			tmp = emptySquare;
		}
		return tmp;
	}

	@Override
	public void setViewableRect(int xOrigin, int yOrigin, int width, int height, Direction direction) {
		cacheChange.writeLock().lock();
		int minyy = -height/IsoCanvas.TILE_Y;
		int maxyy = width/IsoCanvas.TILE_X + 1;
		int maxxx = width/IsoCanvas.TILE_X + height/IsoCanvas.TILE_Y;
		int maxxy = width/IsoCanvas.TILE_X - height/IsoCanvas.TILE_Y;
		int maxyx = width/IsoCanvas.TILE_X;
		int minyx = height/IsoCanvas.TILE_Y;
		
		bottomRight = new Position(maxxx, maxxy);
		topRight = new Position(maxyx, maxyy);
		bottomLeft = new Position(minyx, minyy);
				
		Area oldArea = querryArea;
		viewOrigin = new Position(xOrigin/IsoCanvas.TILE_X, xOrigin/IsoCanvas.TILE_Y);
		querryArea = new Area(xOrigin/IsoCanvas.TILE_X, xOrigin/IsoCanvas.TILE_Y, maxxx, maxyy-minyy);
		viewDirection = direction;
		
		if(oldArea == null || !oldArea.equals(querryArea)) {
			this.resizeCache();
		}
		cacheChange.writeLock().unlock();
	}
	
	/**
	 * Resize the internal cache to the size in querryArea at least
	 */
	private void resizeCache() {
		IsoSquare[][] tmp = new IsoSquare[querryArea.width()+arrayPaddingX*2][querryArea.height()+arrayPaddingY*2];
		
		cacheChange.writeLock().lock();
		
		if(squares != null) {
			for(int x = 0; x < tmp.length; x++) {
				if(x < querryArea.width()) {
					for(int y = 0; y < tmp[x].length; y++) { // TODO: negative?
						if(y < querryArea.height()) {
							tmp[x][y] = squares[x][y];
						}
					}
				}
			}
		}
		
		squares = tmp;
		
		cacheChange.writeLock().unlock();
	}
	
	@Override
	public void update() {
		cacheChange.writeLock().lock();
		Iterable<GameThing> things = gameModel.thingsInRect(querryArea);
		for(GameThing thing : things) {
			Position pos = this.transform(thing);
			IsoSquare square = squares[pos.x()+arrayPaddingX][pos.y()+arrayPaddingY];
			if(square == null) {
				square = new IsoSquare();
			}
			square.addImageForLevel(rendererLibrary.newImageFromGameThing(thing, viewDirection), rendererLibrary.levelFromArguments(thing.userArguments()));
			squares[pos.x()+arrayPaddingX][pos.y()+arrayPaddingY] = square;
		}
		cacheChange.writeLock().unlock();
	}

	/**
	 * Translate/Rotate the coordinates of a given GameThing to the coordinates for the renderer
	 * @param thing
	 * @return
	 */
	private Position transform(GameThing thing) {
		int x = thing.position().x() - viewOrigin.x();
		int y = thing.position().y() - viewOrigin.y();
		
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
		
		return null;
	}
}
