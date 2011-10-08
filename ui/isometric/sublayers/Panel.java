package ui.isometric.sublayers;

import java.awt.Graphics2D;
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
	
	private static final int xPad = 20;
	private static final int yPad = 20;
	
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
	final public void render(Graphics2D g, IsoCanvas into) {
		g = (Graphics2D) g.create((int)(into.getWidth()*x-width/2), (int)(into.getHeight()*y-height/2), width, height);
		
		g.drawImage(bgimage, 0, 0, null);
		g.drawImage(close, width-20, 0, null);
		this.drawContents((Graphics2D) g.create(xPad, yPad, width-xPad*2, height-yPad*2));
	}

	@Override
	public boolean doSelectionPass(Point selectionPoint, IsoCanvas isoCanvas) { // TODO: close button
		removeFrom = false;
		
		if(this.pointInRect(selectionPoint, isoCanvas.getWidth()*x-width/2, isoCanvas.getHeight()*y-height/2, width, height)) {
			if(this.pointInRect(selectionPoint, isoCanvas.getWidth()*x+width/2-20, isoCanvas.getHeight()*y-height/2, 20, 20)) {
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
		else {
			Point p = new Point();
			p.setLocation(event.getPoint().x - (canvas.getWidth()*x-width/2+xPad), event.getPoint().y - (canvas.getHeight()*y-height/2+yPad));
			this.mouseDown(event, p, canvas);
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
	 * Is the given point in a given rectangle
	 * @param p
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 */
	protected boolean pointInRect(Point p, double x, double y, double w, double h) {
		return p.x > x &&
		p.x < x+w &&
		p.y > y &&
		p.y < y+h;
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
	abstract protected void drawContents(Graphics2D g);
	
	/**
	 * Mouse was clicked somewhere in the view
	 * Note: don't rely on the MouseEvent for location info
	 * @param e
	 * @param p
	 * @param canvas
	 */
	abstract protected void mouseDown(MouseEvent e, Point p, IsoCanvas canvas);
}
