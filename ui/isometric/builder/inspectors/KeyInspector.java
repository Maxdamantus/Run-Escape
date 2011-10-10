package ui.isometric.builder.inspectors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import game.things.Key;
import ui.isometric.builder.InspectorPanel;

/**
 * A Inspector for a key
 * 
 * @author melby
 *
 */
public class KeyInspector extends GameThingInspector<Key> {
	private static final long serialVersionUID = 1L;

	/**
	 * Create a KeyInspector with a given Key
	 * @param t
	 * @param inspectorPanel
	 */
	public KeyInspector(final Key t, InspectorPanel inspectorPanel) {
		super(t, inspectorPanel);
		
		final JLabel label = new JLabel(t.doorcode());
		
		JButton setPasskey = new JButton("Set Passkey");
		setPasskey.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String key = JOptionPane.showInputDialog("Enter Passkey");
				if(key == null) return;
				if(key.length() == 0) {
					key = null;
				}
				t.setDoorcode(key);
				label.setText(key);
			}
		});
		this.add(setPasskey);
		this.add(label);
	}
}
