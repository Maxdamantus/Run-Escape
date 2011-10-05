package ui.isometric.sublayers;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import ui.isometric.IsoCanvas;

/**
 * A large panel
 * Note: might be removed soon
 * 
 * @author melby
 *
 */
public abstract class LargePanel extends Panel {
	
	/**
	 * Create a LargePanel at a given fraction of the window width height
	 * @param x
	 * @param y
	 */
	public LargePanel(double x, double y) {
		super(x, y);
	}

	@Override
	public int level() {
		return 100;
	}

	@Override
	public void wasClicked(MouseEvent event, IsoCanvas canvas) {
		// TODO Auto-generated method stub
	}

	@Override
	protected String imageName() {
		return "/resources/ui/panel_large.jpg";
	}

	@Override
	abstract protected void drawContents(Graphics g);
}
