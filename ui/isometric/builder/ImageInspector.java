package ui.isometric.builder;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import ui.isometric.IsoImage;
import util.Direction;

public class ImageInspector extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private ImagePanel image;
	private IsoImage isoImage;

	private IsoInterfaceWorldBuilder builder;

	public ImageInspector(IsoImage im, IsoInterfaceWorldBuilder b) {
		isoImage = im;
		builder = b;
		
		this.setSize(0, 50);
		this.setPreferredSize(new Dimension(200000, 50));
		
		image = new ImagePanel(im.image());
		image.setSize(im.width(), im.height());
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
		game.GameThing thing = builder.serverGameModel().thingWithGID(isoImage.gameThing().gid());
		thing.getLevel().rotate(Direction.WEST, thing);
		this.refresh();
	}
	
	private void refresh() {
		// TODO: refresh inspector
	}

	private void rotateCCW() {
		game.GameThing thing = builder.serverGameModel().thingWithGID(isoImage.gameThing().gid());
		thing.getLevel().rotate(Direction.EAST, thing);
		this.refresh();
	}
	
	private void remove() {
		System.out.println("Remove image " + isoImage.gameThing().renderer());
	}
}
