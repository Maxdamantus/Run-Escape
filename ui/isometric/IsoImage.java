package ui.isometric;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

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
	
	/**
	 * Create an IsoImage with a given resource path/name
	 * Converts the color white into alpha
	 * @param path
	 */
	public IsoImage(String path) {
		image = Resources.readImageResourceUnfliped(path);
		image = ImageEdit.colorToAlpha(image, Color.WHITE);
	}

	/**
	 * Get the image that this IsoImage wraps
	 * @return
	 */
	public Image image() {
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
}
