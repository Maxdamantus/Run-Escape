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

	public ImageInspector(IsoImage im) {
		isoImage = im;
		
		this.setSize(0, 50);
		this.setPreferredSize(new Dimension(200000, 50));
		
		image = new ImagePanel(im);
		image.setSize(50, 50);
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
	}
	
	private void rotateCW() {
		System.out.println("Set image " + isoImage.gameThing().renderer() + " from direction " + isoImage.gameThing().direction() + " to " + isoImage.gameThing().direction().compose(Direction.WEST));
	}
	
	private void rotateCCW() {
		System.out.println("Set image " + isoImage.gameThing().renderer() + " from direction " + isoImage.gameThing().direction() + " to " + isoImage.gameThing().direction().compose(Direction.EAST));
	}
}
