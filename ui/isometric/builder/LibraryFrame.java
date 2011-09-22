package ui.isometric.builder;

import java.awt.GridLayout;

import javax.swing.JFrame;

import ui.isometric.IsoRendererLibrary;
import util.Direction;

public class LibraryFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public LibraryFrame() { // TODO: categories
		int cols = 10;
		int rows = IsoRendererLibrary.allRendererNames().size()/cols;
		
		this.getContentPane().setLayout(new GridLayout(rows, cols));
		
		for(String s : IsoRendererLibrary.allRendererNames()) {
			this.add(new ImagePanel(IsoRendererLibrary.imageForRendererName(s, Direction.NORTH)));
		}
		
		this.validate();
	}
}
