package ui.isometric;

import java.awt.image.BufferedImage;

import client.model.GameThing;

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
	
	/**
	 * Create an IsoImage with a given resource path and square that it is on
	 * @param path
	 * @param square
	 * @param name
	 */
	public IsoImage(String path, IsoSquare square) {
		this.square = square;
		this.image = Resources.readImageResourceUnfliped(path);
	}

	/**
	 * Create an IsoImage with an existing image and square it is on
	 * @param image
	 * @param square
	 */
	public IsoImage(BufferedImage image, IsoSquare square) {
		this.square = square;
		this.image = image;
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
}
