package ui.isometric.builder.inspectors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import ui.isometric.builder.InspectorPanel;
import game.things.Stackable;

/**
 * An inspector for stackable items
 * @author melby
 *
 */
public class StackableInspector extends GameThingInspector<Stackable> {
	private static final long serialVersionUID = 1L;

	/**
	 * Create a StackableInspector with a given game thing
	 * @param t
	 * @param inspectorPanel
	 */
	public StackableInspector(final Stackable t, InspectorPanel inspectorPanel) {
		super(t, inspectorPanel);
		
		final JLabel label = new JLabel(t.amount()+"");
		
		JButton changeamount = new JButton("Set Amount");
		changeamount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int amount = -1;
				while(amount < 1) {
					String string = JOptionPane.showInputDialog("Enter amount");
					try {
						amount = Integer.parseInt(string);
					}
					catch(NumberFormatException e) {};
				}
				t.amount(amount);
				label.setText(amount + "");
			}
		});
		this.add(changeamount);
		this.add(label);
	}
}
