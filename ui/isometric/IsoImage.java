package ui.isometric;

import java.awt.Image;
import java.awt.image.BufferedImage;

import util.Resources;


public class IsoImage {
	private BufferedImage image;
	
	public IsoImage(String path) {
		image = Resources.readImageResourceUnfliped(path);
	}

	public Image image() {
		return image;
	}
	
	public int width() {
		return image.getWidth();
	}
	
	public int height() {
		return image.getHeight();
	}
}
