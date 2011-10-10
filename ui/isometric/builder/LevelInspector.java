package ui.isometric.builder;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import game.GameWorld;
import game.Level;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * An ugly LevelInspector, allows you to set luminance on levels
 * @author melby
 *
 */
public class LevelInspector extends JFrame { // TODO: horrible horrible horrible
	private static final long serialVersionUID = 1L;
	private GameWorld world;
	
	/**
	 * An internal panel for inspecting one level
	 * @author melby
	 *
	 */
	private class LevelPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		public LevelPanel(final Level l) {
			this.add(new JLabel("level "+l.z()));
			this.add(new JLabel("luminance "+l.luminance()));
			final LevelPanel t = this;
			final JTextField r = new JTextField();
			r.setPreferredSize(new Dimension(90, 20));
			r.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						int radius = Integer.parseInt(r.getText());
						String radiusS = ((radius==-1)?"infinity":(radius+""));
						if(JOptionPane.showConfirmDialog(t, "Are you sure you wish to set the level "+l.z()+" to radius "+radiusS+"?") == JOptionPane.OK_OPTION) {
							l.luminance(radius);
						}
					}
					catch (NumberFormatException e) {}
				}
			});
			this.add(r);
		}
	}
	
	/**
	 * Create a LevelInspector with a given world
	 * @param w
	 */
	public LevelInspector(final GameWorld w) {
		super("Set Lightness (-1 = infinity)");
		
		this.world = w;
		this.getContentPane().setLayout(new FlowLayout());
		
		refresh();
	}

	/**
	 * Refresh the inspector
	 */
	private void refresh() {
		this.getContentPane().removeAll();
		
		for(Integer i : world.levels()) {
			this.getContentPane().add(new LevelPanel(world.level(i)));
		}
		
		JButton update = new JButton("Update list");
		update.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				refresh();
			}
		});
		this.add(update);
		
		this.getContentPane().validate();
		this.getContentPane().repaint();
	}
}
