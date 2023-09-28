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
			g.clipRect(x, y-fm.getHeight()-10, width, fm.getHeight()+20);
			g.drawString(s, x-(actualLength-width), y);
			g.setClip(clip);
		}
		else {
			g.drawString(s, x, y);
		}
	}

	public static int drawStringMultiLineUp(Graphics g, String s, int x, int y, int spacing, int width) { // TODO: faster, draw lots of chars at once
		FontMetrics fm = g.getFontMetrics();

		int lineHeight = fm.getHeight();

        int curX = x;
        
        int height = 0;
        
        for(int n = 0; n < s.length(); n++) {
        	curX += fm.charWidth(s.charAt(n));
        	if(curX >= x + width && n != s.length()) {
        		curX = x;
        		height += lineHeight + spacing;
        	}
        }
        
        curX = x;
        int curY = y-height;
        char[] tmp = new char[1];
        
        for(int n = 0; n < s.length(); n++) {
        	tmp[0] = s.charAt(n);
        	g.drawChars(tmp, 0, 1, curX, curY);
        	
        	curX += fm.charWidth(s.charAt(n));
        	if(curX >= x + width && n != s.length()) {
        		curX = x;
        		curY += lineHeight + spacing;
        	}
        }
        
        return height + lineHeight;
	}
}
