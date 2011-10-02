package ui.isometric;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import java.util.LinkedList;

import ui.isometric.IsoCanvas.UILayerRenderer;
import util.Resources;
import util.TextHelper;

/**
 * A class that renders the chat window into an IsoCanvas
 * 
 * @author melby
 *
 */
public class ChatRenderer implements UILayerRenderer {
	private Image chatBoxImage = null;
	
	private String message = "";
	private boolean visible = false;
	
	private int originX = 10;
	private int originY = 10;
	
	private static class Message {
		private String message;
		private Color color;
		
		private Message(String mes, Color col) {
			message = mes;
			color = col;
		}
	}
	
	private LinkedList<Message> log = new LinkedList<Message>();
	
	public ChatRenderer() {
		try {
			chatBoxImage = Resources.readImageResourceUnfliped("/resources/ui/chatbox.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int level() {
		return 100;
	}

	@Override
	public void render(Graphics g, IsoCanvas into) {
		if(visible) {
			g.setColor(Color.WHITE);
			g.drawImage(chatBoxImage, originX, into.getHeight()-originY-chatBoxImage.getHeight(null), originX+chatBoxImage.getWidth(null), into.getHeight()-originY, 0, 0, chatBoxImage.getWidth(null), chatBoxImage.getHeight(null), null);
			TextHelper.drawStringScrolling(g, message, originX+10, into.getHeight()-originY-9, chatBoxImage.getWidth(null)-20);
		}
		
		int messagePosition = into.getHeight()-originY-chatBoxImage.getHeight(null)-10;
		int index = 0;
		while(messagePosition > 0 && index < log.size()) {
			Message nextMessage = log.get(index);
			g.setColor(nextMessage.color);
			index++;
			messagePosition -= TextHelper.drawStringMultiLineUp(g, nextMessage.message, originX, messagePosition, 0, chatBoxImage.getWidth(null)) + 5;
		}
	}
	
	@Override
	public boolean doSelectionPass(Point selectionPoint, IsoCanvas isoCanvas) {
		return false;
	}

	@Override
	public void wasClicked() { }
	
	/**
	 * Set the message to display
	 * @param mes
	 */
	public void setMessage(String mes) {
		if(mes == null) {
			mes = "";
		}
		
		message = mes;
	}
	
	/**
	 * Set weather the box is visible
	 * @param vis
	 */
	public void setBoxVisible(boolean vis) {
		visible = vis;
	}
	
	/**
	 * Add a message to the chat log
	 * @param message
	 * @param color
	 */
	public void logMessage(String message, Color color) {
		log.add(0, new Message(message, color));
		while(log.size() > 100) {
			log.removeLast();
		}
	}
}
