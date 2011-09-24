package ui.isometric;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Set;

import util.Direction;


/**
 * 
 * This canvas will render a section of the world, using the given datasource.
 * 
 * @author melby
 *
 */
public class IsoCanvas extends Canvas implements KeyListener, MouseMotionListener, MouseListener {
	private static final long serialVersionUID = 1L;
	
	public static final int TILE_X = 64;
	public static final int TILE_Y = 44;
	
	private IsoDataSource dataSource;
	
	private Direction viewDirection = Direction.NORTH;
	
	private Point mouse;
	private Point origin = new Point(0, 0);
	
	private boolean selectionRender = false;
	private IsoImage selectedImage = null;
	private Point selectionPoint = new Point(0, 0);
	
	private static final double fps = 10;
		
	public interface SelectionCallback {
		public void imageSelected(IsoImage image, MouseEvent event);
	}
	
	private Set<SelectionCallback> selectionCallback = new HashSet<SelectionCallback>();
	
	/**
	 * Create a new IsoCanvas with a given interface and datasource
	 * @param inter
	 * @param dataSource
	 */
	public IsoCanvas(IsoDataSource dataSource) {
		this.dataSource = dataSource;
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					repaint();
					try {
						Thread.sleep((long) (1000/fps));
					} catch (InterruptedException e) { }
				}
			}
		}).start();
	}
	
	@Override
	public void paint(Graphics g) {
		dataSource.setViewableRect((int)origin.getX(), (int)origin.getY(), this.getWidth(), this.getHeight(), viewDirection);
		dataSource.update();
		
		Point smoothing = dataSource.transform().smoothOrigin(origin);
		
		if(!selectionRender) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		
		int rowY = TILE_Y/2;
		int tileCountY = this.getHeight()/rowY+3;
		int tileCountX = this.getWidth()/TILE_X+2;
		int row = 0;
		
		int y = rowY;
		for(;y<tileCountY*rowY;y+=rowY) {
			if(!selectionRender || selectionPoint.y < y+smoothing.getY()) {
				int yg = -((row%2 == 0)?row/2-1:row/2);
				int xg = (row%2 == 0)?row/2:(row-1)/2;
				int x = (row%2 == 0)?TILE_X/2:0;
				for(;x<tileCountX*TILE_X;x+=TILE_X) {
					this.drawSquareAt(g, (int)(x+smoothing.getX()), (int)(y+smoothing.getY()), xg, yg);
					yg++;
					xg++;
				}
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
			if(!selectionRender) {
				g.drawImage(i.image(), dx-i.width()/2, dy-i.height(), i.width(), i.height(), null, this);
			}
			else {
				if(selectionPoint.x > dx-i.width()/2 && selectionPoint.x < dx+i.width()/2) { // Check x
					if(selectionPoint.y > dy-i.height() && selectionPoint.y < dy) { // Check y
						int x = selectionPoint.x - dx + i.width()/2;
						int y = selectionPoint.y - dy + i.height();
						
						int[] pixels = new int[4];
						i.image().getAlphaRaster().getPixel(x, y, pixels);
						
						if(pixels[0] > 0) {
							selectedImage = i;
						}
					}
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		if(arg0.getKeyChar() == 'r') {
			viewDirection = viewDirection.compose(Direction.EAST);
			this.repaint();
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		Point delta = new Point(arg0.getPoint().x-mouse.x, arg0.getPoint().y-mouse.y);
		delta = dataSource.transform().transformRelitivePoint(delta);
		mouse = arg0.getPoint();
		
		origin.setLocation(origin.getX()-delta.getX(), origin.getY()+delta.getY());
		this.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		final IsoImage i = this.getImageAtPoint(arg0.getPoint());
		
		for(SelectionCallback s : selectionCallback) {
			s.imageSelected(i, arg0);
		}
	}

	/**
	 * Search for the image at a given point in the canvas
	 * @param point
	 * @return
	 */
	private IsoImage getImageAtPoint(Point point) {
		selectedImage = null;
		
		selectionRender = true;
		selectionPoint = point;
		this.paint(null);
		selectionRender = false;
		
		return selectedImage;
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		mouse = arg0.getPoint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
	
	public void addSelectionCallback(SelectionCallback s) {
		selectionCallback.add(s);
	}
	
	public void removeSelectionCallback(SelectionCallback s) {
		selectionCallback.remove(s);
	}
}
