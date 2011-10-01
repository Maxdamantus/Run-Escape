package ui.isometric;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import ui.isometric.IsoCanvas.UILayerRenderer;
import util.Resources;

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
	
	public ChatRenderer() {
		try {
			chatBoxImage = Resources.readImageResourceUnfliped("/resources/ui/chatbox.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int level() {
		return 10;
	}

	@Override
	public void render(Graphics g, IsoCanvas into) {
		if(visible) {
			g.drawImage(chatBoxImage, originX, into.getHeight()-originY-chatBoxImage.getHeight(null), originX+chatBoxImage.getWidth(null), into.getHeight()-originY, 0, 0, chatBoxImage.getWidth(null), chatBoxImage.getHeight(null), null);
			g.setColor(Color.WHITE);
			g.drawString(message, originX+10, into.getHeight()-originY-9);
		}
	}
	
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
}
