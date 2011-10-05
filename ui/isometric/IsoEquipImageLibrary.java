package ui.isometric;

import java.awt.image.BufferedImage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import data.Database;

import serialization.Serializer;
import serialization.Serializers;
import serialization.Tree;
import util.Resources;

public class IsoEquipImageLibrary {
	private static Map<String, BufferedImage> images = null;
	
	private static class ImageType {
		private String name;
		
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
		
		private ImageType(String name) {
			this.name = name;
		}
		
		private BufferedImage load() {
			try {
				return Resources.readImageResourceUnfliped("/resources/inventory/"+name+".png");
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
		synchronized(IsoEquipImageLibrary.class) {
			if(images == null) {
				images = new HashMap<String, BufferedImage>();
				
				Serializer<Map<String, ImageType>> deserializer = new Serializers.Map<String, ImageType>(Serializers.Serializer_String, new ImageType.Serializer());
				Map<String, ImageType> types = null;
				try {
					types = deserializer.read(Database.xmlToTree(Resources.loadTextResource("/resources/inventory/resources.xml")));
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
	
	public BufferedImage imageForName(String name) {
		synchronized(IsoEquipImageLibrary.class) {
			if(images == null) {
				loadImages();
			}
		}
		
		return images.get(name);
	}
}
