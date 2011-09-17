package ui.isometric;

import java.util.List;

import game.PlayerMessage;

import javax.swing.JFrame;

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
	
	/**
	 * Create a interface with a given GameModel
	 * @param name
	 * @param model
	 */
	public IsoInterface(String name, GameModel model) {
		frame = new JFrame(name);
		IsoDataSource d = new IsoGameModelDataSource(model);
		IsoCanvas canvas = new IsoCanvas(d);
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
}
