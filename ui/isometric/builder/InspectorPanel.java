package ui.isometric.builder;

import java.util.HashSet;
import java.util.Set;

import javax.swing.Box;
import javax.swing.JFrame;

import game.*;

public class InspectorPanel extends JFrame {
	private static final long serialVersionUID = 1L;

	private static Set<Runnable> updaters = new HashSet<Runnable>();
	
	private IsoInterfaceWorldBuilder builder;

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
	}
	
	public void inspect(GameThing g) {
		getContentPane().removeAll();
		
		if(g != null) {
			Location l = g.location();
			if(l instanceof Level.Location) {
				Iterable<game.GameThing> things = builder.gameModel().level(0).portion(g.area().translated(((Level.Location)l).position()));
				
				for(game.GameThing t : things) {
					ImageInspector ins = new ImageInspector(t, this);
					getContentPane().add(ins);
				}
			}
		}
		
		getContentPane().add(Box.createVerticalGlue());
		getContentPane().add(new InspectorOptionsPanel(g));
		validate();
	}

	public IsoInterfaceWorldBuilder builder() {
		return builder;
	}
}
