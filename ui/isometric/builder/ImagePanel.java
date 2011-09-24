package ui.isometric.builder;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class ImagePanel extends Canvas {
	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;
	
	public ImagePanel(BufferedImage bufferedImage) {
		this.image = bufferedImage;
	}

	@Override
	public void paint(Graphics g) {
		double vwidth = this.getWidth();
		double vheight = this.getHeight();
		double vaspect = vwidth/vheight;
		
		double iwidth = image.getWidth();
		double iheight = image.getHeight();
		double iaspect = iwidth/iheight;
		
		if(iaspect < vaspect) {
			vwidth = iaspect * vheight;
		}
		else {
			vheight = vwidth / iaspect;
		}
				
		g.drawImage(image, (this.getWidth() - (int)vwidth)/2, (this.getHeight() - (int)vheight)/2, (int)vwidth, (int)vheight, this);
	}
	
	public void setImage(BufferedImage i) {
		image = i;
	}
}
