package ui.isometric;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import util.Resources;

public class QuickBarRenderer implements IsoCanvas.UILayerRenderer {
	private BufferedImage melee = null;
	private BufferedImage missile = null;
	private BufferedImage spell = null;
	private BufferedImage placeholder_tile = null;
	private BufferedImage disabled_tile = null;
	private BufferedImage default_tile = null;
	
	private int tileWidth = 0;
	private int tileHeight = 0;
	private int tileSpacing = 2;
	private int bottomPadding = 5;
	
	private Button[] buttons = new Button[10];
	
	private static int BUTTON_MELEE = 0;
	private static int BUTTON_MISSILE = 1;
	private static int BUTTON_SPELL = 2;
	
	private Button selectedButton;
	
	private Button.ButtonListener configureButton = new Button.ButtonListener() {
		@Override
		public void rightClick(Button selectedButton) {
			// TODO: configure
		}

		@Override
		public void leftClick(Button selectedButton) { }
	};
	
	private static class Button {
		private static interface ButtonListener {
			void rightClick(Button selectedButton);
			void leftClick(Button selectedButton);
		}
		
		private BufferedImage image;
		private ButtonListener listener;
		
		public Button(BufferedImage image, ButtonListener listener) {
			this.image = image;
			this.listener = listener;
		}
		
		public BufferedImage image() {
			return image;
		}

		public ButtonListener buttonListener() {
			return listener;
		}
	}
	
	public QuickBarRenderer() {
		try {
			melee = Resources.readImageResourceUnfliped("/resources/ui/melee.png");
			missile = Resources.readImageResourceUnfliped("/resources/ui/missile.png");
			spell = Resources.readImageResourceUnfliped("/resources/ui/spell.png");
			placeholder_tile = Resources.readImageResourceUnfliped("/resources/ui/placeholder_tile.png");
			disabled_tile = Resources.readImageResourceUnfliped("/resources/ui/disabled_tile.png");
			default_tile = Resources.readImageResourceUnfliped("/resources/ui/default_tile.png");
			
			tileWidth = melee.getWidth();
			tileHeight = melee.getHeight();
			
			for(int n = 0; n < buttons.length; n++) {
				buttons[n] = new Button(placeholder_tile, configureButton);
			}
			
			buttons[BUTTON_MELEE] = new Button(melee, new Button.ButtonListener() {
				@Override
				public void rightClick(Button selectedButton) {
					// TODO: set player default weapon
				}

				@Override
				public void leftClick(Button selectedButton) {
					// TODO: fight
				}
			});
			buttons[BUTTON_MISSILE] = new Button(missile, new Button.ButtonListener() {
				@Override
				public void rightClick(Button selectedButton) {
					// TODO: set player default weapon
				}

				@Override
				public void leftClick(Button selectedButton) {
					// TODO: fight
				}
			});
			buttons[BUTTON_SPELL] = new Button(spell, new Button.ButtonListener() {
				@Override
				public void rightClick(Button selectedButton) { }

				@Override
				public void leftClick(Button selectedButton) {
					// TODO: show all spells
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
	public void render(Graphics g, IsoCanvas into) {
		int x = into.getWidth()/2-tileWidth*buttons.length/2-tileSpacing*(buttons.length/2-1);
		int y = into.getHeight()-melee.getHeight()-bottomPadding;
		
		for(Button b : buttons) {
			g.drawImage(b.image(), x, y, null);
			x += tileWidth + tileSpacing;
		}
	}

	@Override
	public boolean doSelectionPass(Point selectionPoint, IsoCanvas isoCanvas) {
		selectedButton = null;
		
		int x = isoCanvas.getWidth()/2-tileWidth*buttons.length/2-tileSpacing*(buttons.length/2-1);
		int y = isoCanvas.getHeight()-melee.getHeight()-bottomPadding;
		
		for(Button b : buttons) {
			if(selectionPoint.y > y && selectionPoint.y < y+tileHeight) {
				if(selectionPoint.x > x && selectionPoint.x < x+tileWidth) {
					selectedButton = b;
					return true;
				}
			}
			x += tileWidth + tileSpacing;
		}
		
		return false;
	}

	@Override
	public void wasClicked(MouseEvent event) {
		if(event.getButton() == MouseEvent.BUTTON3) { // Right click
			Button.ButtonListener b = selectedButton.buttonListener();
			if(b != null) {
				b.rightClick(selectedButton);
			}
		}
		else {
			Button.ButtonListener b = selectedButton.buttonListener();
			if(b != null) {
				b.leftClick(selectedButton);
			}
		}
	}
}
