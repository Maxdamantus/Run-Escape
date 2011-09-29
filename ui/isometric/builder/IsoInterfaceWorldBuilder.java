package ui.isometric.builder;

import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import ui.isometric.IsoCanvas;
import ui.isometric.IsoDataSource;
import ui.isometric.IsoGameModelDataSource;
import ui.isometric.IsoImage;
import util.Position;

import game.*;

/**
 * 
 * The overall class that manages the entire user interface
 * 
 * @author melby
 *
 */
public class IsoInterfaceWorldBuilder {
	private JFrame frame;
	private InspectorPanel inspector;
	private LibraryFrame library;
	
	private IsoCanvas canvas;
	
	private GameWorld model;
	private GameLogic logic;
	
	/**
	 * Create a world builder interface with a given GameModel and GameLogic
	 * @param name
	 * @param model
	 * @param logic
	 */
	public IsoInterfaceWorldBuilder(String name, GameWorld model, GameLogic logic) {
		this.model = model;
		this.logic = logic;
		
		frame = new JFrame(name);
		IsoDataSource d = new IsoGameModelDataSource(this.model);
		canvas = new IsoCanvas(d);
		canvas.addSelectionCallback(new IsoCanvas.SelectionCallback() {
			@Override
			public void imageSelected(final IsoImage i, MouseEvent event) {
				inspect(i);
			}

			@Override
			public void squareAtPointSelected(Position s, MouseEvent arg0) {
				// TODO: implement
			}
		});
		canvas.setSize(300, 300);
		canvas.repaint();
		frame.add(canvas);
		frame.pack();
		
		inspector = new InspectorPanel(this);
		inspector.getContentPane().setLayout(new BoxLayout(inspector.getContentPane(), BoxLayout.Y_AXIS));
		inspector.setSize(200, 400);
		inspector.getContentPane().add(Box.createVerticalGlue());
		inspector.getContentPane().add(new InspectorOptionsPanel(null)); // TODO: Why doesn't this go away after being removed
		inspector.validate();
		
		library = new LibraryFrame();
	}
	
	/**
	 * Display this interface
	 */
	public void show() {
		frame.setVisible(true);
		inspector.setVisible(true);
		library.setVisible(true);
	}
	
	/**
	 * Get the GameLogic that this interface is connected to
	 * @return
	 */
	public GameLogic gameLogic() {
		return logic;
	}
	
	private void inspect(IsoImage i) { // TODO: deal with squares
		if(i == null) {
			inspector.inspect(null);
		}
		else {
			inspector.inspect(i.gameThing());
		}
	}

	public GameWorld gameModel() {
		return model;
	}
}
