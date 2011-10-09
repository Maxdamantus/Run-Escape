package ui.isometric.abstractions;

import java.awt.image.BufferedImage;

import game.GameThing;

import ui.isometric.libraries.IsoRendererLibrary.RendererImage;

/**
 * 
 * A GameThing/Image abstraction for isometric rendering
 * 
 * @author melby
 *
 */
public class IsoObject {
	private RendererImage image;
	private GameThing thing = null;
	private IsoSquare square;
	private int yoffset = 0;

	/**
	 * Create an IsoObject with a image, an existing image, square it is on and y-offset
	 * @param thing
	 * @param rendererImage
	 * @param square
	 * @param yoff
	 */
	public IsoObject(GameThing thing, RendererImage rendererImage, IsoSquare square, int yoff) {
		this.square = square;
		this.image = rendererImage;
		this.yoffset = yoff;
		this.thing = thing;
	}

	/**
	 * Get the image that this IsoObject wraps
	 * @return
	 */
	public BufferedImage image() {
		return image.image();
	}
	
	/**
	 * Get the width that this object should be displayed at
	 * @return
	 */
	public int width() {
		return image.width();
	}
	
	/**
	 * Get the height this object should be displayed at
	 * @return
	 */
	public int height() {
		return image.height();
	}

	/**
	 * Get the GameThing that this IsoObject represents
	 * @return
	 */
	public GameThing gameThing() {
		return thing;
	}

	/**
	 * Get the IsoObject that this image is on
	 * @return
	 */
	public IsoSquare square() {
		return square;
	}
	
	/**
	 * Get the y-offset to renderer this object at
	 * @return
	 */
	public int yoffset() {
		return yoffset;
	}
	
	/**
	 * Get the RendererImage that backs this IsoObject
	 * @return
	 */
	protected RendererImage rawImage() {
		return image;
	}
	
	/**
	 * Does this IsoObject have a health attribute
	 * @return
	 */
	public boolean hasHealth() {
		if(thing != null) {
			if(thing.info().get("health") != null) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * How much health this object has
	 * @return
	 */
	public double health() {
		if(thing != null) {
			String value = thing.info().get("health");
			if(value != null) {
				try {
					return Double.valueOf(value);
				}
				catch(NumberFormatException e) {};
			}
		}
		
		return 1;
	}
}
