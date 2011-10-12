package ui.isometric.builder.inspectors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import ui.isometric.builder.InspectorPanel;
import game.Container;
import game.GameThing;
import game.GameWorld;
import game.things.PickupGameThing;
import game.things.ShopKeeper;

/**
 * A ShopKeeperInspector, allows you to configure a shopkeeper
 * 
 * @author melby
 *
 */
public class ShopKeeperInspector extends ContainableInspector<ShopKeeper> {
	private static final long serialVersionUID = 1L;
	private GameWorld world;

	public ShopKeeperInspector(final ShopKeeper t, InspectorPanel inspectorPanel) {
		super(t, inspectorPanel);
		world = t.world();
		
		final JLabel label = new JLabel(t.name());
		
		JButton nameButton = new JButton("Set Name");
		nameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String name = JOptionPane.showInputDialog("Enter name");
				if(name == null) return;
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
	
	protected ContainerInspector.DropWrapper dropWrapper() {
		return new ContainerInspector.DropWrapper() {
			@Override
			public GameThing wrap(PickupGameThing g, Container container) {
				try {
					String a = JOptionPane.showInputDialog("Enter an amount");
					if(a == null) return g;
					int amount = Integer.parseInt(a);
					String c = JOptionPane.showInputDialog("Enter a cost");
					if(c == null) return g;
					int cost = Integer.parseInt(c);
					return new game.things.ShopItem(world, g, amount, cost);
				}
				catch (NumberFormatException e) {
					return g;
				}
			}
		};
	}
}
