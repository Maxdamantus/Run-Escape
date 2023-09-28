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
					if(t instanceof game.things.OpenableFurniture) {
						ins = new OpenableFurnitureInspector((game.things.OpenableFurniture)t, this);
					}
					else if(t instanceof game.things.Door) {
						ins = new DoorInspector((game.things.Door)t, this);
					}
					else if(t instanceof game.things.Stackable) {
						ins = new StackableInspector((game.things.Stackable)t, this);
					}
					else if(t instanceof game.things.Enemy) {
						ins = new NPCInspector((game.things.Enemy)t, this);
					}
					else if(t instanceof game.things.ShopKeeper) {
						ins = new ShopKeeperInspector((game.things.ShopKeeper)t, this);
					}
					else if(t instanceof game.things.Stairs) {
						ins = new StairInspector((game.things.Stairs)t, this);
					}
					else if(t instanceof game.things.Light) {
						ins = new LightInspector((game.things.Light)t, this);
					}
					else if(t instanceof game.things.Key) {
						ins = new KeyInspector((game.things.Key)t, this);
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
