package util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/**
 * 
 * Basic image editing
 * 
 * @author melby
 *
 */
public class ImageEdit {
	
	/**
	 * Convert a specific color in the input image into transparency
	 * @param image - the input image
	 * @param color - the color to replace with alpha
	 * @return - a new image with color replaced with alpha
	 */
	public static BufferedImage colorToAlpha(BufferedImage image, Color color) { // TODO: deal with different types of input images
		Raster inColorRaster = image.getData();
		int[] colorComponents = new int[3];
		colorComponents[0] = color.getRed();
		colorComponents[1] = color.getGreen();
		colorComponents[2] = color.getBlue();
		
		BufferedImage out = new BufferedImage(inColorRaster.getWidth(), inColorRaster.getHeight(), BufferedImage.TYPE_INT_ARGB);
		WritableRaster outAlphaRaster = out.getAlphaRaster();
		WritableRaster outColorRaster = out.getRaster();
		
		int[] pixel = new int[4];
		int[] alpha = new int[1];
		for(int x = 0; x < inColorRaster.getWidth(); x++) {
			for(int y = 0; y < inColorRaster.getHeight(); y++) {
				pixel = inColorRaster.getPixel(x, y, pixel);
				outColorRaster.setPixel(x, y, pixel);
				alpha[0] = (pixel[0] == colorComponents[0] && pixel[1] == colorComponents[1] && pixel[2] == colorComponents[2])?0:255;
				outAlphaRaster.setPixel(x, y, alpha);
			}
		}
		return out;
	}

	/**
	 * Split an image into an array of images, based on the number of
	 * frames and spacing between images
	 * @param image - the image to split
	 * @param frames - the number of frames in the animation
	 * @param spacing - the number of pixels between frames of the animations in image
	 * @return
	 */
	public static BufferedImage[] splitImage(BufferedImage image, int frames, int spacing) {
		BufferedImage[] out = new BufferedImage[frames];
				
		int width = (image.getWidth() - spacing*(frames-1))/frames;
		for(int n = 0; n < frames; n++) {
			out[n] = image.getSubimage((width+spacing)*n, 0, width, image.getHeight());
		}
		
		return out;
	}
}
