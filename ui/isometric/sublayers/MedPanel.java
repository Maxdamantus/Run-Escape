package ui.isometric.sublayers;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import ui.isometric.IsoCanvas;

abstract public class MedPanel extends Panel {

	public MedPanel(double x, double y) {
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
		return "/resources/ui/panel_med.png";
	}

	@Override
	abstract protected void drawContents(Graphics g);
}
