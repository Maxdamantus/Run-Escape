package util;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
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
	 * @throws IOException 
	 */
	public static String loadTextResource(String resourceName) throws IOException {
		InputStream inputStream = Resources.class.getResourceAsStream(resourceName);
		
		if(inputStream != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			
			if(reader != null) {
				StringWriter sw = new StringWriter();
			    String line = null;
			    
				while((line = reader.readLine()) != null) {
					sw.append(line);
					sw.append('\n');
				}
				
				return sw.toString();
			}
		}
		
		return null; // Getting here is bad
	}
	
	/**
	 * Read the contents of a file into a string
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static String loadTextFile(String path) throws IOException {
		StringWriter sw = new StringWriter();
		
		BufferedReader reader = new BufferedReader(new FileReader(path));
		
		String line = null;
		while((line = reader.readLine()) != null) {
			sw.append(line);
			sw.append('\n');
		}
		
		return sw.toString();
	}
	
	/**
	 * Load an image unfliped
	 * @param resourceName
	 * @return
	 * @throws IOException 
	 */
	public static BufferedImage readImageResourceUnfliped(String resourceName) throws IOException {
		URL url = getResourceURL(resourceName);
		if(url == null) {
			throw new RuntimeException("Error reading resource " + resourceName);
		}
		
		BufferedImage img = ImageIO.read(url);
		return img;
	}
	
	/**
	 * Load an image fliped
	 * @param resourceName
	 * @return
	 * @throws IOException 
	 */
	public static BufferedImage readImageResourceFliped(String resourceName) throws IOException {
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
	}
	
	/**
	 * Get the URL to a given resource
	 * @param resourceName
	 * @return
	 */
	public static URL getResourceURL(String resourceName) { // TODO: print out if file doesn't exist
		return Resources.class.getResource(resourceName);
	}
}
