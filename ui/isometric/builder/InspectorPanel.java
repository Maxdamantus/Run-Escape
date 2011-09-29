package ui.isometric.builder;

import java.util.HashSet;
import java.util.Set;

import javax.swing.Box;
import javax.swing.JFrame;

import util.Area;

import game.*;

public class InspectorPanel extends JFrame {
	private static final long serialVersionUID = 1L;

	private static Set<Runnable> updaters = new HashSet<Runnable>();
	
	private IsoInterfaceWorldBuilder builder;
	private Location loc = null;

	public static void registerForUpdates(Runnable r) {
		updaters.add(r);
	}
	
	public static void signalUpdate() {
		for(Runnable r : updaters) {
			r.run();
		}
	}
	
	public InspectorPanel(IsoInterfaceWorldBuilder builder) {
		this.builder = builder;
		
		registerForUpdates(new Runnable() {
			@Override
			public void run() {
				inspect(loc);
			}
		});
	}
	
	public void inspect(Location l) {
		loc = l;
		
		getContentPane().removeAll();
		
		if(l != null) {
			if(l instanceof Level.Location) {
				Iterable<game.GameThing> things = ((Level.Location) l).level().portion(new Area(((Level.Location)l).position(), 1, 1));
				
				for(game.GameThing t : things) {
					ImageInspector ins = new ImageInspector(t, this);
					getContentPane().add(ins);
				}
			}
		}
		
		getContentPane().add(Box.createVerticalGlue());
		validate();
	}

	public IsoInterfaceWorldBuilder builder() {
		return builder;
	}
}
