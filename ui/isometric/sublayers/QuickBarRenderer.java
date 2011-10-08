package ui.isometric.sublayers;

import game.*;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import ui.isometric.IsoCanvas;
import ui.isometric.IsoInterface;
import ui.isometric.abstractions.IsoPlayer;
import util.Resources;

/**
 * 
 * A class the renders the buttons at the bottom of the screen allowing
 * interaction with the current player
 * 
 * @author melby
 *
 */
public class QuickBarRenderer implements IsoCanvas.UILayerRenderer {
	private BufferedImage spell = null;
	private BufferedImage open_inventory = null;
	
	private int tileWidth = 0;
	private int tileHeight = 0;
	private int tileSpacing = 2;
	private int bottomPadding = 5;
	
	private Button[] buttons = new Button[2];
	
	private static int BUTTON_SPELL = 0;
	private static int BUTTON_INVENTORY = 1;
	
	private IsoInterface inter;
	
	private Button selectedButton;
	
	private InventoryRenderer inventoryRenderer;
	
	/**
	 * A private button class, used for storing state of each button
	 * @author melby
	 *
	 */
	private class Button {
		private BufferedImage image;
		private GameThing thing;
		private String interaction;
		private Runnable run;
		
		/**
		 * Create a Button
		 * @param image
		 * @param thing
		 * @param interaction
		 * @param run
		 */
		public Button(BufferedImage image, GameThing thing, String interaction, Runnable run) {
			this.image = image;
			this.thing = thing;
			this.interaction = interaction;
			this.run = run;
		}
		
		/**
		 * The image of this button
		 * @return
		 */
		public BufferedImage image() {
			return image;
		}
		
		/**
		 * Do the interaction and action
		 */
		public void doInteraction() {
			if(thing != null && interaction != null) {
				inter.performActionOn(interaction, thing);
			}
			if(run != null) {
				run.run();
			}
		}
	}
	
	/**
	 * Create a QuickBarRenderer with a given interface
	 * @param inter
	 */
	public QuickBarRenderer(final IsoInterface inter) {
		try {			
			this.inter = inter;
			this.inventoryRenderer = new InventoryRenderer(inter, 0.5, 0.45);
			
			spell = Resources.readImageResourceUnfliped("/resources/ui/spell.png");
			open_inventory = Resources.readImageResourceUnfliped("/resources/ui/open_inventory.png");
			
			tileWidth = spell.getWidth();
			tileHeight = spell.getHeight();
			
			buttons[BUTTON_SPELL] = new Button(spell, null, null, null);
			buttons[BUTTON_INVENTORY] = new Button(open_inventory, null, null, new Runnable() {
				@Override
				public void run() {
					showHideInventory();
				}
			});
			
			inter.player().addShowContainerListener(new IsoPlayer.ShowContainerListener() {
				@Override
				public void showContainer(Container show) {
					showInventory();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int level() {
		return 10;
	}

	@Override
	public void render(Graphics2D g, IsoCanvas into) {
		int x = into.getWidth()/2-tileWidth*buttons.length/2-tileSpacing*(buttons.length/2-1);
		int y = into.getHeight()-tileHeight-bottomPadding;
		
		for(Button b : buttons) {
			g.drawImage(b.image(), x, y, null);
			x += tileWidth + tileSpacing;
		}
	}

	@Override
	public boolean doSelectionPass(Point selectionPoint, IsoCanvas isoCanvas) {
		selectedButton = null;
		boolean clicked = false;
		
		int x = isoCanvas.getWidth()/2-tileWidth*buttons.length/2-tileSpacing*(buttons.length/2-1);
		int y = isoCanvas.getHeight()-tileWidth-bottomPadding;
		
		for(Button b : buttons) {
			if(selectionPoint.y > y && selectionPoint.y < y+tileHeight) {
				if(selectionPoint.x > x && selectionPoint.x < x+tileWidth) {
					selectedButton = b;
					clicked = true;
					break;
				}
			}
			x += tileWidth + tileSpacing;
		}
		
		return clicked;
	}

	@Override
	public void wasClicked(MouseEvent event, IsoCanvas canvas) {
		selectedButton.doInteraction();
	}

	@Override
	public void setSuperview(IsoCanvas canvas) {
		// TODO: need later?
	}
	
	/**
	 * Show the inventory
	 */
	private void showInventory() {
		if(inventoryRenderer.superview() == null) {
			inter.canvas().addLayerRenderer(inventoryRenderer);
		}
	}
	
	/**
	 * Hide the inventory
	 */
	private void hideInventory() {
		if(inventoryRenderer.superview() != null) {
			inventoryRenderer.removeFromSuperview();
		}
	}
	
	/**
	 * Show/hide the inventory panel
	 */
	private void showHideInventory() {
		if(inventoryRenderer.superview() == null) {
			showInventory();
		}
		else {
			hideInventory();
		}
	}
}
