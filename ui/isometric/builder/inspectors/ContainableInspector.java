package ui.isometric.builder.inspectors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;

import ui.isometric.builder.InspectorPanel;
import game.Containable;
import game.Container;
import game.GameThing;

/**
 * An inspector for Containable GameThings
 * @author melby
 *
 * @param <T>
 */
public class ContainableInspector<T extends GameThing & Containable> extends GameThingInspector<T> {
	private static final long serialVersionUID = 1L;
	private Map<String, ContainerInspector> windows = new HashMap<String, ContainerInspector>();

	/**
	 * Create a ContainableInspector with the given Containable GameThing
	 * @param t
	 * @param inspectorPanel
	 */
	public ContainableInspector(final T t, InspectorPanel inspectorPanel) {
		super(t, inspectorPanel);
		
		JButton inspectContainer = new JButton("Inspect Contents");
		inspectContainer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Map<String, Container> map = t.getContainers();
				for(String s : map.keySet()) {
					if(map.get(s) != null) {
						showContainer(map.get(s), s);
					}
				}
			}
		});
		this.add(inspectContainer);
	}
	
	/**
	 * Show the container window for this item
	 * @param container
	 * @param name
	 */
	private void showContainer(Container container, final String name) {
		if(windows.get(name) == null) {
			ContainerInspector temp = new ContainerInspector(container, name, dropWrapper());
			temp.addWindowListener(new WindowListener() {
				@Override
				public void windowOpened(WindowEvent e) {}

				@Override
				public void windowClosing(WindowEvent e) {
					windows.remove(name);
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
			});
			temp.setVisible(true);
			windows.put(name, temp);
		}
		else {
			windows.get(name).setVisible(true);
		}
	}
	
	/**
	 * Get the DropWrapper to use for the ContainerInspector.
	 * Note, may be null
	 * @return
	 */
	protected ContainerInspector.DropWrapper dropWrapper() {
		return null;
	}
}
