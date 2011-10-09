package ui.isometric.builder.inspectors;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Timer;
import java.util.TimerTask;

import game.Container;
import game.GameThing;
import game.things.PickupGameThing;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import ui.isometric.builder.things.ThingCreator;
import ui.isometric.builder.things.ThingCreatorDnD;
import ui.isometric.libraries.IsoInventoryImageLibrary;
import util.GUI;
import util.ImagePanel;

/**
 * A container inspector, allows you to add or remove items from a container
 * 
 * @author ruarusmelb
 *
 */
public class ContainerInspector extends JFrame implements WindowListener, MouseListener {
	private static final long serialVersionUID = 1L;
	private Container container;
	private Timer t;
	
	/**
	 * Create a ContainerInspector for a given container and frame name
	 * 
	 * @param c
	 * @param name
	 */
	public ContainerInspector(Container c, String name) {
		super(name +" inspector");
		container = c;
		
		this.setSize(400, 300);
		GUI.centerWindow(this);
		
		this.getContentPane().setLayout(new GridLayout(5, 5));
		
		t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				refreshContainer();
			}
		}, 0, 200);
		
		this.addWindowListener(this);
		this.addMouseListener(this);
		this.setDropTarget(new DropTarget(this, new ThingCreatorDnD.ThingDropListener(new ThingCreatorDnD.ThingDropListener.ThingDropListenerAction() {
			@Override
			public void thingCreatorDroped(Component onto, Point location, ThingCreator creator) {
				GameThing thing = creator.createThing(container.world());
				if(thing instanceof PickupGameThing) {
					container.put(thing);
				}
				else {
					container.world().forget(thing);
				}
			}
		})));
	}
	
	/**
	 * Refresh the container, this is called ever 5th of a second
	 */
	private void refreshContainer() {
		this.getContentPane().removeAll();
		container.lock().readLock().lock();
		try {
			for(GameThing g : container) {
				ImagePanel panel = new ImagePanel(IsoInventoryImageLibrary.imageForName(g.renderer()));
				panel.setDragObject(g);
				this.getContentPane().add(panel);
			}
		}
		finally {
			container.lock().readLock().unlock();
		}
		this.getContentPane().validate();
		this.getContentPane().repaint();
	}
	
	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {
		t.cancel();
	}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON3) { // Right click
			Component source = this.getContentPane().getComponentAt(e.getPoint());
			if(source instanceof ImagePanel) {
				ImagePanel p = (ImagePanel)source;
				Object o = p.dragObject();
				
				if(o instanceof GameThing) {
					final GameThing thing = (GameThing)o;
					
					JPopupMenu m = new JPopupMenu();
					JMenuItem i = new JMenuItem("Remove");
					i.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							game.LocationS.NOWHERE.put(thing);
							thing.world().forget(thing);
						}
					});
					m.add(i);
					m.show(this, e.getPoint().x, e.getPoint().y);
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}
