package ui.isometric.builder.things;

import game.GameThing;
import game.GameWorld;

import java.awt.image.BufferedImage;
import java.util.Set;

/**
 * A interface that specifies how to use factory methods for creating GameThings
 * @author melby
 *
 */
public interface ThingCreator {
	/**
	 * Create a new GameThing
	 * @param w
	 * @return
	 */
	public GameThing createThing(GameWorld w);
	
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