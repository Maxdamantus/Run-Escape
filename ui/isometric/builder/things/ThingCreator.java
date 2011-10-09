package ui.isometric.builder.things;

import game.GameThing;
import game.GameWorld;
import game.Location;

import java.awt.image.BufferedImage;
import java.util.Set;

/**
 * A interface that specifies how to use factory methods for creating GameThings
 * @author melby
 *
 */
public interface ThingCreator {
	/**
	 * Create a new GameThing with a given world and location
	 * @param w
	 * @param l 
	 * @return
	 */
	public GameThing createThing(GameWorld w, Location l);
	
	/**
	 * A preview image of the thing this will create
	 * @return
	 */
	public BufferedImage previewImage();
	
	/**
	 * The names of the renderers that the GameThing this creates uses
	 * @return
	 */
	public Set<String> rendererNames();

	/**
	 * Get the description of this creator and what it creates
	 * @return
	 */
	public String description();
}