package ui.isometric.builder;

import java.awt.Component;
import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import ui.isometric.IsoCanvas;
import ui.isometric.IsoDataSource;
import ui.isometric.IsoGameModelDataSource;
import ui.isometric.IsoImage;
import ui.isometric.builder.things.ThingCreator;
import ui.isometric.builder.things.ThingCreatorDnD;
import util.Direction;

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
	
	private GameWorld world;
	private GameLogic logic;
	private IsoDataSource dataSource;
	
	/**
	 * Create a world builder interface with a given GameWorld and GameLogic
	 * @param name
	 * @param world
	 * @param logic
	 */
	public IsoInterfaceWorldBuilder(String name, final GameWorld world, GameLogic logic) {
		this.world = world;
		this.logic = logic;
		
		frame = new JFrame(name);
		dataSource = new IsoGameModelDataSource(this.world);
		canvas = new IsoCanvas(dataSource);
		canvas.addSelectionCallback(new IsoCanvas.SelectionCallback() {
			@Override
			public void selected(final IsoImage i, final Location l, MouseEvent event) {
				inspect(l);
			}
		});
		canvas.setDropTarget(new DropTarget(canvas, new ThingCreatorDnD.ThingDropListener(new ThingCreatorDnD.ThingDropListener.ThingDropListenerAction() {
			@Override
			public void thingCreatorDroped(Component onto, Point location, ThingCreator creator) {
				if(onto instanceof IsoCanvas) {
					IsoCanvas canvas = (IsoCanvas)onto;
					new Level.Location(dataSource.level(), canvas.getSquarePositionAtPoint(location), Direction.NORTH).put(creator.createThing(world));
				}
			}
		})));
		frame.setSize(300, 300);
		frame.add(canvas);
		
		inspector = new InspectorPanel(this);
		inspector.getContentPane().setLayout(new BoxLayout(inspector.getContentPane(), BoxLayout.Y_AXIS));
		inspector.setSize(200, 400);
		inspector.getContentPane().add(Box.createVerticalGlue());
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
	
	/**
	 * Inspect a given location
	 * @param l
	 */
	private void inspect(Location l) {
		inspector.inspect(l);
	}

	/**
	 * The GameWorld that this interface is using
	 * @return
	 */
	public GameWorld gameWorld() {
		return world;
	}
}
