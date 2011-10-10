package ui.isometric.builder.inspectors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import ui.isometric.builder.InspectorPanel;
import game.things.Enemy;

/**
 * Inspector for NPCs
 * 
 * @author melby
 *
 */
public class NPCInspector extends GameThingInspector<Enemy> {
	private static final long serialVersionUID = 1L;

	/**
	 * Create a NPCInspector with a given NPC
	 * @param t
	 * @param inspectorPanel
	 */
	public NPCInspector(final Enemy t, InspectorPanel inspectorPanel) {
		super(t, inspectorPanel);
		
		final JLabel name = new JLabel(t.name());
		
		JButton setName = new JButton("Set Name");
		setName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String string = JOptionPane.showInputDialog("Enter name");
				if(string == null) return;
				t.name(string);
				name.setText(string);
			}
		});
		this.add(setName);
		this.add(name);
		
		final JLabel stats = new JLabel("attack:"+t.attack()+" strength:"+t.strength()+" defence:"+t.defence()+" delay:"+t.delay());
		
		JButton setStats = new JButton("Set Stats");
		setStats.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String string = JOptionPane.showInputDialog("Enter stats (attack,strength,defence,delay)");
				if(string == null) return;
				String[] arr = string.split(",");
				if(arr.length != 4) { return; }
				try {
					int a = Integer.parseInt(arr[0]);
					int s = Integer.parseInt(arr[1]);
					int d = Integer.parseInt(arr[2]);
					int l = Integer.parseInt(arr[3]);
					stats.setText("attack:"+t.attack(a)+" strength:"+t.strength(s)+" defence:"+t.defence(d)+" delay:"+t.delay(l));
				}
				catch (NumberFormatException e) {}
			}
		});
		this.add(setStats); // TODO: set stats
		this.add(stats);
		
		final JLabel radius = new JLabel(t.walkdistance()+"");
		
		JButton setRadius = new JButton("Set Radius");
		setRadius.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String string = JOptionPane.showInputDialog("Enter radius (squares)");
				if(string == null) return;
				try {
					int dist = Integer.parseInt(string);
					t.walkdistance(dist);
					radius.setText(dist+"");
				}
				catch (NumberFormatException e) {}
			}
		});
		this.add(setRadius);
		this.add(radius);
	}
}
