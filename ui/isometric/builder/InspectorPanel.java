package ui.isometric.builder;

import java.awt.Component;
import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Box;
import javax.swing.JFrame;

import ui.isometric.builder.things.ThingCreator;
import ui.isometric.builder.things.ThingCreatorDnD;
import util.Area;

import game.*;

/**
 * A panel for inspecting locations
 * 
 * @author melby
 *
 */
public class InspectorPanel extends JFrame {
	private static final long serialVersionUID = 1L;

	private static Set<Runnable> updaters = new HashSet<Runnable>();
	
	private IsoInterfaceWorldBuilder builder;
	private Location loc = null;

	/**
	 * Register for updates to the world
	 * @param r
	 */
	public static void registerForUpdates(Runnable r) { // TODO: use world deltas
		updaters.add(r);
	}
	
	/**
	 * Signal a update to the world
	 */
	public static void signalUpdate() {
		for(Runnable r : updaters) {
			r.run();
		}
	}
	
	/**
	 * Create a InspectorPanel with a IsoInterfaceWorldBuilder that is managing it
	 * @param builder
	 */
	public InspectorPanel(final IsoInterfaceWorldBuilder builder) {
		super(builder.frameName()+" - Inspector");
		
		this.builder = builder;
		
		registerForUpdates(new Runnable() {
			@Override
			public void run() {
				inspect(loc);
			}
		});
		
		this.setDropTarget(new DropTarget(this, new ThingCreatorDnD.ThingDropListener(new ThingCreatorDnD.ThingDropListener.ThingDropListenerAction() {
			@Override
			public void thingCreatorDroped(Component onto, Point location, ThingCreator creator) {
				if(loc != null) {
					loc.put(creator.createThing(builder.gameWorld()));
					signalUpdate();
				}
			}
		})));
	}
	
	/**
	 * Inspect a given Location
	 * @param l
	 */
	public void inspect(Location l) {
		loc = l;
		
		getContentPane().removeAll();
		
		if(l != null) {
			if(l instanceof Level.Location) {
				Iterable<game.GameThing> things = ((Level.Location) l).level().portion(new Area(((Level.Location)l).position(), 1, 1));
				
				for(game.GameThing t : things) {
					GameThingInspector ins = new GameThingInspector(t, this);
					getContentPane().add(ins);
				}
			}
		}
		
		getContentPane().add(Box.createVerticalGlue());
		validate();
		repaint();
	}

	/**
	 * The IsoInterfaceWorldBuilder that is managing this InspectorPanel
	 * @return
	 */
	public IsoInterfaceWorldBuilder builder() {
		return builder;
	}
}
