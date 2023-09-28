package ui.isometric.builder.inspectors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import ui.isometric.builder.InspectorPanel;
import game.things.Stairs;

/**
 * Inspector for stairs, allows you to adjust number of levels etc
 * 
 * @author melby
 *
 */
public class StairInspector extends GameThingInspector<Stairs> {
	private static final long serialVersionUID = 1L;

	/**
	 * Create a StairInspector with a given Stairs object
	 * 
	 * @param t
	 * @param inspectorPanel
	 */
	public StairInspector(final Stairs t, InspectorPanel inspectorPanel) {
		super(t, inspectorPanel);
		
		int movement = 0;
		if(t.up() > 0) {
			movement = t.up();
		}
		else if(t.down() > 0) {
			movement = -t.down();
		}
		
		final JLabel label = new JLabel(movement+" levels");
		
		JButton changeamount = new JButton("Set Levels");
		changeamount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int movement = -1;
				while(movement < 1) {
					String string = JOptionPane.showInputDialog("Enter Levels (-tve down, +tve up)");
					if(string == null) return;
					try {
						movement = Integer.parseInt(string);
					}
					catch(NumberFormatException e) {};
				}
				if(movement < 0) {
					t.up(0);
					t.down(-movement);
				}
				else if(movement > 0) {
					t.up(movement);
					t.down(0);
				}
				else {
					t.up(0);
					t.down(0);
				}
				label.setText(movement + " levels");
			}
		});
		this.add(changeamount);
		this.add(label);
	}
}
