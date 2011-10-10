package ui.isometric.builder.inspectors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import ui.isometric.builder.InspectorPanel;
import game.things.Light;

/**
 * a Light inspector
 * 
 * @author melby
 *
 */
public class LightInspector extends GameThingInspector<Light> {
	private static final long serialVersionUID = 1L;

	/**
	 * Create a LightInspector using the given light
	 * @param t
	 * @param inspectorPanel
	 */
	public LightInspector(final Light t, InspectorPanel inspectorPanel) {
		super(t, inspectorPanel);
		
		final JLabel label = new JLabel(t.luminance()+"");
		
		JButton changeradius = new JButton("Set Radius");
		changeradius.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int amount = -1;
				while(amount < 1) {
					String string = JOptionPane.showInputDialog("Enter radius (squares)");
					if(string == null) return;
					try {
						amount = Integer.parseInt(string);
					}
					catch(NumberFormatException e) {};
				}
				t.luminance(amount);
				label.setText(amount + "");
			}
		});
		this.add(changeradius);
		this.add(label);
	}
}
