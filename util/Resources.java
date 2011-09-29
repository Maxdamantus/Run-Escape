package util;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * A class to use for loading and finding resources from inside the jar package
 * 
 * @author melby
 *
 */
public class Resources {
	/**
	 * Read a text resource, i.e. the entire file into a string
	 * 
	 * @param resourceName
	 * @return
	 */
	public static String loadTextResource(String resourceName) {
		InputStream inputStream = "".getClass().getResourceAsStream(resourceName);
		
		if(inputStream != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			
			if(reader != null) {
			    String file = "";
			    String line = null;
			    
			    try {
					while((line = reader.readLine()) != null) {
						file += line+"\n";
					}
					return file;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return null; // Getting here is bad
	}
	
	/**
	 * Load an image unfliped
	 * @param resourceName
	 * @return
	 */
	public static BufferedImage readImageResourceUnfliped(String resourceName) {
		try {
			URL url = getResourceURL(resourceName);
			if(url == null) {
				throw new RuntimeException("Error reading resource " + resourceName);
			}
			
			BufferedImage img = ImageIO.read(url);
			return img;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Load an image fliped
	 * @param resourceName
	 * @return
	 */
	public static BufferedImage readImageResourceFliped(String resourceName) {
		try {
			URL url = getResourceURL(resourceName);
			if(url == null) {
				throw new RuntimeException("Error reading resource " + resourceName);
			}
			
			BufferedImage img = ImageIO.read(url);
			
			java.awt.geom.AffineTransform tx = java.awt.geom.AffineTransform.getScaleInstance(1, -1);
			tx.translate(0, -img.getHeight(null));
			
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			
			img = op.filter(img, null); // Flip image into OpenGL coordinates
			return img;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Get the URL to a given resource
	 * @param resourceName
	 * @return
	 */
	public static URL getResourceURL(String resourceName) { // TODO: print out if file doesn't exist
		return "".getClass().getResource(resourceName);
	}
}
