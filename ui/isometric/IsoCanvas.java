package ui.isometric;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;


/**
 * 
 * This canvas will render a section of the world, using the given datasource.
 * 
 * @author melby
 *
 */
public class IsoCanvas extends Canvas {
	private static final long serialVersionUID = 1L;
	
	private static final int tileX = 64;
	private static final int tileY = 44;
	
	private IsoDataSource dataSource;
	
	/**
	 * Create a new IsoCanvas with a given datasource
	 * @param dataSource
	 */
	public IsoCanvas(IsoDataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		int rowY = tileY/2;
		int tileCountY = this.getHeight()/rowY+3;
		int tileCountX = this.getWidth()/tileX+2;
		int row = 0;
		
		int y = rowY;
		for(;y<tileCountY*rowY;y+=rowY) {
			int xg = (row%2 == 0)?row/2-1:row/2;
			int yg = (row%2 == 0)?row/2:(row-1)/2;
			int x = (row%2 == 0)?tileX/2:0;
			for(;x<tileCountX*tileX;x+=tileX) {
				this.drawSquareAt(g, x, y, xg, yg);
				xg--;
				yg++;
			}
			row++;
		}
	}
	
	private void drawSquareAt(Graphics g, int dx, int dy, int sx, int sy) {
		IsoSquare square = dataSource.squareAt(sx, sy);
		
		for(IsoImage i : square) {
			g.drawImage(i.image(), dx-i.width()/2, dy-i.height(), i.width(), i.height(), null, this);
		}
	}
}
