package ui.isometric.sublayers;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import util.Resources;

public class Panel {
	private BufferedImage image = null;
	private int x;
	private int y;
	
	public Panel(int x, int y) {
		try {
			image = Resources.readImageResourceUnfliped("/resources/ui/panel_med.png");
			this.x = x;
			this.y = y;
		} catch (IOException e) {
			e.printStackTrace();
		};
	}
	
	public void render(Graphics g) {
		g.drawImage(image, x, y, null);
	}
}
