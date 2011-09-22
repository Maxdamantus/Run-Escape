package ui.isometric.builder;

import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import ui.isometric.IsoCanvas;
import ui.isometric.IsoDataSource;
import ui.isometric.IsoGameModelDataSource;
import ui.isometric.IsoImage;
import ui.isometric.IsoSquare;

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
public class IsoInterfaceWorldBuilder {
	private JFrame frame;
	private JFrame inspector;
	private IsoCanvas canvas;
	
	private GameModel model;
	private GameLogic logic;
	
	/**
	 * Create a world builder interface with a given GameModel and GameLogic
	 * @param name
	 * @param model
	 * @param logic
	 */
	public IsoInterfaceWorldBuilder(String name, GameModel model, GameLogic logic) {
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
		});
		canvas.setSize(300, 300);
		canvas.repaint();
		frame.add(canvas);
		frame.pack();
		
		inspector = new JFrame();
		inspector.getContentPane().setLayout(new BoxLayout(inspector.getContentPane(), BoxLayout.Y_AXIS));
		inspector.setSize(200, 400);
		inspector.getContentPane().add(Box.createVerticalGlue());
		inspector.getContentPane().add(new InspectorOptionsPanel(null)); // TODO: Why doesn't this go away after being removed
		inspector.validate();
	}
	
	/**
	 * Display this interface
	 */
	public void show() {
		frame.setVisible(true);
		inspector.setVisible(true);
	}
	
	/**
	 * Get the GameLogic that this interface is connected to
	 * @return
	 */
	public GameLogic gameLogic() {
		return logic;
	}
	
	private void inspect(IsoImage i) {
		inspector.getContentPane().removeAll();
		GameThing g = null;
		
		if(i != null) {
			IsoSquare s = i.square();
			g = i.gameThing();
			
			for(IsoImage im : s) {
				ImageInspector ins = new ImageInspector(im);
				inspector.getContentPane().add(ins);
			}
		}
		
		inspector.getContentPane().add(Box.createVerticalGlue());
		inspector.getContentPane().add(new InspectorOptionsPanel(g));
		inspector.validate();
	}
}
