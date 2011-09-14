package ui.isometric;

import java.util.*;

/**
 * 
 * This class represents a single square for an isometric renderer, each square can contain many images
 * 
 * @author melby
 *
 */
public class IsoSquare implements Iterable<IsoImage> {

	public static final int FLOOR = 0;
	public static final int WALL = 1000;
	
	private ArrayList<ImageLevel> images = new ArrayList<ImageLevel>();
	
	private class ImageLevel {
		private IsoImage image;
		private int level;
		
		private ImageLevel(IsoImage image, int level) {
			this.image = image;
			this.level = level;
		}
	}
	
	private class ImageLevelComparator implements Comparator<ImageLevel> {
		@Override
		public int compare(ImageLevel arg0, ImageLevel arg1) {
			return arg0.level - arg1.level;
		}
	}
	
	private class ImageIterator implements Iterator<IsoImage> {
		Iterator<ImageLevel> iterator = images.iterator();
		
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public IsoImage next() {
			return iterator.next().image;
		}

		@Override
		public void remove() {
			// Does nothing
		}
	}
	
	/**
	 * Add a given IsoImage to at a given level on this square
	 * @param image
	 * @param level
	 */
	public void addImageForLevel(IsoImage image, int level) {
		images.add(new ImageLevel(image, level));
		Collections.sort(images, new ImageLevelComparator());
	}
	
	@Override
	public Iterator<IsoImage> iterator() {
		return new ImageIterator();
	}
}
