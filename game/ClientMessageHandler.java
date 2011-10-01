package game;

/**
 * Enables the sending of ClientMessages
 * @author greenwthom
 *
 */
public interface ClientMessageHandler {
	/**
	 * Sends the message
	 * @param message message to be sent
	 */
	public void sendMessage(ClientMessage message);
	
	public void sendChat(String chatText);
}
