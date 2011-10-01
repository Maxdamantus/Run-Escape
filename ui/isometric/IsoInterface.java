package ui.isometric;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

import game.GameThing;
import game.PlayerMessage;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import game.*;

/**
 * 
 * The overall class that manages the entire user interface
 * 
 * @author melby
 *
 */
public class IsoInterface implements PlayerMessage {
	private JFrame frame;
	private IsoCanvas canvas;
	private IsoInterface isoInterface = this;
	
	private GameWorld model;
	private ClientMessageHandler logic;
	
	/**
	 * Create a interface with a given GameModel and ClientMessageHandler
	 * @param name
	 * @param model
	 * @param logic
	 */
	public IsoInterface(String name, GameWorld model, final ClientMessageHandler logic) {
		this.model = model;
		this.logic = logic;
		
		frame = new JFrame(name);
		IsoDataSource d = new IsoGameModelDataSource(this.model);
		canvas = new IsoCanvas(d);
		canvas.addSelectionCallback(new IsoCanvas.SelectionCallback() {
			@Override
			public void selected(final IsoImage i, final Location l, MouseEvent event) {
				if(i != null) {			
					if(event.getButton() == MouseEvent.BUTTON3 || event.isControlDown()) { // Right click
						JPopupMenu popup = new JPopupMenu();
						for(GameThing t : i.gameThing().location().contents()) {
							JMenuItem n = new JMenuItem(t.name());
							n.setEnabled(false);
							popup.add(n);
							
							List<String> interactions = t.interactions();
							
							for(String intr : interactions) {
								JMenuItem item = new JMenuItem("  "+intr);
								item.addActionListener(new ActionListener() {
									private GameThing thing = i.gameThing();
									
									@Override
									public void actionPerformed(ActionEvent e) {
										Object s = e.getSource();
										
										if(s instanceof JMenuItem) {
											JMenuItem m = (JMenuItem)s;
											isoInterface.performActionOn(m.getText().substring(3), thing);
										}
									}
								});
								popup.add(item);
							}
						}
						popup.show(canvas, event.getPoint().x, event.getPoint().y);
					}
					else {
						isoInterface.performActionOn(i.gameThing().defaultInteraction(), i.gameThing());
					}
				}
			}
		});
		frame.setSize(300, 300);
		frame.add(canvas);
	}
	
	/**
	 * Send an interaction to the clienthandler
	 * @param interaction
	 * @param thing
	 */
	protected void performActionOn(String interaction, GameThing thing) {
		logic.sendMessage(new ClientMessage(new ClientMessage.Interaction(thing.gid(), interaction), -1));
	}

	/**
	 * Display this interface
	 */
	public void show() {
		frame.setVisible(true);
	}

	@Override
	public void logMessage(String message) {
		// TODO Auto-generated method stub
	}

	@Override
	public void showContainer(String name, List<GameThing> contents) {
		// TODO Auto-generated method stub
	}
}
