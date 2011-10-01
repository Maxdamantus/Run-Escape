package ui.isometric.builder;

import java.awt.Component;
import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileFilter;

import data.Database;

import ui.isometric.IsoCanvas;
import ui.isometric.IsoDataSource;
import ui.isometric.IsoGameModelDataSource;
import ui.isometric.IsoImage;
import ui.isometric.builder.things.ThingCreator;
import ui.isometric.builder.things.ThingCreatorDnD;
import util.Direction;
import util.Resources;

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
	
	private static final String EXTENTION = "wblrd";
	
	/**
	 * Create a world builder interface with a given GameWorld and ClientMessageHandler
	 * @param name
	 * @param world
	 * @param logic
	 */
	public IsoInterfaceWorldBuilder(String name, final GameWorld world, ClientMessageHandler logic) {
		this.world = world;
		
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
		dataSource = new IsoGameModelDataSource(this.world);
		canvas = new IsoCanvas(dataSource);
		canvas.addSelectionCallback(new IsoCanvas.SelectionCallback() {
			@Override
			public void selected(final IsoImage i, final Location l, MouseEvent event) {
				if(event.getButton() == MouseEvent.BUTTON3 || event.isControlDown()) { // Right click
					if(i != null) {
						if(event.getButton() == MouseEvent.BUTTON3 || event.isControlDown()) { // Right click
							JPopupMenu popup = new JPopupMenu();
							for(GameThing t : i.gameThing().location().contents()) {
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
												for(GameThing t : dataSource.level()) {
													if(t instanceof Player) {
														System.out.println(m.getName());
														world.thingWithGID(Long.parseLong(m.getName())).interact(m.getText().substring(3), (Player)t);
														break;
													}
												}
											}
										}
									});
									popup.add(item);
								}
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
		library.setSize(200, 400);
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
		
		world.fromTree(Database.xmlToTree(loaded));
	}
}
