package ui.isometric.builder.inspectors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import game.things.Door;
import ui.isometric.builder.InspectorPanel;

/**
 * A Inspector for a door
 * 
 * @author melby
 *
 */
public class DoorInspector extends GameThingInspector<Door> {
	private static final long serialVersionUID = 1L;

	/**
	 * Create a DoorInspector with a given Door
	 * @param t
	 * @param inspectorPanel
	 */
	public DoorInspector(final Door t, InspectorPanel inspectorPanel) {
		super(t, inspectorPanel);
		
		String key = t.doorcode();
		final JLabel label = new JLabel(key);
		
		JButton openButton = new JButton("Toggle Open");
		openButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				t.toggle();
				refresh();
			}
		});
		this.add(openButton);
		JButton setPasskey = new JButton("Set Passkey");
		setPasskey.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String key = JOptionPane.showInputDialog("Enter Passkey");
				if(key == null) return;
				t.setDoorcode(key);
				label.setText(key);
			}
		});
		this.add(setPasskey);
		this.add(label);
	}
}
