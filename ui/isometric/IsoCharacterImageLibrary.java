package ui.isometric;

import java.awt.image.BufferedImage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import data.Database;

import serialization.Serializer;
import serialization.Serializers;
import serialization.Tree;
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
	 * Internal data structure used to storing and loading info from disk
	 * 
	 * @author melby
	 *
	 */
	private static class ImageType {
		private String name;
		
		/**
		 * The Serializer for a ImageType
		 * @author melby
		 *
		 */
		private static class Serializer implements serialization.Serializer<ImageType> {
			private static final String NAME = "name";
			
			@Override
			public ImageType read(Tree in) {
				serialization.Serializer<Map<String, String>> deserializer = new Serializers.Map<String, String>(Serializers.Serializer_String, Serializers.Serializer_String);
				Map<String, String> store = deserializer.read(in);
				
				return new ImageType(store.get(NAME));
			}

			@Override
			public Tree write(ImageType in) {
				serialization.Serializer<Map<String, String>> serializer = new Serializers.Map<String, String>(Serializers.Serializer_String, Serializers.Serializer_String);
				Map<String, String> store = new HashMap<String, String>();
				store.put(NAME, in.name);
				
				return serializer.write(store);
			}
		}
		
		/**
		 * Create an ImageType with a given image name
		 * @param name
		 */
		private ImageType(String name) {
			this.name = name;
		}
		
		/**
		 * Load the bufferedImage that this ImageType represents
		 * @return
		 */
		private BufferedImage load() {
			try {
				return Resources.readImageResourceUnfliped("/resources/characters/"+name+".png");
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * Load all the images from disk into our internal data structures
	 */
	private static void loadImages() {
		synchronized(IsoCharacterImageLibrary.class) {
			if(images == null) {
				images = new HashMap<String, BufferedImage>();
				
				Serializer<Map<String, ImageType>> deserializer = new Serializers.Map<String, ImageType>(Serializers.Serializer_String, new ImageType.Serializer());
				Map<String, ImageType> types = null;
				try {
					types = deserializer.read(Database.xmlToTree(Resources.loadTextResource("/resources/characters/resources.xml")));
				} catch (IOException e) {
					System.err.println("Unable to load resource declerations");
					e.printStackTrace();
				}
				
				for(String key : types.keySet()) {
					images.put(key, types.get(key).load());
				}
			}
		}
	}
	
	/**
	 * Get the image for a given character name
	 * @param name
	 * @return
	 */
	public BufferedImage imageForCharacterName(String name) {
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
	public Set<String> getAllCharacters() { // TODO: more consistancy checks
		synchronized(IsoCharacterImageLibrary.class) {
			if(images == null) {
				loadImages();
			}
		}
		
		return images.keySet();
	}
}
