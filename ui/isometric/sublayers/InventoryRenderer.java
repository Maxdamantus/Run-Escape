package ui.isometric.sublayers;

import java.awt.Graphics;

import ui.isometric.abstractions.IsoPlayer;
import ui.isometric.libraries.IsoCharacterImageLibrary;

/**
 * Renderer the players inventory
 * 
 * @author melby
 *
 */
public class InventoryRenderer extends LargePanel {
	private IsoPlayer player;
	
	/**
	 * Create an InventoryRenderer with a given player and percentage x/y coord
	 * @param player
	 * @param x
	 * @param y
	 */
	public InventoryRenderer(IsoPlayer player, double x, double y) {
		super(x, y);
		
		this.player = player;
	}

	@Override
	protected void drawContents(Graphics g) {
		g.drawImage(IsoCharacterImageLibrary.imageForCharacterName(player.characterName()), 0, 0, null);
	}
}
