package ui.isometric.sublayers;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import ui.isometric.IsoCanvas;
import util.Resources;

abstract public class Panel implements IsoCanvas.UILayerRenderer {
	private BufferedImage image = null;
	private double x;
	private double y;
	private int width;
	private int height;
	private IsoCanvas superview;
	
	public Panel(double x, double y) {
		try {
			image = Resources.readImageResourceUnfliped(this.imageName());
			this.width = image.getWidth();
			this.height = image.getHeight();
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
		g.drawImage(image, (int)(into.getWidth()*x-width/2), (int)(into.getHeight()*y-height/2), null);
	}

	@Override
	public boolean doSelectionPass(Point selectionPoint, IsoCanvas isoCanvas) {
		return false; // TODO:
	}

	@Override
	abstract public void wasClicked(MouseEvent event, IsoCanvas canvas);
	
	@Override
	final public void setSuperview(IsoCanvas canvas) {
		superview = canvas;
	}
	
	public IsoCanvas superview() {
		return superview;
	}
	
	public void removeFromSuperview() {
		superview.removeLayerRenderer(this);
	}
	
	abstract protected String imageName();
}
