package ui.isometric.sublayers;

import game.Container;
import game.GameThing;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import ui.isometric.IsoCanvas;
import ui.isometric.IsoInterface;
import ui.isometric.abstractions.IsoPlayer;
import ui.isometric.libraries.IsoCharacterImageLibrary;
import ui.isometric.libraries.IsoEquipImageLibrary;
import util.Resources;

/**
 * Renderer the players inventory
 * 
 * @author melby
 *
 */
public class InventoryRenderer extends LargePanel {
	private IsoPlayer player;
	private IsoInterface inter;
	
	private static BufferedImage weapon_slot = null;
	private static BufferedImage armour_slot = null;
	private static BufferedImage helmet_slot = null;
	private static BufferedImage gauntlets_slot = null;
	private static BufferedImage boots_slot = null;
	private static BufferedImage cloak_slot = null;
	private static BufferedImage shield_slot = null;
	
	private static int imageSize = 44;
	
	private Set<ClickArea> permAreas = new HashSet<ClickArea>();
	private Set<ClickArea> dynmAreas = new HashSet<ClickArea>();
	
	private static class ClickArea {
		private static interface ClickAction {
			static class GameThingAction implements ClickAction {
				private GameThing thing;
				
				public GameThingAction(GameThing thing) {
					this.thing = thing;
				}
				
				@Override
				public void perform(MouseEvent e, Point p, final InventoryRenderer inven) {
					if(e.getButton() == MouseEvent.BUTTON3) { // Right click
						JPopupMenu popup = new JPopupMenu();
						for(String s : thing.interactions()) {
							JMenuItem item = new JMenuItem(s);
							item.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									Object s = e.getSource();
									
									if(s instanceof JMenuItem) {
										JMenuItem m = (JMenuItem)s;
										inven.ui().performActionOn(m.getText(), thing);
									}
								}
							});
							popup.add(item);
						}
						popup.show(inven.superview(), e.getPoint().x, e.getPoint().y);
					} // TODO: left click
				}
			}
			
			void perform(MouseEvent e, Point p, InventoryRenderer inven);
		}
		
		private int x;
		private int y;
		private int w;
		private int h;
		private ClickAction action;
		
		public ClickArea(int x, int y, int w, int h, ClickAction action) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			this.action = action;
		}
		
		public int x() { return x; }
		
		public int y() { return y; };
		
		public int width() { return w; }
		
		public int height() { return h; }
		
		public void perform(MouseEvent e, Point p, InventoryRenderer inven) {
			action.perform(e, p, inven);
		}
	}
	
	/**
	 * Create an InventoryRenderer with a given ui and percentage x/y coord
	 * @param inter
	 * @param x
	 * @param y
	 */
	public InventoryRenderer(IsoInterface inter, double x, double y) {
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
					shield_slot = Resources.readImageResourceUnfliped("/resources/ui/slot_shield.png");
					
					permAreas.add(new ClickArea(230, 30, imageSize+2, imageSize+2, new ClickArea.ClickAction() {
						@Override
						public void perform(MouseEvent e, Point p,
								InventoryRenderer inven) {
							System.out.println("Helmet");
						}
						
					})); // Helmet
					permAreas.add(new ClickArea(120, 50, imageSize+2, imageSize+2, new ClickArea.ClickAction() {
						@Override
						public void perform(MouseEvent e, Point p,
								InventoryRenderer inven) {
							System.out.println("Cloak");
						}
						
					})); // Cloak
					permAreas.add(new ClickArea(100, 100, imageSize+2, imageSize+2, new ClickArea.ClickAction() {
						@Override
						public void perform(MouseEvent e, Point p,
								InventoryRenderer inven) {
							System.out.println("Armour");
						}
						
					})); // Armour
					permAreas.add(new ClickArea(110, 150, imageSize+2, imageSize+2, new ClickArea.ClickAction() {
						@Override
						public void perform(MouseEvent e, Point p,
								InventoryRenderer inven) {
							System.out.println("Weapon");
						}
						
					})); // Weapon
					permAreas.add(new ClickArea(270, 120, imageSize+2, imageSize+2, new ClickArea.ClickAction() {
						@Override
						public void perform(MouseEvent e, Point p,
								InventoryRenderer inven) {
							System.out.println("Shield");
						}
						
					})); // Shield
					permAreas.add(new ClickArea(260, 200, imageSize+2, imageSize+2, new ClickArea.ClickAction() {
						@Override
						public void perform(MouseEvent e, Point p,
								InventoryRenderer inven) {
							System.out.println("Boots");
						}
						
					})); // Boots
					permAreas.add(new ClickArea(50, 150, imageSize+2, imageSize+2, new ClickArea.ClickAction() {
						@Override
						public void perform(MouseEvent e, Point p,
								InventoryRenderer inven) {
							System.out.println("Gauntlets");
						}
						
					})); // Gauntlets
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		this.inter = inter;
		this.player = inter.player();
	}

	@Override
	protected void drawContents(Graphics g) {
		dynmAreas.clear();
		
		drawEquipment(g);
		drawInventory(g);
		drawContainer(g);
	}

	/**
	 * Draw the container view
	 * @param g
	 */
	private void drawContainer(Graphics g) {
		g.drawRect(425, 10, 150, 590);
	}

	/**
	 * Draw the equipment
	 * @param g
	 */
	private void drawEquipment(Graphics g) {
		g.drawRect(10, 10, 400, 280);
		g.drawImage(IsoCharacterImageLibrary.imageForCharacterName(player.characterName()), 110, 20, null);
		g.drawImage(helmet_slot, 230, 30, null);
		g.drawImage(cloak_slot, 120, 50, null);
		g.drawImage(armour_slot, 100, 100, null);
		g.drawImage(weapon_slot, 110, 150, null);
		g.drawImage(shield_slot, 270, 120, null);
		g.drawImage(boots_slot, 260, 200, null);
		g.drawImage(gauntlets_slot, 50, 150, null);
	}

	/**
	 * Draw the inventory
	 * @param g
	 */
	private void drawInventory(Graphics g) {
		int x = 10;
		int y = 305;
		int width = 400;
		int height = 295;
		int spacing = 46;
		
		g.drawRect(x, y, width, height);
		
		x++;
		y++;
		
		Container inventory = player.inventory();
		if(inventory != null) {
			for(GameThing thing : inventory) {
				BufferedImage i = IsoEquipImageLibrary.imageForName(thing.renderer());
				
				if(i != null) { // TODO: placeholder image '?'?
					g.drawImage(i, x, y, null);
					dynmAreas.add(new ClickArea(x, y, imageSize, imageSize, new ClickArea.ClickAction.GameThingAction(thing)));
				}
				else {
					System.out.println("null image for " + thing.renderer());
				}
				
				x += spacing;
				if(x + spacing >= width) {
					y += spacing;
					x = 0;
				}
			}
		}
	}

	@Override
	protected void mouseDown(MouseEvent e, Point p, IsoCanvas canvas) {
		for(ClickArea a : permAreas) {
			if(this.pointInRect(p, a.x(), a.y(), a.width(), a.height())) {
				a.perform(e, p, this);
				return;
			}
		}
		for(ClickArea a : dynmAreas) {
			if(this.pointInRect(p, a.x(), a.y(), a.width(), a.height())) {
				a.perform(e, p, this);
				return;
			}
		}
	}
	
	private IsoInterface ui() {
		return inter;
	}
}
