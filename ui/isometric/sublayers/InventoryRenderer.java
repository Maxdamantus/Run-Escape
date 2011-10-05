package ui.isometric.sublayers;

import java.awt.Graphics;

import ui.isometric.IsoCharacterImageLibrary;
import ui.isometric.IsoPlayer;

public class InventoryRenderer extends LargePanel {
	private IsoPlayer player;
	
	public InventoryRenderer(IsoPlayer player, double x, double y) {
		super(x, y);
		
		this.player = player;
	}

	@Override
	protected void drawContents(Graphics g) {
		g.drawImage(IsoCharacterImageLibrary.imageForCharacterName(player.characterName()), 0, 0, null);
	}
}
