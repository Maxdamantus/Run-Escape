package ui.isometric.libraries;

import java.awt.image.BufferedImage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import data.Database;

import serialization.Serializer;
import serialization.Serializers;
import util.Resources;

/**
 * A class for accessing inventory images for GameThings
 * 
 * @author melby
 *
 */
public class IsoInventoryImageLibrary {
	private static Map<String, BufferedImage> images = null;
	
	/**
	 * Load all the images from disk into our internal data structures
	 */
	private static void loadImages() {
		synchronized(IsoInventoryImageLibrary.class) {
			if(images == null) {
				images = new HashMap<String, BufferedImage>();
				
				Serializer<Map<String, String>> deserializer = new Serializers.Map<String, String>(Serializers.Serializer_String, Serializers.Serializer_String);
				Map<String, String> names = null;
				try {
					names = deserializer.read(Database.xmlToTree(Resources.loadTextResource("/resources/inventory/resources.xml")));
				} catch (IOException e) {
					System.err.println("Unable to load resource declerations");
					e.printStackTrace();
				}
				
				for(String key : names.keySet()) {
					try {
						images.put(key, Resources.readImageResourceUnfliped("/resources/inventory/"+names.get(key)+".png"));
					} catch (IOException e) {
						System.err.println("Unable to load inventory image named: "+names.get(key));
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * Get the image for a given renderer name
	 * @param name
	 * @return
	 */
	public static BufferedImage imageForName(String name) {
		synchronized(IsoInventoryImageLibrary.class) {
			if(images == null) {
				loadImages();
			}
		}
		
		return images.get(name);
	}
}
