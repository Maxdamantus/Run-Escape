package ui.isometric.builder;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import data.Database;

import serialization.ParseException;
import ui.isometric.IsoCanvas;
import ui.isometric.IsoInterface;
import ui.isometric.abstractions.IsoObject;
import ui.isometric.abstractions.IsoPlayer;
import ui.isometric.builder.things.ThingCreator;
import ui.isometric.builder.things.ThingCreatorDnD;
import ui.isometric.datasource.IsoChangeLevelDataSource;
import util.Direction;
import util.Resources;

import game.*;

/**
 * 
 * The overall class that manages a builder user interface
 * 
 * @author melby
 *
 */
public class BuilderInterface implements IsoInterface {
	private JFrame frame;
	private InspectorPanel inspector;
	private LibraryFrame library;
	private String frameName;
	
	private IsoCanvas canvas;
	
	private GameWorld world;
	private IsoChangeLevelDataSource dataSource;
	
	private static final String EXTENTION = "wblrd";
	
	private ThingCreator storedCreator = null;
	
	/**
	 * Create a world builder interface with a given GameWorld and ClientMessageHandler
	 * @param name
	 * @param world
	 * @param logic
	 */
	public BuilderInterface(String name, final GameWorld world, ClientMessageHandler logic) {
		this.world = world;
		this.frameName = name;
		
		JMenuBar bar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem save = new JMenuItem("Save");
		file.add(save);
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				save();
			}
		});
		JMenuItem load = new JMenuItem("Load");
		file.add(load);
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				load();
			}
		});
		bar.add(file);
		
		frame = new JFrame(name);
		frame.setJMenuBar(bar);
		dataSource = new IsoChangeLevelDataSource(this.world);
		canvas = new IsoCanvas(dataSource);
		canvas.addSelectionCallback(new IsoCanvas.SelectionCallback() {
			@Override
			public void selected(final IsoObject i, final Location l, MouseEvent event) {
				if(event.getButton() == MouseEvent.BUTTON3 || event.isControlDown()) { // Right click
					if(storedCreator != null) {
						canvas.calculateTypesAtAtPoint(event.getPoint());
						l.put(storedCreator.createThing(world, l));
					}
				}
				else {
					if(event.isShiftDown()) {
						if(i != null) {
							Location loc = i.gameThing().location();
							if(loc instanceof Level.Location) {
								((Level.Location)loc).rotate(Direction.EAST).put(i.gameThing());
							}
						}
					}
					else {
						storedCreator = null;
						inspect(l);
					}
				}
			}
		});
		canvas.setDropTarget(new DropTarget(canvas, new ThingCreatorDnD.ThingDropListener(new ThingCreatorDnD.ThingDropListener.ThingDropListenerAction() {
			@Override
			public void thingCreatorDroped(Component onto, Point location, ThingCreator creator) {
				if(onto instanceof IsoCanvas) {
					storedCreator = creator;
					IsoCanvas canvas = (IsoCanvas)onto;
					canvas.calculateTypesAtAtPoint(location);
					Location l = dataSource.level().location(canvas.getCachedSelectedSquarePosition(), Direction.NORTH);
					l.put(creator.createThing(world, l));
				}
			}
		})));
		canvas.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {}

			@Override
			public void keyReleased(KeyEvent arg0) {}

			@Override
			public void keyTyped(KeyEvent arg0) {
				if(arg0.getKeyChar() == 'r') {
					canvas.setViewDirection(canvas.viewDirection().compose(Direction.EAST));
				}
				else if(arg0.getKeyChar() == 'u') {
					dataSource.goUp();
				}
				else if(arg0.getKeyChar() == 'd') {
					dataSource.goDown();
				}
			}
		});
		frame.add(canvas);
		
		inspector = new InspectorPanel(this);
		inspector.getContentPane().setLayout(new BoxLayout(inspector.getContentPane(), BoxLayout.Y_AXIS));
		inspector.getContentPane().add(Box.createVerticalGlue());
		inspector.validate();
		
		library = new LibraryFrame(this);
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		screen.height -= 100;
		double perx = 0.3;
		
		library.setLocation(0, 0);
		library.setSize((int)(screen.width*perx), (int)(screen.height*0.5));
		inspector.setLocation(0, (int)(screen.height*0.5));
		inspector.setSize((int)(screen.width*perx), (int)(screen.height*0.5));
		frame.setLocation((int)(screen.width*perx), 0);
		frame.setSize((int)(screen.width*(1-perx)), (int)(screen.height));
	}
	
	@Override
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

	@Override
	public GameWorld world() {
		return world;
	}
	
	/**
	 * Save the world
	 */
	public void save() {
		String file = Database.treeToXML(world.toTree());
		
		JFileChooser chooser = new JFileChooser();
		if(chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
			File save = chooser.getSelectedFile().getAbsoluteFile();
			if(!save.getAbsolutePath().endsWith("."+EXTENTION)) {
				save = new File(save.getAbsolutePath() + "." + EXTENTION);
			}
			if(save.exists()) {
				if(JOptionPane.showConfirmDialog(frame, "This file exists, are you sure you wish to overwrite it?", null, JOptionPane.OK_CANCEL_OPTION, 0) == JOptionPane.CANCEL_OPTION) {
					return;
				}
			}
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(save));
				writer.write(file);
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Load the world
	 */
	public void load() {
		String loaded = null;
		
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File arg0) {
				return arg0.isFile() && arg0.getAbsolutePath().endsWith(EXTENTION);
			}

			@Override
			public String getDescription() {
				return "World Builder File";
			}
		});
		if(chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
			File load = chooser.getSelectedFile().getAbsoluteFile();
			try {
				loaded = Resources.loadTextFile(load.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			return;
		}
		
		if(loaded == null) {
			JOptionPane.showMessageDialog(frame, "Error loading file");
			return;
		}
		
		try {
			world.fromTree(Database.xmlToTree(loaded));
		} catch (ParseException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Error loading file");
		}
	}
	
	/**
	 * Get the name of the frame
	 * @return
	 */
	public String frameName() {
		return frameName;
	}

	@Override
	public IsoCanvas canvas() {
		return canvas;
	}

	@Override
	public void performActionOn(String interaction, GameThing thing) {
		thing.interact(interaction, null);
	}

	@Override
	public IsoPlayer player() {
		return null;
	}

	@Override
	public void incomingChat(String message, Color color) {
		System.out.println("Got Chat: "+message);
	}

	@Override
	public void sendChatMessage(String message) {
		System.out.println("Send Chat: "+message);
	}
}
