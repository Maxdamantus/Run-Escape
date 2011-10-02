package ui.isometric.builder;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.TransferHandler;

import ui.isometric.IsoRendererLibrary;
import ui.isometric.builder.things.ThingCreator;
import ui.isometric.builder.things.ThingCreatorDnD;
import ui.isometric.builder.things.ThingLibrary;

/**
 * 
 * A window with all the different types of GameThings
 * that can be dragged out and used in the world
 * 
 * @author melby
 *
 */
public class LibraryFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create a LibraryFrame with the default GameThings and IsoInterfaceWorldBuilder
	 * @param builder
	 */
	public LibraryFrame(IsoInterfaceWorldBuilder builder) { // TODO: categories
		super(builder.frameName()+" - Image Library");
		
		int cols = 10;
		int rows = IsoRendererLibrary.allRendererNames().size()/cols;
		
		this.getContentPane().setLayout(new GridLayout(rows, cols));
		
		for(ThingCreator g : ThingLibrary.creators()) {
			ImagePanel panel = new ImagePanel(g.previewImage());
			panel.addMouseListener(new MouseAdapter() {
				@Override()
			    public void mousePressed(MouseEvent e) {			        
			        JComponent c = (JComponent) e.getSource();
			        TransferHandler handler = c.getTransferHandler();
			        handler.exportAsDrag(c, e, TransferHandler.COPY);
			    }
			});
			panel.setTransferHandler(new ThingCreatorDnD.ThingCreatorToThingTransferHandler());
			panel.setDragObject(g);
			this.add(panel);
		}
		
		this.validate();
	}
}
