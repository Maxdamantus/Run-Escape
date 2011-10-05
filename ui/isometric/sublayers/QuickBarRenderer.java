package ui.isometric.sublayers;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import ui.isometric.IsoCanvas;
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
	private BufferedImage melee = null;
	private BufferedImage melee_default = null;
	private BufferedImage missile = null;
	private BufferedImage spell = null;
	private BufferedImage background = null;
	private BufferedImage disabled_tile = null;
	private BufferedImage default_tile = null;
	private BufferedImage open_inventory = null;
	
	private int tileWidth = 0;
	private int tileHeight = 0;
	private int tileSpacing = 2;
	private int bottomPadding = 5;
	
	private Button[] buttons = new Button[10];
	
	private static int BUTTON_MELEE = 0;
	private static int BUTTON_MISSILE = 1;
	private static int BUTTON_SPELL = 2;
	private static int BUTTON_INVENTORY = 9;
	
	private Button selectedButton;
	
	private Button.ButtonListener configureButton = new Button.ButtonListener() {
		@Override
		public void rightClick(Button selectedButton, Point point, IsoCanvas canvas) {
			showMenu(canvas, point, new MenuListener() {
				@Override
				public void clicked(String menuName) {
					System.out.println(menuName);
				}
			}, "configure");
		}

		@Override
		public void leftClick(Button selectedButton, Point point, IsoCanvas canvas) { }
	};
	
	private interface MenuListener {
		void clicked(String text);
	}
	
	/**
	 * A private button class, used for storing state of each button
	 * @author melby
	 *
	 */
	private static class Button {
		/**
		 * A button listener to be called when a button is clicked
		 * @author melby
		 *
		 */
		private static interface ButtonListener {
			void rightClick(Button selectedButton, Point point, IsoCanvas canvas);
			void leftClick(Button selectedButton, Point point, IsoCanvas canvas);
		}
		
		private BufferedImage image;
		private ButtonListener listener;
		
		/**
		 * Create a Button with an image and ButtonListener
		 * @param image
		 * @param listener
		 */
		public Button(BufferedImage image, ButtonListener listener) {
			this.image = image;
			this.listener = listener;
		}
		
		/**
		 * The image of this button
		 * @return
		 */
		public BufferedImage image() {
			return image;
		}

		/**
		 * The ButtonListener on this button
		 * Note: may be null
		 * @return
		 */
		public ButtonListener buttonListener() {
			return listener;
		}
	}
	
	/**
	 * Create a QuickBarRenderer
	 */
	public QuickBarRenderer() {
		try {
			melee = Resources.readImageResourceUnfliped("/resources/ui/melee.png");
			melee_default = Resources.readImageResourceUnfliped("/resources/ui/melee_default.png");
			missile = Resources.readImageResourceUnfliped("/resources/ui/missile.png");
			spell = Resources.readImageResourceUnfliped("/resources/ui/spell.png");
			background = Resources.readImageResourceUnfliped("/resources/ui/tile_background.png");
			disabled_tile = Resources.readImageResourceUnfliped("/resources/ui/disabled_tile.png");
			default_tile = Resources.readImageResourceUnfliped("/resources/ui/default_tile.png");
			open_inventory = Resources.readImageResourceUnfliped("/resources/ui/open_inventory.png");
			
			tileWidth = melee.getWidth();
			tileHeight = melee.getHeight();
			
			for(int n = 0; n < buttons.length; n++) {
				buttons[n] = new Button(background, configureButton);
			}
			
			buttons[BUTTON_MELEE] = new Button(melee_default, new Button.ButtonListener() {
				@Override
				public void rightClick(Button selectedButton, Point point, IsoCanvas canvas) {
					showMenu(canvas, point, new MenuListener() {
						@Override
						public void clicked(String menuName) {
							System.out.println(menuName);
						}
					}, "set default weapon");
				}

				@Override
				public void leftClick(Button selectedButton, Point point, IsoCanvas canvas) {
					// TODO: fight
				}
			});
			buttons[BUTTON_MISSILE] = new Button(missile, new Button.ButtonListener() {
				@Override
				public void rightClick(Button selectedButton, Point point, IsoCanvas canvas) {
					showMenu(canvas, point, new MenuListener() {
						@Override
						public void clicked(String menuName) {
							System.out.println(menuName);
						}
					}, "set default weapon");
				}

				@Override
				public void leftClick(Button selectedButton, Point point, IsoCanvas canvas) {
					// TODO: fight
				}
			});
			buttons[BUTTON_SPELL] = new Button(spell, new Button.ButtonListener() {
				@Override
				public void rightClick(Button selectedButton, Point point, IsoCanvas canvas) { }

				@Override
				public void leftClick(Button selectedButton, Point point, IsoCanvas canvas) {
					// TODO: show all spells
				}
			});
			buttons[BUTTON_INVENTORY] = new Button(open_inventory, new Button.ButtonListener() {
				@Override
				public void rightClick(Button selectedButton, Point point, IsoCanvas canvas) { }

				@Override
				public void leftClick(Button selectedButton, Point point, IsoCanvas canvas) {
					// TODO: open inventory
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
		boolean clicked = false;
		
		int x = isoCanvas.getWidth()/2-tileWidth*buttons.length/2-tileSpacing*(buttons.length/2-1);
		int y = isoCanvas.getHeight()-melee.getHeight()-bottomPadding;
		
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
		if(event.getButton() == MouseEvent.BUTTON3) { // Right click
			Button.ButtonListener b = selectedButton.buttonListener();
			if(b != null) {
				b.rightClick(selectedButton, event.getPoint(), canvas);
			}
		}
		else {
			Button.ButtonListener b = selectedButton.buttonListener();
			if(b != null) {
				b.leftClick(selectedButton, event.getPoint(), canvas);
			}
		}
	}
	
	protected void showMenu(IsoCanvas canvas, Point point, final MenuListener menuListener, String ... strings) {
		JPopupMenu popup = new JPopupMenu();
		for(String menuName : strings) {
			JMenuItem item = new JMenuItem(menuName);
			popup.add(item);
			
			item.addActionListener(new ActionListener() {								
				@Override
				public void actionPerformed(ActionEvent e) {
					Object s = e.getSource();
					
					if(s instanceof JMenuItem) {
						JMenuItem m = (JMenuItem)s;
						menuListener.clicked(m.getText());
					}
				}
			});
			popup.add(item);
		}
		
		popup.show(canvas, point.x, point.y);
	}
}
