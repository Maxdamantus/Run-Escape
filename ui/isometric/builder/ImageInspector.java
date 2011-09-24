package ui.isometric.builder;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import clientinterface.Conversions;

import ui.isometric.IsoImage;
import ui.isometric.IsoRendererLibrary;
import util.Direction;

public class ImageInspector extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private ImagePanel image;
	private game.GameThing thing;
	private IsoImage isoImage;

	private InspectorPanel panel;

	public ImageInspector(game.GameThing t, InspectorPanel inspectorPanel) {
		this.thing = t;
		this.isoImage = IsoRendererLibrary.newImageFromGameThing(null, Conversions.fromServerGameThing(t), Direction.NORTH);
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
	
	private void rotateCW() {
		game.GameThing thing = panel.builder().serverGameModel().thingWithGID(isoImage.gameThing().gid());
		thing.getLevel().rotate(Direction.WEST, thing);
		this.refresh();
	}
	
	private void refresh() {
		InspectorPanel.signalUpdate();
		isoImage = IsoRendererLibrary.newImageFromGameThing(null, Conversions.fromServerGameThing(thing), Direction.NORTH);
		image.setImage(isoImage.image());
		image.repaint();
	}

	private void rotateCCW() {
		game.GameThing thing = panel.builder().serverGameModel().thingWithGID(isoImage.gameThing().gid());
		thing.getLevel().rotate(Direction.EAST, thing);
		this.refresh();
	}
	
	private void remove() {
		System.out.println("Remove image " + isoImage.gameThing().renderer());
	}
}
