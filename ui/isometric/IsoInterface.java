package ui.isometric;

import java.util.List;

import game.PlayerMessage;

import javax.swing.JFrame;

import clientinterface.GameLogic;
import clientinterface.GameModel;
import clientinterface.GameThing;

/**
 * 
 * The overall class that manages the entire user interface
 * 
 * @author melby
 *
 */
public class IsoInterface implements PlayerMessage {
	private JFrame frame;
	
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
		IsoCanvas canvas = new IsoCanvas(this, d);
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
