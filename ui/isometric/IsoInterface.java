package ui.isometric;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

import game.PlayerMessage;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import client.model.GameLogic;
import client.model.GameModel;
import client.model.GameThing;

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
	
	private GameModel model;
	private GameLogic logic;
	
	/**
	 * Create a interface with a given GameModel and GameLogic
	 * @param name
	 * @param model
	 * @param logic
	 */
	public IsoInterface(String name, GameModel model, GameLogic logic) {
		this.model = model;
		this.logic = logic;
		
		frame = new JFrame(name);
		IsoDataSource d = new IsoGameModelDataSource(this.model);
		canvas = new IsoCanvas(d);
		canvas.addSelectionCallback(new IsoCanvas.SelectionCallback() {
			@Override
			public void imageSelected(final IsoImage i, MouseEvent event) {
				if(i != null) {			
					if(event.getButton() == MouseEvent.BUTTON3 || event.isControlDown()) { // Right click
						List<String> interactions = i.gameThing().interactions();
						
						JPopupMenu popup = new JPopupMenu();
						for(String intr : interactions) {
							JMenuItem item = new JMenuItem(intr);
							item.addActionListener(new ActionListener() {
								private GameThing thing = i.gameThing();
								
								@Override
								public void actionPerformed(ActionEvent e) {
									Object s = e.getSource();
									
									if(s instanceof JMenuItem) {
										JMenuItem m = (JMenuItem)s;
										isoInterface.gameLogic().performActionOn(m.getText(), thing);
									}
								}
							});
							popup.add(item);
						}
						popup.show(canvas, event.getPoint().x, event.getPoint().y);
					}
					else {
						isoInterface.gameLogic().performActionOn(i.gameThing().defaultInteraction(), i.gameThing());
					}
				}
			}
		});
		canvas.setSize(300, 300);
		canvas.repaint();
		frame.add(canvas);
		frame.pack();
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
	
	/**
	 * Get the GameLogic that this interface is connected to
	 * @return
	 */
	public GameLogic gameLogic() {
		return logic;
	}
}
