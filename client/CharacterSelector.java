package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JRadioButton;
import java.awt.GridLayout;
import java.util.Set;

import javax.swing.JScrollPane;

import ui.isometric.libraries.IsoCharacterImageLibrary;
import util.ImagePanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CharacterSelector extends JDialog {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			CharacterSelector dialog = new CharacterSelector();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public CharacterSelector() {
		String[] charNames = IsoCharacterImageLibrary.getAllCharacters().toArray(new String[0]);
		if (charNames.length < 8) {
			int i = 0;
			String[] replacementArray = new String[8];
			while (i < charNames.length) {
				replacementArray[i] = charNames[i++];
			}
			while (i < 8) {
				replacementArray[i++] = charNames[0];
			}
			charNames = replacementArray;
			
		}
		charNames.toString();
		
		
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			getContentPane().add(scrollPane, BorderLayout.CENTER);
			{
				JPanel scrolledPanel = new JPanel();
				scrollPane.setViewportView(scrolledPanel);
				scrolledPanel.setLayout(new GridLayout(0, 8, 0, 0));
				{
					JPanel panel_1 = new JPanel();
					scrolledPanel.add(panel_1);
					panel_1.setLayout(new BorderLayout(0, 0));
					{
						JRadioButton rdbtnNewRadioButton = new JRadioButton("");
						panel_1.add(rdbtnNewRadioButton, BorderLayout.SOUTH);
					}
					{
						ImagePanel charPanel1 = new ImagePanel(IsoCharacterImageLibrary.imageForCharacterName(charNames[0]));
						panel_1.add(charPanel1, BorderLayout.CENTER);
					}
				}
				{
					JPanel panel_2 = new JPanel();
					scrolledPanel.add(panel_2);
					panel_2.setLayout(new BorderLayout(0, 0));
					{
						JRadioButton radioButton = new JRadioButton("");
						panel_2.add(radioButton, BorderLayout.SOUTH);
					}
					{
						ImagePanel charPanel2 = new ImagePanel(IsoCharacterImageLibrary.imageForCharacterName(charNames[1]));
						panel_2.add(charPanel2, BorderLayout.CENTER);
					}
				}
				{
					JPanel panel_3 = new JPanel();
					scrolledPanel.add(panel_3);
					panel_3.setLayout(new BorderLayout(0, 0));
					{
						JRadioButton radioButton = new JRadioButton("");
						panel_3.add(radioButton, BorderLayout.SOUTH);
					}
					{
						ImagePanel charPanel3 = new ImagePanel(IsoCharacterImageLibrary.imageForCharacterName(charNames[2]));
						panel_3.add(charPanel3, BorderLayout.CENTER);
					}
				}
				{
					JPanel panel_4 = new JPanel();
					scrolledPanel.add(panel_4);
					panel_4.setLayout(new BorderLayout(0, 0));
					{
						JRadioButton radioButton = new JRadioButton("");
						panel_4.add(radioButton, BorderLayout.SOUTH);
					}
					{
						ImagePanel charPanel4 = new ImagePanel(IsoCharacterImageLibrary.imageForCharacterName(charNames[3]));
						panel_4.add(charPanel4, BorderLayout.CENTER);
					}
				}
				{
					JPanel panel_5 = new JPanel();
					scrolledPanel.add(panel_5);
					panel_5.setLayout(new BorderLayout(0, 0));
					{
						JRadioButton radioButton = new JRadioButton("");
						panel_5.add(radioButton, BorderLayout.SOUTH);
					}
					{
						ImagePanel charPanel5 = new ImagePanel(IsoCharacterImageLibrary.imageForCharacterName(charNames[4]));
						panel_5.add(charPanel5, BorderLayout.CENTER);
					}
				}
				{
					JPanel panel_6 = new JPanel();
					scrolledPanel.add(panel_6);
					panel_6.setLayout(new BorderLayout(0, 0));
					{
						JRadioButton radioButton = new JRadioButton("");
						panel_6.add(radioButton, BorderLayout.SOUTH);
					}
					{
						ImagePanel charPanel6 = new ImagePanel(IsoCharacterImageLibrary.imageForCharacterName(charNames[5]));
						panel_6.add(charPanel6, BorderLayout.CENTER);
					}
				}
				{
					JPanel panel_7 = new JPanel();
					scrolledPanel.add(panel_7);
					panel_7.setLayout(new BorderLayout(0, 0));
					{
						JRadioButton radioButton = new JRadioButton("");
						panel_7.add(radioButton, BorderLayout.SOUTH);
					}
					{
						ImagePanel charPanel7 = new ImagePanel(IsoCharacterImageLibrary.imageForCharacterName(charNames[6]));
						panel_7.add(charPanel7, BorderLayout.CENTER);
					}
				}
				{
					JPanel panel_8 = new JPanel();
					scrolledPanel.add(panel_8);
					panel_8.setLayout(new BorderLayout(0, 0));
					{
						JRadioButton radioButton = new JRadioButton("");
						panel_8.add(radioButton, BorderLayout.SOUTH);
					}
					{
						ImagePanel charPanel8 = new ImagePanel(IsoCharacterImageLibrary.imageForCharacterName(charNames[7]));
						panel_8.add(charPanel8, BorderLayout.CENTER);
					}
				}
			}
		}
	}
}
