package ui.isometric;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import util.Resources;

public class QuickBarRenderer implements IsoCanvas.UILayerRenderer {
	private BufferedImage melee = null;
	private BufferedImage missile = null;
	private BufferedImage spell = null;
	private BufferedImage placeholder = null;
	private int tileSpacing = 2;
	private int bottomPadding = 5;
	
	private int tileWidth = 0;
	
	public QuickBarRenderer() {
		try {
			melee = Resources.readImageResourceUnfliped("/resources/ui/melee.png");
			missile = Resources.readImageResourceUnfliped("/resources/ui/missile.png");
			spell = Resources.readImageResourceUnfliped("/resources/ui/spell.png");
			placeholder = Resources.readImageResourceUnfliped("/resources/ui/placeholder_tile.png");
			
			tileWidth = melee.getWidth();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int level() {
		return 10;
	}

	@Override
	public void render(Graphics g, IsoCanvas into) {
		g.drawImage(melee, into.getWidth()/2-tileWidth*2-tileSpacing*1, into.getHeight()-melee.getHeight()-bottomPadding, null);
		g.drawImage(missile, into.getWidth()/2-tileWidth*1-tileSpacing*0, into.getHeight()-missile.getHeight()-bottomPadding, null);
		g.drawImage(spell, into.getWidth()/2+tileWidth*0+tileSpacing*1, into.getHeight()-spell.getHeight()-bottomPadding, null);
		g.drawImage(placeholder, into.getWidth()/2+tileWidth*1+tileSpacing*2, into.getHeight()-placeholder.getHeight()-bottomPadding, null);
	}

	@Override
	public boolean doSelectionPass(Point selectionPoint, IsoCanvas isoCanvas) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void wasClicked() {
		// TODO Auto-generated method stub
		
	}
}
