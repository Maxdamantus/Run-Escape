package client;

import game.ClientMessage;

/**
 * Enables the sending of ClientMessages
 * 
 * @author greenwthom
 * 
 */
public interface ClientMessageHandler {
	/**
	 * Sends the ClientMessage to the appropriate service (server or otherwise)
	 * 
	 * @param message
	 *            message to be sent
	 */
	public void sendMessage(ClientMessage message);

	/**
	 * Sends a chat message. Pre-processes with username before transmission
	 * 
	 * @param chatText
	 *            chat text to be sent
	 */
	public void sendChat(String chatText);

	/**
	 * Gets the name of the character object being played by the user
	 * 
	 * @return the name of the character
	 */
	public String getCharacterName();
}
