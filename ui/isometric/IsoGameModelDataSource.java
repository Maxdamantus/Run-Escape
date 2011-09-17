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
	private Area viewArea;
	private IsoSquare[][] squares = new IsoSquare[0][0];
	private ReentrantReadWriteLock cacheChange = new ReentrantReadWriteLock();
	private IsoRendererLibrary rendererLibrary = new IsoRendererLibrary();
	private IsoSquare emptySquare = new IsoSquare();
	private Direction viewDirection;
	
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
		IsoSquare tmp = squares[x][y];
		cacheChange.readLock().unlock();
		if(tmp == null) {
			tmp = emptySquare;
		}
		return tmp;
	}

	@Override
	public void setViewableRect(int xOrigin, int yOrigin, int width, int height, Direction direction) {
		cacheChange.writeLock().lock();
		Area oldArea = viewArea;
		viewArea = new Area(xOrigin, yOrigin, width, height);
		viewDirection = direction;
		
		if(!oldArea.equals(viewArea)) {
			this.resizeCache();
		}
		cacheChange.writeLock().unlock();
	}
	
	private void resizeCache() {
		IsoSquare[][] tmp = new IsoSquare[viewArea.width()][viewArea.height()];
		
		cacheChange.writeLock().lock();
		for(int x = 0; x < tmp.length; x++) {
			if(x < viewArea.width()) {
				for(int y = 0; y < tmp[x].length; y++) {
					if(y < viewArea.height()) {
						tmp[x][y] = squares[x][y];
					}
				}
			}
		}
		cacheChange.writeLock().unlock();
	}
	
	@Override
	public void update() {
		cacheChange.writeLock().lock();
		Iterable<GameThing> things = gameModel.thingsInRect(viewArea);
		for(GameThing thing : things) {
			Position pos = this.transform(thing);
			IsoSquare square = squares[pos.x()][pos.y()];
			if(square == null) {
				square = new IsoSquare();
			}
			square.addImageForLevel(rendererLibrary.newImageFromGameThing(thing, viewDirection), rendererLibrary.levelFromArguments(thing.userArguments()));
		}
		cacheChange.writeLock().unlock();
	}

	private Position transform(GameThing thing) {
		int x = thing.area().x() - viewArea.x();
		int y = thing.area().y() - viewArea.y();
		int w = thing.area().width();
		int h = thing.area().height();
		
		switch(viewDirection) {
			case NORTH:
				return new Position(x, y);
			case EAST:
				return new Position(w-y, x);
			case SOUTH:
				return new Position(w-x, h-y);
			case WEST:
				return new Position(y, w-x);
		}
		
		return null;
	}
}
