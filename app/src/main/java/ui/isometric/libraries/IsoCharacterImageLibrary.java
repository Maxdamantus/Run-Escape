package ui.isometric.libraries;

import java.awt.image.BufferedImage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import data.Database;

import serialization.ParseException;
import serialization.Serializer;
import serialization.Serializers;
import util.Resources;

/**
 * A class for accessing character images
 * 
 * @author melby
 *
 */
public class IsoCharacterImageLibrary {
	private static Map<String, BufferedImage> images = null;
	
	/**
	 * Load all the images from disk into our internal data structures
	 */
	private static void loadImages() {
		synchronized(IsoCharacterImageLibrary.class) {
			if(images == null) {
				images = new HashMap<String, BufferedImage>();
				
				Serializer<Map<String, String>> deserializer = new Serializers.Map<String, String>(Serializers.Serializer_String, Serializers.Serializer_String);
				Map<String, String> names = null;
				try {
					names = deserializer.read(Database.xmlToTree(Resources.loadTextResource("/resources/characters/resources.xml")));
				} catch (IOException e) {
					System.err.println("Unable to load resource declerations");
					e.printStackTrace();
				} catch (ParseException e) {
					System.err.println("Unable to load resource declerations");
					e.printStackTrace();
				}
				
				for(String key : names.keySet()) {
					try {
						images.put(key, Resources.readImageResourceUnfliped("/resources/characters/"+names.get(key)+".png"));
					} catch (IOException e) {
						System.err.println("Unable to load character resource named: "+names.get(key));
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * Get the image for a given character name
	 * @param name
	 * @return
	 */
	public static BufferedImage imageForCharacterName(String name) {
		synchronized(IsoCharacterImageLibrary.class) {
			if(images == null) {
				loadImages();
			}
		}
		
		return images.get(name);
	}
	
	/**
	 * Get a list of all characters
	 * @return
	 */
	public static Set<String> getAllCharacters() { // TODO: more consistancy checks
		synchronized(IsoCharacterImageLibrary.class) {
			if(images == null) {
				loadImages();
			}
		}
		
		return images.keySet();
	}
}
