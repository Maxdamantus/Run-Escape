package ui.isometric.builder;

import game.*;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import ui.isometric.abstractions.IsoImage;
import ui.isometric.libraries.IsoRendererLibrary;
import util.Direction;

/**
 * This class is an inspector for a single GameThing.
 * It is a self-configuring JPanel and can be added to many interfaces
 * @author melby
 *
 */
public class GameThingInspector extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private ImagePanel image;
	private GameThing thing;
	private IsoImage isoImage;

	private InspectorPanel panel;

	/**
	 * Create a GameThingInspector with a given GameThing and InspectorPanel
	 * @param t - any GameThing
	 * @param inspectorPanel - the InspectorPanel managing this GameThingInspector
	 */
	public GameThingInspector(GameThing t, InspectorPanel inspectorPanel) {
		this.thing = t;
		this.isoImage = IsoRendererLibrary.newImageFromGameThing(null, t, Direction.NORTH);
		panel = inspectorPanel;
		
		this.setSize(0, 50);
		this.setPreferredSize(new Dimension(200000, 50));
		
		image = new ImagePanel(isoImage.image());
		image.setSize(isoImage.width(), isoImage.height());
		this.add(image);
		
		JButton rotateCW = new JButton("Rotate CW");
		rotateCW.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				rotateCW();
			}
		});
		this.add(rotateCW);
		
		JButton rotateCCW = new JButton("Rotate CCW");
		rotateCCW.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				rotateCCW();
			}
		});
		this.add(rotateCCW);
		
		JButton remove = new JButton("Remove");
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				remove();
			}
		});
		this.add(remove);
		
		this.validate();
	}
	
	/**
	 * Notify any inspectors of changes
	 */
	private void refresh() {
		InspectorPanel.signalUpdate();
	}
	
	/**
	 * Rotate the GameThing CW
	 */
	private void rotateCW() {
		Location l = thing.location();
		if(l instanceof Level.Location) {
			((Level.Location)l).rotate(Direction.WEST).put(thing);
		}
		else {
			System.err.println("Trying to rotate something weird"); // TODO: fix
		}
		this.refresh();
	}

	/**
	 * Rotate the GameThing CCW
	 */
	private void rotateCCW() {
		Location l = thing.location();
		if(l instanceof Level.Location) {
			((Level.Location)l).rotate(Direction.EAST).put(thing);
		}
		else {
			System.err.println("Trying to rotate something weird"); // TODO: fix
		}
		this.refresh();
	}
	
	/**
	 * Remove the GameThing
	 */
	private void remove() {
		game.LocationS.NOWHERE.put(thing);
		panel.builder().gameWorld().forget(thing);
		this.refresh();
	}
}
