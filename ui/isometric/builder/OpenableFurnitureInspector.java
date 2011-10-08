package ui.isometric.builder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Map;

import javax.swing.JButton;

import game.Container;
import game.things.OpenableFurniture;

/**
 * A inspector for OpenableFurniture, allows you to inspect the contents
 * 
 * @author ruarusmelb
 *
 */
public class OpenableFurnitureInspector extends GameThingInspector {
	private static final long serialVersionUID = 1L;
	
	private ContainerInspector window;
	
	/**
	 * create a OpenableFurnitureInspector with a given thing and InspectorPanel
	 * 
	 * @param thing
	 * @param inspectorPanel
	 */
	public OpenableFurnitureInspector(final OpenableFurniture thing, InspectorPanel inspectorPanel) {
		super(thing, inspectorPanel);
		
		JButton inspectContainer = new JButton("Inspect Contents");
		inspectContainer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Map<String, Container> map = thing.getContainer();
				if(map.values().size() == 0) {
					System.err.println("No container...");
				}
				for(Container c : map.values()) {
					if(c != null) {
						showContainer(c, thing.name());
						break;
					}
					
					System.err.println("Null container..." + c);
				}
			}
		});
		this.add(inspectContainer);
	}
	
	/**
	 * Show the container window for this item
	 * @param c
	 * @param name
	 */
	private void showContainer(Container c, String name) {
		if(window == null) {
			window = new ContainerInspector(c, name);
			window.addWindowListener(new WindowListener() {
				@Override
				public void windowOpened(WindowEvent e) {}

				@Override
				public void windowClosing(WindowEvent e) {
					window = null;
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
			window.setVisible(true);
		}
		else {
			window.setVisible(true);
		}
	}
}
