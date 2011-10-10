package ui.isometric.client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.List;

import game.GameThing;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import ui.isometric.IsoCanvas;
import ui.isometric.IsoInterface;
import ui.isometric.abstractions.IsoObject;
import ui.isometric.abstractions.IsoPlayer;
import ui.isometric.datasource.IsoDataSource;
import ui.isometric.datasource.IsoPlayerDataSource;
import ui.isometric.sublayers.ChatRenderer;
import ui.isometric.sublayers.QuickBarRenderer;
import util.Direction;

import game.*;

/**
 * 
 * The overall class that manages the entire user
 * interface for a player/client
 * 
 * @author melby
 *
 */
public class ClientInterface implements IsoInterface {
	private JFrame frame;
	private IsoCanvas canvas;
	private ClientInterface isoInterface = this;
	
	private IsoPlayer player;
	
	private GameWorld world;
	private ClientMessageHandler logic;
	
	private ChatRenderer chatRenderer;
	private QuickBarRenderer quickBarRenderer;
		
	/**
	 * Create a interface with a given frame name, GameWorld, ClientMessageHandler and player gid
	 * @param name
	 * @param world
	 * @param logic
	 * @param playerGid
	 */
	public ClientInterface(String name, final GameWorld world, final ClientMessageHandler logic, final long playerGid) {
		this.world = world;
		this.logic = logic;
		
		long end = System.currentTimeMillis() + 5000; // 5 sec
		GameThing me = null;
		while((me = world.thingWithGID(playerGid)) == null && end > System.currentTimeMillis()) { // Find player
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		this.player = new IsoPlayer(world, me, this, logic.getCharacterName());
		
		frame = new JFrame(name);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		IsoDataSource dataSource = new IsoPlayerDataSource(this.world, player);
		
		canvas = new IsoCanvas(dataSource);
		canvas.addSelectionCallback(new IsoCanvas.SelectionCallback() {
			@Override
			public void selected(final IsoObject i, final Location l, MouseEvent event) {
				if(i != null) {			
					if(event.getButton() == MouseEvent.BUTTON3 || event.isControlDown()) { // Right click
						JPopupMenu popup = new JPopupMenu();
						for(GameThing t : i.gameThing().location().contents()) {
							if(t.gid() != playerGid) {
								JMenuItem n = new JMenuItem(t.name());
								n.setEnabled(false);
								popup.add(n);
								
								List<String> interactions = t.interactions();
								
								for(String intr : interactions) {
									JMenuItem item = new JMenuItem("   "+intr);
									item.setName(t.gid()+"");
									item.addActionListener(new ActionListener() {								
										@Override
										public void actionPerformed(ActionEvent e) {
											Object s = e.getSource();
											
											if(s instanceof JMenuItem) {
												JMenuItem m = (JMenuItem)s;
												isoInterface.performActionOn(m.getText().substring(3), world.thingWithGID(Long.parseLong /* BLARGH */ (m.getName())));
											}
										}
									});
									popup.add(item);
								}
							}
						}
						popup.show(canvas, event.getPoint().x, event.getPoint().y);
					}
					else {
						if(i.gameThing() != null && i.gameThing().defaultInteraction() != null) {
							isoInterface.performActionOn(i.gameThing().defaultInteraction(), i.gameThing());
						}
					}
				}
			}
		});
		canvas.addKeyListener(new KeyListener() { // TODO: key listeners in renderer layers?
			private boolean chat = false;
			private String message = "";
			
			@Override
			public void keyPressed(KeyEvent arg0) {}

			@Override
			public void keyReleased(KeyEvent arg0) {}

			@Override
			public void keyTyped(KeyEvent arg0) {
				if(arg0.getKeyChar() == '\n') {
					if(chat) {
						if(message.length() > 0) {
							sendChatMessage(message);
						}
						chat = false;
						message = "";
					}
					else {
						chat = true;
					}
					chatRenderer.setBoxVisible(chat);
				}
				else if(chat) {
					if(arg0.getKeyChar() == '\b') {
						if(message.length() > 0) {
							message = message.substring(0, message.length()-1);
						}
					}
					else {
						message = message + arg0.getKeyChar();
					}
				}
				else if(arg0.getKeyChar() == 'r') {
					canvas.setViewDirection(canvas.viewDirection().compose(Direction.EAST));
				}
				else if(arg0.getKeyChar() == 'l') {
					canvas.setLightsEnabled(!canvas.lightsEnabled());
				}
				
				chatRenderer.setMessage(message);
			}
		});
		
		chatRenderer = new ChatRenderer();
		quickBarRenderer = new QuickBarRenderer(this);
		
		canvas.addLayerRenderer(chatRenderer);
		canvas.addLayerRenderer(quickBarRenderer);
		
		frame.setSize(300, 300);
		frame.add(canvas);
	}
	
	@Override
	public void performActionOn(String interaction, GameThing thing) {
		logic.sendMessage(new ClientMessage(new ClientMessage.Interaction(thing.gid(), interaction), -1));
	}

	@Override
	public void show() {
		frame.setVisible(true);
	}
	
	@Override
	public void incomingChat(String message, Color color) {
		chatRenderer.logMessage(message, color);
	}
	
	@Override
	public void sendChatMessage(String message) {
		logic.sendChat(message);
	}

	@Override
	public GameWorld world() {
		return world;
	}
	
	@Override
	public IsoPlayer player() {
		return player;
	}
	
	@Override
	public IsoCanvas canvas() {
		return canvas;
	}
}
