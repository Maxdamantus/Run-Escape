package util;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Shape;

public class TextHelper {
	public static void drawStringScrolling(Graphics g, String s, int x, int y, int width) {
		FontMetrics fm = g.getFontMetrics();
		
		int actualLength = fm.stringWidth(s);
		
		if(actualLength > width) {
			Shape clip = g.getClip();
			g.clipRect(x, y-100, width, 200);
			g.drawString(s, x-(actualLength-width), y);
			g.setClip(clip);
		}
		else {
			g.drawString(s, x, y);
		}
	}
}
