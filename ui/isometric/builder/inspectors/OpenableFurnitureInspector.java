package ui.isometric.builder.inspectors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import ui.isometric.builder.InspectorPanel;

import game.things.OpenableFurniture;

/**
 * A inspector for OpenableFurniture, allows you to inspect the contents
 * 
 * @author ruarusmelb
 *
 */
public class OpenableFurnitureInspector extends ContainableInspector<OpenableFurniture> {
	private static final long serialVersionUID = 1L;
		
	/**
	 * create a OpenableFurnitureInspector with a given thing and InspectorPanel
	 * 
	 * @param thing
	 * @param inspectorPanel
	 */
	public OpenableFurnitureInspector(final OpenableFurniture thing, InspectorPanel inspectorPanel) {
		super(thing, inspectorPanel);
		
		String key = thing.doorcode();
		final JLabel label = new JLabel(key==null?"No key":key);
		
		JButton button = new JButton("Toggle Open");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				thing.toggle();
				refresh();
			}
		});
		this.add(button);
		JButton setPasskey = new JButton("Set Passkey");
		setPasskey.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String key = JOptionPane.showInputDialog("Enter Passkey");
				if(key == null) return;
				if(key.length() == 0) {
					key = null;
				}
				thing.setDoorcode(key);
				label.setText(key==null?"No key":key);
			}
		});
		this.add(setPasskey);
		this.add(label);
	}
}
