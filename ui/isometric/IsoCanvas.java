package ui.isometric;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import util.Direction;


/**
 * 
 * This canvas will render a section of the world, using the given datasource.
 * 
 * @author melby
 *
 */
public class IsoCanvas extends Canvas implements KeyListener {
	private static final long serialVersionUID = 1L;
	
	public static final int TILE_X = 64;
	public static final int TILE_Y = 44;
	
	private IsoDataSource dataSource;
	
	private Direction viewDirection = Direction.NORTH;
	
	/**
	 * Create a new IsoCanvas with a given datasource
	 * @param dataSource
	 */
	public IsoCanvas(IsoDataSource dataSource) {
		this.dataSource = dataSource;
		this.addKeyListener(this);
	}
	
	@Override
	public void paint(Graphics g) {
		dataSource.setViewableRect(0, 0, this.getWidth(), this.getHeight(), viewDirection);
		dataSource.update();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight()); // TODO: doesn't work?????
		
		int rowY = TILE_Y/2;
		int tileCountY = this.getHeight()/rowY+3;
		int tileCountX = this.getWidth()/TILE_X+2;
		int row = 0;
		
		int y = rowY;
		for(;y<tileCountY*rowY;y+=rowY) {
			int yg = -((row%2 == 0)?row/2-1:row/2);
			int xg = (row%2 == 0)?row/2:(row-1)/2;
			int x = (row%2 == 0)?TILE_X/2:0;
			for(;x<tileCountX*TILE_X;x+=TILE_X) {
				this.drawSquareAt(g, x, y, xg, yg);
				yg++;
				xg++;
			}
			row++;
		}
	}
	
	/**
	 * Draw a square at a given location
	 * @param g
	 * @param dx
	 * @param dy
	 * @param sx
	 * @param sy
	 */
	private void drawSquareAt(Graphics g, int dx, int dy, int sx, int sy) {
		IsoSquare square = dataSource.squareAt(sx, sy);
		
		for(IsoImage i : square) {
			g.drawImage(i.image(), dx-i.width()/2, dy-i.height(), i.width(), i.height(), null, this);
		}
		
		g.setColor(Color.WHITE);
		g.drawString(sx + "x" + sy, dx, dy);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		if(arg0.getKeyChar() == 'r') {
			viewDirection = viewDirection.compose(Direction.EAST);
			this.repaint();
		}
	}
}
