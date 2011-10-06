package ui.isometric.abstractions;

import java.awt.image.BufferedImage;

import game.GameThing;

import ui.isometric.libraries.IsoRendererLibrary.RendererImage;

/**
 * 
 * An image abstraction for isometric rendering
 * 
 * @author melby
 *
 */
public class IsoImage {
	private RendererImage image;
	private GameThing gameThing = null;
	private IsoSquare square;
	private int yoffset = 0;

	/**
	 * Create an IsoImage with an existing image, square it is on and y-offset
	 * @param rendererImage
	 * @param square
	 * @param yoff
	 */
	public IsoImage(RendererImage rendererImage, IsoSquare square, int yoff) {
		this.square = square;
		this.image = rendererImage;
		this.yoffset = yoff;
	}

	/**
	 * Get the image that this IsoImage wraps
	 * @return
	 */
	public BufferedImage image() {
		return image.image();
	}
	
	/**
	 * Get the width that this image should be displayed at
	 * @return
	 */
	public int width() {
		return image.width();
	}
	
	/**
	 * Get the height this image should be displayed at
	 * @return
	 */
	public int height() {
		return image.height();
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
	 * Get the y-offset to renderer this image at
	 * @return
	 */
	public int yoffset() {
		return yoffset;
	}
	
	/**
	 * Get the RendererImage that backs this IsoImage
	 * @return
	 */
	protected RendererImage rawImage() {
		return image;
	}
}
