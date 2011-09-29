package ui.isometric.builder;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import util.DragInfo;

/**
 * A simple JPanel that renders an image proportionally into itself
 * 
 * @author melby
 *
 */
public class ImagePanel extends JPanel implements DragInfo {
	private static final long serialVersionUID = 1L;
	
	private Image image;
	private Object dragObject;
	
	/**
	 * Create an ImagePanel with a given Image,
	 * it's Preferred size will be the size of the image in pixels
	 * @param image
	 */
	public ImagePanel(Image image) {
		this.image = image;
		this.setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));
	}

	@Override
	public void paint(Graphics g) {
		double vwidth = this.getWidth();
		double vheight = this.getHeight();
		double vaspect = vwidth/vheight;
		
		double iwidth = image.getWidth(null);
		double iheight = image.getHeight(null);
		double iaspect = iwidth/iheight;
		
		if(iaspect < vaspect) {
			vwidth = iaspect * vheight;
		}
		else {
			vheight = vwidth / iaspect;
		}
				
		g.drawImage(image, (this.getWidth() - (int)vwidth)/2, (this.getHeight() - (int)vheight)/2, (int)vwidth, (int)vheight, this);
	}
	
	/**
	 * Set/update the image
	 * @param i
	 */
	public void setImage(BufferedImage i) {
		image = i;
	}
	
	@Override
	public Object dragObject() {
		return dragObject;
	}
	
	@Override
	public void setDragObject(Object o) {
		dragObject = o;
	}
}
