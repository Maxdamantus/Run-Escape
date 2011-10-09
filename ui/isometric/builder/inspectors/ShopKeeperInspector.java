package ui.isometric.builder.inspectors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import ui.isometric.builder.InspectorPanel;
import game.Container;
import game.things.ShopKeeper;

/**
 * A ShopKeeperInspector, allows you to configure a shopkeeper
 * 
 * @author melby
 *
 */
public class ShopKeeperInspector extends GameThingInspector<ShopKeeper> {
	private static final long serialVersionUID = 1L;
	private Map<String, ContainerInspector> windows = new HashMap<String, ContainerInspector>();

	public ShopKeeperInspector(final ShopKeeper t, InspectorPanel inspectorPanel) {
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
		
		final JLabel label = new JLabel(t.name());
		
		JButton nameButton = new JButton("Set Name");
		nameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String name = JOptionPane.showInputDialog("Enter name");
				t.name(name);
				label.setText(name);
			}
		});
		this.add(nameButton);
		this.add(label);
		
		JButton addPart = new JButton("Add Part");
		addPart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String name = JOptionPane.showInputDialog("Enter part name");
				t.addPart(name);
			}
		});
		this.add(addPart);
	}
	
	/**
	 * Show the container window for this item
	 * @param c
	 * @param name
	 */
	private void showContainer(Container c, final String name) {
		if(windows.get(name) == null) {
			ContainerInspector temp = new ContainerInspector(c, name);
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
}
