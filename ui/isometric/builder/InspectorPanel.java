package ui.isometric.builder;

import java.awt.Component;
import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Box;
import javax.swing.JFrame;

import ui.isometric.builder.inspectors.ContainableInspector;
import ui.isometric.builder.inspectors.DoorInspector;
import ui.isometric.builder.inspectors.GameThingInspector;
import ui.isometric.builder.inspectors.KeyInspector;
import ui.isometric.builder.inspectors.LightInspector;
import ui.isometric.builder.inspectors.NPCInspector;
import ui.isometric.builder.inspectors.OpenableFurnitureInspector;
import ui.isometric.builder.inspectors.ShopKeeperInspector;
import ui.isometric.builder.inspectors.StackableInspector;
import ui.isometric.builder.inspectors.StairInspector;
import ui.isometric.builder.things.ThingCreator;
import ui.isometric.builder.things.ThingCreatorDnD;
import util.Area;

import game.*;
import game.things.Door;
import game.things.Enemy;
import game.things.Key;
import game.things.Light;
import game.things.OpenableFurniture;
import game.things.ShopKeeper;
import game.things.Stackable;
import game.things.Stairs;

/**
 * A self-configuring panel for inspecting locations
 * 
 * @author melby
 *
 */
public class InspectorPanel extends JFrame {
	private static final long serialVersionUID = 1L;

	private static Set<Runnable> updaters = new HashSet<Runnable>();
	
	private BuilderInterface builder;
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
	public InspectorPanel(final BuilderInterface builder) {
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
					loc.put(creator.createThing(builder.world(), loc));
					signalUpdate();
				}
			}
		})));
	}
	
	/**
	 * Inspect a given Location
	 * @param l
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void inspect(Location l) {
		loc = l;
		
		getContentPane().removeAll();
		
		if(l != null) {
			if(l instanceof Level.Location) {
				Iterable<game.GameThing> things = ((Level.Location) l).level().portion(new Area(((Level.Location)l).position(), 1, 1));
				
				for(game.GameThing t : things) {
					GameThingInspector<?> ins;
					if(t instanceof OpenableFurniture) {
						ins = new OpenableFurnitureInspector((OpenableFurniture)t, this);
					}
					else if(t instanceof Door) {
						ins = new DoorInspector((Door)t, this);
					}
					else if(t instanceof Stackable) {
						ins = new StackableInspector((Stackable)t, this);
					}
					else if(t instanceof Enemy) {
						ins = new NPCInspector((Enemy)t, this);
					}
					else if(t instanceof ShopKeeper) {
						ins = new ShopKeeperInspector((ShopKeeper)t, this);
					}
					else if(t instanceof Stairs) {
						ins = new StairInspector((Stairs)t, this);
					}
					else if(t instanceof Light) {
						ins = new LightInspector((Light)t, this);
					}
					else if(t instanceof Key) {
						ins = new KeyInspector((Key)t, this);
					}
					else if(t instanceof Containable) {
						ins = new ContainableInspector(t, this); // TODO: exception ever thrown?
					}
					else {
						ins = new GameThingInspector<GameThing>(t, this);
					}
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
	public BuilderInterface builder() {
		return builder;
	}
}
