package ui.isometric.builder.things;

import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceMotionListener;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import ui.isometric.builder.ImagePanel;

public class ThingCreatorDnD {
	public static class ThingCreatorTransfer implements Transferable {
		private static DataFlavor dragAndDropDataFlavor = null;
		
		private static DataFlavor getDragAndDropDataFlavor() {
			if(dragAndDropDataFlavor == null) {
				try {
					dragAndDropDataFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=" + ThingCreatorTransfer.class.getName());
				} catch (ClassNotFoundException e) {
					System.out.println("Unable to find ThingTransfer class: " + DataFlavor.javaJVMLocalObjectMimeType + ";class=" + ThingCreatorTransfer.class.getName());
					e.printStackTrace();
				}
	        }

	        return dragAndDropDataFlavor;
		}
		
		private ThingCreator creator;
		
		public ThingCreatorTransfer(ThingCreator creator) {
			this.creator = creator;
		}

		@Override
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
			if(flavor.equals(getDragAndDropDataFlavor())) {
				return creator;
			}
			
			return null;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { getDragAndDropDataFlavor() };
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavors) {
			return flavors.equals(getDragAndDropDataFlavor());
		}
		
	}
	
	public static class ThingCreatorToThingTransferHandler extends TransferHandler implements DragSourceMotionListener {
		private static final long serialVersionUID = 1L;

		@Override
		public Transferable createTransferable(JComponent c) {			
	        if(c instanceof ImagePanel) {
	        	ImagePanel p = (ImagePanel)c;
	        	if(p.dragObject() != null && p.dragObject() instanceof ThingCreator) {
		            Transferable tip = new ThingCreatorTransfer((ThingCreator)p.dragObject());
		            return tip;
	        	}
	        }

	        return null;
	    }

		@Override
	    public void dragMouseMoved(DragSourceDragEvent dsde) {}

	    @Override
	    public int getSourceActions(JComponent c) {	    	
	        if(c instanceof ImagePanel) {
	        	ImagePanel p = (ImagePanel)c;
	        	if(p.dragObject() != null && p.dragObject() instanceof ThingCreator) {
	        		return TransferHandler.COPY;
	        	}
	        }
	        	        
	        return TransferHandler.NONE;
	    }
	}
	
	public static class ThingDropListener implements DropTargetListener {
		public static interface ThingDropListenerAction {
			public void thingCreatorDroped(Component onto, Point location, ThingCreator creator);
		}
		
		private ThingDropListenerAction action;
		
	    public ThingDropListener(ThingDropListener.ThingDropListenerAction action) {
	    	this.action = action;
	    }

	    @Override
		public void dragEnter(DropTargetDragEvent arg0) {}

		@Override
		public void dragExit(DropTargetEvent arg0) {}

		@Override
		public void dragOver(DropTargetDragEvent arg0) {}

		@Override
		public void dropActionChanged(DropTargetDragEvent arg0) {}
		
		@Override
	    public void drop(DropTargetDropEvent drag) {			
	        DataFlavor dragAndDropFlavor = null;
	        
	        Object transferableObj = null;
	        Transferable transferable = null;
	        
	        try {
	        	dragAndDropFlavor = ThingCreatorTransfer.getDragAndDropDataFlavor();
	            
	            transferable = drag.getTransferable();
	            
	            if(transferable.isDataFlavorSupported(dragAndDropFlavor)) {
	                transferableObj = drag.getTransferable().getTransferData(dragAndDropFlavor);
	            } 
	            
	        } catch (Exception e) { }
	        
	        if(transferableObj == null) {
	            return;
	        }
	        
	        ThingCreator thingCreator = (ThingCreator)transferableObj;
	        
	        action.thingCreatorDroped(drag.getDropTargetContext().getComponent(), drag.getLocation(), thingCreator);
	        
	        drag.acceptDrop(TransferHandler.COPY); // TODO: working?
	    }
	}
}
