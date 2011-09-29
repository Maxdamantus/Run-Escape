package ui.isometric.builder;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;
	private Object dragObject;
	
	public ImagePanel(BufferedImage bufferedImage) {
		this.image = bufferedImage;
		this.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
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
	
	public Object dragObject() {
		return dragObject;
	}
	
	public void setDragObject(Object o) {
		dragObject = o;
	}
}
