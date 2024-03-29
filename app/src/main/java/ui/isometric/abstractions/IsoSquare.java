package ui.isometric.abstractions;

import game.Level;
import game.Level.Location;

import java.util.*;

/**
 * 
 * This class represents a single square for an isometric renderer, each square can contain many images
 * 
 * @author melby
 *
 */
public class IsoSquare implements Iterable<IsoObject> {

	public static final int GROUND = 0;
	public static final int GROUND_HIGHLIGHT = 50;
	public static final int PICKUP_ITEM = 100;
	public static final int FURNATURE = 200;
	public static final int WALL = 1000;
	public static final int SPAWN_POINT = 1500;
	public static final int CHARACTER = 2000;
	public static final int DROP = 100;
	
	private ArrayList<ImageLevel> images = new ArrayList<ImageLevel>();
	private Location location;
	
	/**
	 * Represents an image level on a square, used for sorting etc
	 * @author melby
	 *
	 */
	private class ImageLevel {
		private IsoObject image;
		private int level;
		
		private ImageLevel(IsoObject image, int level) {
			this.image = image;
			this.level = level;
		}
	}
	
	/**
	 * Compares ImageLevels
	 * @author melby
	 *
	 */
	private class ImageLevelComparator implements Comparator<ImageLevel> {
		@Override
		public int compare(ImageLevel arg0, ImageLevel arg1) {
			return arg0.level - arg1.level;
		}
	}
	
	/**
	 * An iterator that through the images on a square in level order from bottom to top
	 * @author melby
	 *
	 */
	private class ImageIterator implements Iterator<IsoObject> {
		Iterator<ImageLevel> iterator = images.iterator();
		
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public IsoObject next() {
			return iterator.next().image;
		}

		@Override
		public void remove() { }
	}
	
	/**
	 * Create a IsoSquare with a given location
	 * @param location
	 */
	public IsoSquare(Level.Location location) {
		this.location = location;
	}
	
	/**
	 * Add a given IsoImage to at a given level on this square
	 * @param image
	 * @param level
	 */
	public void addImageForLevel(IsoObject image, int level) { // TODO: insert rather than sort?
		images.add(new ImageLevel(image, level));
		Collections.sort(images, new ImageLevelComparator());
	}
	
	/**
	 * Iterate through the images on a square in level order from bottom to top
	 */
	@Override
	public Iterator<IsoObject> iterator() {
		return new ImageIterator();
	}
	
	/**
	 * The number of images on this square
	 * @return
	 */
	public int numberOfImages() {
		return images.size();
	}

	/**
	 * Get the location of this square
	 * @return
	 */
	public Level.Location location() {
		return location;
	}
}
