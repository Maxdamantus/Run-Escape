package ui.isometric.builder;

import java.awt.Component;
import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import ui.isometric.IsoCanvas;
import ui.isometric.IsoDataSource;
import ui.isometric.IsoGameModelDataSource;
import ui.isometric.IsoImage;
import ui.isometric.builder.things.ThingCreator;
import ui.isometric.builder.things.ThingCreatorDnD;
import util.Direction;

import game.*;
import game.things.Player;

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
	private IsoDataSource dataSource;
	
	/**
	 * Create a world builder interface with a given GameWorld and ClientMessageHandler
	 * @param name
	 * @param world
	 * @param logic
	 */
	public IsoInterfaceWorldBuilder(String name, final GameWorld world, ClientMessageHandler logic) {
		this.world = world;
		
		frame = new JFrame(name);
		dataSource = new IsoGameModelDataSource(this.world);
		canvas = new IsoCanvas(dataSource);
		canvas.addSelectionCallback(new IsoCanvas.SelectionCallback() {
			@Override
			public void selected(final IsoImage i, final Location l, MouseEvent event) {
				if(event.getButton() == MouseEvent.BUTTON3 || event.isControlDown()) { // Right click
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
											for(GameThing t : dataSource.level()) {
												if(t instanceof Player) {
													thing.interact(m.getText(), (Player)t);
													break;
												}
											}
										}
									}
								});
								popup.add(item);
							}
							popup.show(canvas, event.getPoint().x, event.getPoint().y);
						}
					}
				}
				else {
					inspect(l);
				}
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
