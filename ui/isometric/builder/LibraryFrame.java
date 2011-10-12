package ui.isometric.builder;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

import ui.isometric.builder.things.ThingCreator;
import ui.isometric.builder.things.ThingCreatorDnD;
import ui.isometric.builder.things.ThingLibrary;
import util.ImagePanel;

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
	 * A list of creators that can be dragged out of the view
	 * @author melby
	 *
	 */
	private class CreatorList extends JPanel {
		private static final long serialVersionUID = 1L;
		
		/**
		 * Create a CreatorList with a given list of ThingCreators
		 * @param creators
		 */
		public CreatorList(List<ThingCreator> creators) {
			int cols = (int)Math.sqrt(creators.size());
			int rows = (int)Math.ceil(creators.size()/(double)cols);
			
			this.setLayout(new GridLayout(rows, cols));
			
			for(ThingCreator g : creators) {
				ImagePanel panel = new ImagePanel(g.previewImage());
				panel.setToolTipText(g.description());
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
	
	private CreatorList list;
	
	/**
	 * Create a LibraryFrame with the default GameThings and IsoInterfaceWorldBuilder
	 * @param builder
	 */
	public LibraryFrame(BuilderInterface builder) {
		super(builder.frameName()+" - Image Library");
		
		this.getContentPane().setLayout(new BorderLayout());
		
		final JComboBox box = new JComboBox();
		for(String category : ThingLibrary.categories()) {
			box.addItem(category);
		}
		box.setSelectedItem(ThingLibrary.ALL);
		box.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				remove(list);
				list = new CreatorList(ThingLibrary.creatorsInCategory((String)box.getSelectedItem()));
				getContentPane().add(list, BorderLayout.CENTER);
				validate();
				doLayout();
			}
		});
		this.getContentPane().add(box, BorderLayout.NORTH);
		
		list = new CreatorList(ThingLibrary.creatorsInCategory(ThingLibrary.ALL));
		this.getContentPane().add(list, BorderLayout.CENTER);
		this.validate();
	}
}
