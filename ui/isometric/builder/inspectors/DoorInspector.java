package ui.isometric.builder.inspectors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

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
		
		JButton button = new JButton("Toggle Open");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				t.toggle();
				refresh();
			}
		});
		this.add(button);
	}
}
