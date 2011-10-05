package ui.isometric.sublayers;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import ui.isometric.IsoCanvas;
import util.Resources;

/**
 * A panel renderer, draws a panel background and close widgets etc.
 * Subclasses override drawContents to draw the contents of the panel
 * 
 * @author melby
 *
 */
abstract public class Panel implements IsoCanvas.UILayerRenderer {
	private BufferedImage bgimage = null;
	private double x;
	private double y;
	private int width;
	private int height;
	private IsoCanvas superview;
	private boolean removeFrom = false;
	
	private static BufferedImage close = null;
	
	/**
	 * Create a panel centered in the view by the given %
	 * @param x - 0-1, distance from right side
	 * @param y - 0-1, distance from top
	 */
	public Panel(double x, double y) {
		try {
			synchronized(Panel.class) {
				if(close == null) {
					close = Resources.readImageResourceUnfliped("/resources/ui/close.png");
				}
			}
			
			bgimage = Resources.readImageResourceUnfliped(this.imageName());
			this.width = bgimage.getWidth();
			this.height = bgimage.getHeight();
			this.x = x;
			this.y = y;
			
		} catch (IOException e) {
			e.printStackTrace();
		};
	}

	@Override
	abstract public int level();

	@Override
	final public void render(Graphics g, IsoCanvas into) {
		g = g.create((int)(into.getWidth()*x-width/2), (int)(into.getHeight()*y-height/2), width, height);
		
		g.drawImage(bgimage, 0, 0, null);
		g.drawImage(close, width-20, 0, null);
		this.drawContents(g.create(20, 20, width-40, height-40));
	}

	@Override
	public boolean doSelectionPass(Point selectionPoint, IsoCanvas isoCanvas) { // TODO: close button
		removeFrom = false;
		
		if(selectionPoint.x > isoCanvas.getWidth()*x-width/2 &&
				selectionPoint.x < isoCanvas.getWidth()*x+width/2 &&
				selectionPoint.y > isoCanvas.getHeight()*y-height/2 &&
				selectionPoint.y < isoCanvas.getHeight()*y+height/2) {
			if(selectionPoint.x > isoCanvas.getWidth()*x+width/2-20 &&
					selectionPoint.y < isoCanvas.getHeight()*y-height/2+20) {
				removeFrom = true;
			}
			
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void wasClicked(MouseEvent event, IsoCanvas canvas) {
		if(removeFrom) {
			this.removeFromSuperview();
		}
	}
	
	@Override
	final public void setSuperview(IsoCanvas canvas) {
		superview = canvas;
	}
	
	/**
	 * Get the panels superview
	 * @return
	 */
	public IsoCanvas superview() {
		return superview;
	}
	
	/**
	 * Remove this panel from it's superview
	 */
	public void removeFromSuperview() {
		superview.removeLayerRenderer(this);
		superview = null;
	}
	
	/**
	 * Get the name of the image to use for the background.
	 * Note, this may be removed soon
	 * @return
	 */
	abstract protected String imageName();
	
	/**
	 * Override to draw the contents of the panel
	 * @param g
	 */
	abstract protected void drawContents(Graphics g);
}
