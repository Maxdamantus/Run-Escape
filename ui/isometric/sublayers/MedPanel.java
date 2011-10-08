package ui.isometric.sublayers;

import java.awt.Graphics2D;

/**
 * A medium sized panel
 * Note: might be removed soon
 * 
 * @author melby
 *
 */
abstract public class MedPanel extends Panel {

	/**
	 * Create a MedPanel at a given fraction of the window width height
	 * @param x
	 * @param y
	 */
	public MedPanel(double x, double y) {
		super(x, y);
	}

	@Override
	protected String imageName() {
		return "/resources/ui/panel_med.png";
	}

	@Override
	abstract protected void drawContents(Graphics2D g);
}
