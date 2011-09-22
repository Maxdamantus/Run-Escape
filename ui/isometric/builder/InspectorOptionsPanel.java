package ui.isometric.builder;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import ui.isometric.IsoSquare;

public class InspectorOptionsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private IsoSquare square;
	
	public InspectorOptionsPanel(IsoSquare sq) {
		square = sq;
		this.setPreferredSize(new Dimension(10000, 40));
		
		JButton add = new JButton("Add");
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addNewImage();
			}
		});
		this.add(add);
		
		this.validate();
	}
	
	private void addNewImage() {
		System.out.println("Add new image to " + square);
	}
}
