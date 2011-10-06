package ui.isometric.sublayers;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import ui.isometric.abstractions.IsoPlayer;
import ui.isometric.libraries.IsoCharacterImageLibrary;
import util.Resources;

/**
 * Renderer the players inventory
 * 
 * @author melby
 *
 */
public class InventoryRenderer extends LargePanel {
	private IsoPlayer player;
	
	private static BufferedImage weapon_slot = null;
	private static BufferedImage armour_slot = null;
	private static BufferedImage helmet_slot = null;
	private static BufferedImage gauntlets_slot = null;
	private static BufferedImage boots_slot = null;
	private static BufferedImage cloak_slot = null;
	private static BufferedImage sheild_slot = null;
	
	/**
	 * Create an InventoryRenderer with a given player and percentage x/y coord
	 * @param player
	 * @param x
	 * @param y
	 */
	public InventoryRenderer(IsoPlayer player, double x, double y) {
		super(x, y);
		
		synchronized(InventoryRenderer.class) {
			if(weapon_slot == null) {
				try {
					weapon_slot = Resources.readImageResourceUnfliped("/resources/ui/slot_weapon.png");
					armour_slot = Resources.readImageResourceUnfliped("/resources/ui/slot_armour.png");
					helmet_slot = Resources.readImageResourceUnfliped("/resources/ui/slot_helmet.png");
					gauntlets_slot = Resources.readImageResourceUnfliped("/resources/ui/slot_gauntlets.png");
					boots_slot = Resources.readImageResourceUnfliped("/resources/ui/slot_boots.png");
					cloak_slot = Resources.readImageResourceUnfliped("/resources/ui/slot_cloak.png");
					sheild_slot = Resources.readImageResourceUnfliped("/resources/ui/slot_sheild.png");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		this.player = player;
	}

	@Override
	protected void drawContents(Graphics g) {
		g.drawRect(10, 10, 400, 280);
		g.drawRect(425, 10, 150, 590);
		g.drawRect(10, 305, 400, 295);
		g.drawImage(IsoCharacterImageLibrary.imageForCharacterName(player.characterName()), 110, 20, null);
		g.drawImage(helmet_slot, 230, 30, null);
		g.drawImage(cloak_slot, 120, 50, null);
		g.drawImage(armour_slot, 100, 100, null);
		g.drawImage(weapon_slot, 110, 150, null);
		g.drawImage(sheild_slot, 270, 120, null);
		g.drawImage(boots_slot, 260, 200, null);
		g.drawImage(gauntlets_slot, 50, 150, null);
	}
}
