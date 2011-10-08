package ui.isometric.sublayers;

import java.awt.Graphics2D;

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
	protected String imageName() {
		return "/resources/ui/panel_large.jpg";
	}

	@Override
	abstract protected void drawContents(Graphics2D g);
}
