package util;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

/**
 * GUI utils
 * @author ruarusmelb
 *
 */
public class GUI {
	/**
	 * Center the given window on the main screen
	 * @param frame
	 */
	public static void centerWindow(Window frame) {
	    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
	    int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
	    frame.setLocation(x, y);
	}
}
