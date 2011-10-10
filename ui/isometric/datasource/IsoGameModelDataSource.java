package ui.isometric.datasource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import ui.isometric.IsoTransform;
import ui.isometric.IsoTransformImp;
import ui.isometric.abstractions.IsoObject;
import ui.isometric.abstractions.IsoSquare;
import ui.isometric.libraries.IsoRendererLibrary;
import util.Area;
import util.Direction;
import util.Position;

import game.*;

/**
 * 
 * A class that provides data from a GameWorld to an IsoCanvas
 * 
 * @author melby
 *
 */
abstract public class IsoGameModelDataSource implements IsoDataSource {
	private GameWorld gameWorld;
	private IsoSquare[][] squares = null;
	private ReentrantReadWriteLock cacheChange = new ReentrantReadWriteLock();
	
	private Direction viewDirection;
	private Area querryArea;
	private IsoTransformImp transform;
	
	private int arrayPaddingY = 100; // TODO: calculate
	private int arrayPaddingX = 100; // TODO: calculate
	
	private Map<Long, Animation> animations = new HashMap<Long, Animation>();
	private ReadWriteLock animationsLock = new ReentrantReadWriteLock();
	
	private Set<Light> lights = new HashSet<Light>();
	
	private static final double ANIMATION_FPS = 20;
	
	private class Animation {
		private String renderer;
		private long startTime;
		
		public Animation(String renderer) {
			this.renderer = renderer;
			this.startTime = System.currentTimeMillis();
		}
		
		public long startTime() {
			return startTime;
		}
		
		public String renderer() {
			return renderer;
		}
	}
	
	/**
	 * Create a IsoGameModelDataSource with a given GameWorld
	 * @param model
	 */
	public IsoGameModelDataSource(GameWorld model) {
		gameWorld = model;
		gameWorld.addDeltaWatcher(new GameWorld.DeltaWatcher() {
			@Override
			public void delta(WorldDelta d) {
				if(d.action() instanceof WorldDelta.Animate) {
					WorldDelta.Animate animate = (WorldDelta.Animate)d.action();
					GameThing t = animate.which(gameWorld);
					String r = animate.what();
															
					animationsLock.writeLock().lock();
					animations.put(t.gid(), new Animation(r));
					animationsLock.writeLock().unlock();
				}
			}
		});
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
		return tmp;
	}

	@Override
	public void setViewableRect(int xOrigin, int yOrigin, int width, int height, Direction direction) {
		transform = new IsoTransformImp(xOrigin, yOrigin, width, height, direction);
		viewDirection = direction;
		Area oldArea = querryArea;
		querryArea = transform.querryArea();
		
		if(oldArea == null || !oldArea.equals(querryArea)) {
			cacheChange.writeLock().lock();
			this.clearCache();
			cacheChange.writeLock().unlock();
		}
	}
	
	/**
	 * Clear and resize the internal cache to the size in querryArea at least
	 */
	private void clearCache() {
		IsoSquare[][] tmp = new IsoSquare[querryArea.width()+arrayPaddingX*2][querryArea.height()+arrayPaddingY*2];
		
		cacheChange.writeLock().lock();
		squares = tmp;
		lights.clear();
		cacheChange.writeLock().unlock();
	}
	
	@Override
	public void update() {
		cacheChange.writeLock().lock();
		this.clearCache();
		
		Iterable<GameThing> things = this.level().portion(querryArea);
		for(GameThing thing : things) {
			Location l = thing.location();
			if(l instanceof Level.Location) {
				Position pos = transform.transformMapToView(((Level.Location)l).position());
				IsoSquare square = squares[pos.x()+arrayPaddingX][pos.y()+arrayPaddingY];
				if(square == null) {
					square = new IsoSquare((Level.Location)l);
				}
				
				this.configureSquare(square, thing);
				
				squares[pos.x()+arrayPaddingX][pos.y()+arrayPaddingY] = square;
				
				if(this.isLight(thing)) {
					lights.add(this.lightForThing(thing));
				}
			}
		}
		cacheChange.writeLock().unlock();
	}
	
	/**
	 * Get a Light for a given thing
	 * @param thing
	 * @return
	 */
	private Light lightForThing(GameThing thing) {
		String luminance = thing.info().get("luminance");
		
		if(luminance != null) {
			try {
				double radius = Integer.valueOf(luminance);
				
				if(thing.location() instanceof Level.Location) {
					Position point = ((Level.Location)thing.location()).position();
					
					return new Light(radius, point);
				}
			}
			catch (NumberFormatException e) {}
		}
		
		return null;
	}

	/**
	 * Check to see if the given thing is a light
	 * @param thing
	 * @return
	 */
	private boolean isLight(GameThing thing) {
		return thing.info().get("luminance") != null;
	}

	/**
	 * Configure a square with a given thing.
	 * Note: default just adds the thing to the square,
	 * manages animations etc, so be sure to call
	 * super in subclasses
	 * @param square
	 * @param thing
	 */
	protected void configureSquare(IsoSquare square, GameThing thing) {
		animationsLock.readLock().lock();
		Animation animate = animations.get(thing.gid());
		animationsLock.readLock().unlock();
		if(animate == null) {
			IsoObject image = IsoRendererLibrary.newImageFromGameThing(square, thing, viewDirection);
			square.addImageForLevel(image, thing.renderLevel());
		}
		else {
			IsoRendererLibrary.RendererImage animation = IsoRendererLibrary.imageForRendererName(animate.renderer(), viewDirection);
								
			if(animate.startTime() < System.currentTimeMillis() - (animation.frameCount() / ANIMATION_FPS * 1000.0)) {						
				IsoObject image = IsoRendererLibrary.newImageFromGameThing(square, thing, viewDirection);
				square.addImageForLevel(image, thing.renderLevel());
				
				animationsLock.writeLock().lock(); // Animation stopped
				animations.remove(thing);
				animationsLock.writeLock().unlock();
			}
			else {
				int frame = (int) ((System.currentTimeMillis() - animate.startTime()) / 1000.0 * ANIMATION_FPS);
				IsoObject image = IsoRendererLibrary.newImageFromGameThing(square, thing, viewDirection, animate.renderer(), frame);
				square.addImageForLevel(image, thing.renderLevel());
			}
		}
	}

	@Override
	public IsoTransform transform() {
		return transform;
	}
	
	@Override
	public Set<Light> lights() {
		return lights;
	}
}
