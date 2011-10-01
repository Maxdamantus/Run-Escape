package ui.isometric;

import java.awt.image.BufferedImage;
import java.io.IOException;

import game.GameThing;

import util.*;

/**
 * 
 * An image abstraction for isometric rendering
 * 
 * @author melby
 *
 */
public class IsoImage {
	private BufferedImage image;
	private GameThing gameThing = null;
	private IsoSquare square;
	private int yoffset = 0;
	
	/**
	 * Create an IsoImage with a given resource path and square that it is on
	 * @param path
	 * @param square
	 * @param name
	 */
	public IsoImage(String path, IsoSquare square) {		
		try {
			this.image = Resources.readImageResourceUnfliped(path);
		} catch (IOException e) {
			System.err.println("Unable to load: " + path);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Create an IsoImage with an existing image, square it is on and y-offset
	 * @param image
	 * @param square
	 * @param yoff
	 */
	public IsoImage(BufferedImage image, IsoSquare square, int yoff) {
		this.square = square;
		this.image = image;
		this.yoffset = yoff;
	}

	/**
	 * Get the image that this IsoImage wraps
	 * @return
	 */
	public BufferedImage image() {
		return image;
	}
	
	/**
	 * Get the width that this image should be displayed at
	 * @return
	 */
	public int width() {
		return image.getWidth();
	}
	
	/**
	 * Get the height this image should be displayed at
	 * @return
	 */
	public int height() {
		return image.getHeight();
	}

	/**
	 * Get the GameThing that this IsoImage represents
	 * @return
	 */
	public GameThing gameThing() {
		return gameThing;
	}
	
	/**
	 * Set the GameThing that this IsoImage represents
	 * @param thing
	 */
	public void setGameThing(GameThing thing) {
		gameThing = thing;
	}

	/**
	 * Get the IsoSquare that this image is on
	 * @return
	 */
	public IsoSquare square() {
		return square;
	}
	
	/**
	 * Get ther y-offset to renderer this image at
	 * @return
	 */
	public int yoffset() {
		return yoffset;
	}
}
