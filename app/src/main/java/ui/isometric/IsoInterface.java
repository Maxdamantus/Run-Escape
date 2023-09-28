package ui.isometric;

import java.awt.Color;

import ui.isometric.abstractions.IsoPlayer;
import game.GameThing;
import game.GameWorld;

/**
 * 
 * The interface for overall classes that manage the user interface
 * 
 * @author melby
 *
 */
public interface IsoInterface {
	/**
	 * Send an interaction to the GameWorld
	 * @param interaction
	 * @param thing
	 */
	public void performActionOn(String interaction, GameThing thing);
	
	/**
	 * Get the wrapper around the current player
	 * @return
	 */
	public IsoPlayer player();
	
	/**
	 * Display this interface
	 */
	public void show();
	
	/**
	 * Get the game world
	 */
	public GameWorld world();
	
	/**
	 * Get the rendering canvas
	 * @return
	 */
	public IsoCanvas canvas();
	
	/**
	 * Display an incoming chat message
	 * @param message
	 */
	public void incomingChat(String message, Color color);
	
	/**
	 * Post a chat message
	 * @param message
	 */
	public void sendChatMessage(String message);
}
