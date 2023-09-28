package shim;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.function.Consumer;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.canvas.CanvasImageSource;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.canvas.ImageData;
import org.teavm.jso.core.JSString;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLImageElement;
import org.teavm.jso.dom.xml.Node;

import shim.awt.datatransfer.Transferable;
import shim.awt.event.ComponentListener;
import shim.awt.event.KeyListener;
import shim.awt.event.MouseAdapter;
import shim.awt.event.WindowListener;
import shim.awt.image.CanvasBufferedImage;
import shim.awt.image.ImageObserver;

import static shim.Shim.document;

public class awt {
	private static <T> T TODO(String msg) {
		System.out.println("TODO: " + msg);
		return null;
	}

	public static class AlphaComposite implements Composite {
		public static final AlphaComposite SrcAtop = null; // TODO
	}
	public static class AWTEvent extends EventObject {
		public AWTEvent(Object source) {
			super(source);
			// TODO Auto-generated constructor stub
		}
	}
	public static class BorderLayout implements LayoutManager {
		public BorderLayout(int i, int j) {
			// TODO Auto-generated constructor stub
		}
		public BorderLayout() {
			// TODO Auto-generated constructor stub
		}
		public static final String SOUTH = "South";
		public static final String CENTER = "Center";
		public static final Object NORTH = "North";
	}
	public static class Color {
		private final int argb;
		private final JSObject shimStyle;

		public static final Color RED = new Color(0xFF0000);
		public static final Color GREEN = new Color(0x00FF00);
		public static final Color BLUE = new Color(0x0000FF);
		public static final Color WHITE = new Color(0xFFFFFF);
		public static final Color ORANGE = new Color(0xFFC800);
		public static final Color YELLOW = new Color(0xFFFF00);
		public static final Color BLACK = new Color(0x000000);

		public Color(int rgb, boolean hasAlpha) {
			// TODO: figure out why this is not needed in actual AWT; `IsoCanvas` uses `new Color(0, 0, 0, 0)`, which is presumably transparent
			if (rgb == 0 && hasAlpha)
				hasAlpha = false;
			if (!hasAlpha)
				rgb |= 0xFF000000;
			argb = rgb;
			shimStyle = JSString.valueOf("rgb(" + getRed() + ", " + getGreen() + ", " + getBlue() + ", " + getAlpha() + ")");
		}
		public Color(int rgb) { this(rgb, false); }
		public Color(int r, int g, int b) { this(r, g, b, 255); }
		public Color(int r, int g, int b, int a) { this((a << 24) | (r << 16) | (g << 8) | (b << 0), true); }

		public static Color getHSBColor(float h, float s, float b) {
			// TODO
			return YELLOW;
		}
		public static Color decode(String s) {
			return new Color(Integer.decode(s));
		}
		public int getRGB() {
			return argb;
		}
		public int getAlpha() {
			return argb >>> 24 & 0xFF;
		}
		public int getRed() {
			return argb >>> 16 & 0xFF;
		}
		public int getGreen() {
			return argb >>> 8 & 0xFF;
		}
		public int getBlue() {
			return argb >>> 0 & 0xFF;
		}
	}
	public static abstract class Component implements image.ImageObserver {
		private String name;
		private boolean isMouseDown = false, isMouseDragged = false;
		final HTMLElement element = shimNewElement();

		HTMLElement shimNewElement() {
			var element = document().createElement("div");
			element.setClassName("component");
			return element;
		}

		public void setDropTarget(dnd.DropTarget arg0) {
			// TODO
		}
		public void repaint() {
			// TODO
		}
		private void shimAddMouseEventListener(String domEventName, boolean focus, Consumer<event.MouseEvent> handler) {
			element.addEventListener(domEventName, evt -> {
				var domEvent = evt.<org.teavm.jso.dom.events.MouseEvent>cast();
				domEvent.preventDefault();
				domEvent.stopPropagation();
				if (focus)
					element.focus();
				var awtEvent = new event.MouseEvent(this, domEvent);
				handler.accept(awtEvent);
			});
		}
		public void addMouseListener(event.MouseListener l) {
			shimAddMouseEventListener("contextmenu", true, evt -> {});
			shimAddMouseEventListener("mouseenter", false, l::mouseEntered);
			shimAddMouseEventListener("mouseleave", false, l::mouseExited);
			shimAddMouseEventListener("mousedown", true, l::mousePressed);
			shimAddMouseEventListener("mouseup", false, evt -> {
				l.mouseReleased(evt);
				if (isMouseDown && !isMouseDragged)
					l.mouseClicked(evt);
			});
		}
		public void addMouseMotionListener(event.MouseMotionListener l) {
			shimAddMouseEventListener("mousedown", true, awtEvent -> {
				isMouseDown = true;
			});
			shimAddMouseEventListener("mouseup", false, awtEvent -> {
				isMouseDown = false;
				isMouseDragged = false;
			});
			shimAddMouseEventListener("mousemove", false, awtEvent -> {
				if (isMouseDown) {
					isMouseDragged = true;
					l.mouseDragged(awtEvent);
				} else
					l.mouseMoved(awtEvent);
			});
		}
		public void addKeyListener(KeyListener keyListener) {
			// TODO: don't attach to document
			document().addEventListener("keydown", evt -> {
				var domEvent = evt.<org.teavm.jso.dom.events.KeyboardEvent>cast();
				String key = domEvent.getKey();
				char c;
				if (key.length() == 1)
					c = key.charAt(0);
				else
					switch (key) {
						case "Enter": c = '\n'; break;
						case "Backspace": c = '\b'; break;
						default: return;
					}
				domEvent.preventDefault();
				domEvent.stopPropagation();
				var awtEvent = new event.KeyEvent(this, c);
				keyListener.keyTyped(awtEvent);
			});
		}
		public void setFocusable(boolean b) {
			// TODO Auto-generated method stub
		}
		public void addComponentListener(ComponentListener componentListener) {
			// TODO Auto-generated method stub
		}
		public Dimension getSize() {
			// TODO Auto-generated method stub
			return null;
		}
		public void setSize(int w, int h) {
			// TODO Auto-generated method stub
		}
		public int getWidth() {
			// TODO: think about how this should actually work? cache this?
			return element.getBoundingClientRect().getWidth();
		}
		public int getHeight() {
			// TODO: think about how this should actually work? cache this?
			return element.getBoundingClientRect().getHeight();
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
	}
	public static interface Composite {}
	public static class Container extends Component {
		public void setLayout(LayoutManager layoutManager) {
			// TODO Auto-generated method stub
		}

		public void add(Component component, Object constraints) {
			// TODO: constraints?
			element.appendChild(component.element);
		}

		public void add(Component component) {
			add(component, null);
		}
		public void doLayout() { /* TODO */ }
		public void validate() { /* TODO */ }

		public void removeAll() {
			element.setInnerHTML("");
		}

		public void remove(Component component) {
			element.removeChild(component.element);
		}

		public Component getComponentAt(Point point) {
			return TODO("Container.getComponentAt");
		}
	}
	public static class Dialog extends Window {
		public void setTitle(String title) { /* TODO */ }
	}
	public static class Dimension extends geom.Dimension2D {
		public int width, height;

		public Dimension(int w, int h) { width = w; height = h; }

		public double getWidth() {
			return width;
		}

		public double getHeight() {
			return height;
		}
	}
	public static class FlowLayout implements LayoutManager {
		public static final int RIGHT = 0;

		public FlowLayout(int align) {
			// TODO Auto-generated constructor stub
		}
	}
	public static class Frame extends Window {
		public void setTitle(String title) { /* TODO */ }
	}
	public static class Font {
		public Font(String name, int style, int size) {
			// TODO Auto-generated constructor stub
		}

		public static final int PLAIN = 0;
	}
	public static class FontMetrics {

		public int getHeight() {
			return 12;
		}

		public int stringWidth(String s) {
			// TODO
			return s.length()*8;
		}

		public int charWidth(char charAt) {
			// TODO
			return 8;
		}
	}
	public static abstract class Graphics {
		public abstract void setColor(Color color);
		public abstract void fillRect(int i, int j, int width, int height);
		public abstract void clearRect(int i, int j, int width, int height);
		public abstract void drawImage(Image img, int x, int y, Void observer);
		public abstract void drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, image.ImageObserver observer);
		public abstract void drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, image.ImageObserver observer);
		public abstract void drawImage(Image img, int x, int y, int width, int height, Color bgColor, image.ImageObserver void1);
		public abstract void drawImage(Image img, int i, int j, int width, int height, image.ImageObserver observer);
		public abstract void drawRect(int x, int i, int w, int j);
		public Graphics create(int x, int y, int width, int height) {
			Graphics g = create();
			g.translate(x, y);
			g.clipRect(0, 0, width, height);
			return g;
		}
		public abstract void clipRect(int i, int j, int width, int height);
		public abstract void translate(int x, int y);
		public abstract Graphics create();
		public Font getFont() {
			// TODO Auto-generated method stub
			return null;
		}
		public void setFont(Font font) {
			// TODO Auto-generated method stub
		}
		public FontMetrics getFontMetrics() {
			return new FontMetrics();
		}
		public abstract Shape getClip();
		public abstract void drawString(String s, int i, int y);
		public abstract void setClip(Shape clip);
		public abstract void drawChars(char[] data, int offset, int length, int x, int y);
	}
	public static abstract class Graphics2D extends Graphics {
		public void setRenderingHint(Void keyTextAntialiasing, Object valueTextAntialiasOn) {
			// TODO Auto-generated method stub
		}
		public abstract void setBackground(Color color);
		public Composite getComposite() {
			// TODO Auto-generated method stub
			return null;
		}
		public void setComposite(Composite composite) {
			// TODO Auto-generated method stub
		}
		@Override
		public abstract void setColor(Color color);
		@Override
		public abstract void fillRect(int i, int j, int width, int height);
		@Override
		public abstract void clearRect(int i, int j, int width, int height);

		void shimDebug(int x, int y, String text) {}
		void shimDrawImage(Image img, int x, int y) {}

		@Override
		public void drawImage(Image img, int x, int y, Void observer) {
			shimDrawImage(img, x, y);
		}

		@Override
		public void drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
			shimDrawImage(img, dx1, dy1);
		}
		@Override
		public void drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
			shimDrawImage(img, dx1, dy1);
		}
		@Override
		public void drawImage(Image img, int x, int y, int width, int height, Color bgColor, ImageObserver void1) {
			shimDrawImage(img, x, y);
		}
		@Override
		public void drawImage(Image img, int i, int j, int width, int height, ImageObserver observer) {
			shimDrawImage(img, i, j);
		}
		@Override
		public abstract void drawRect(int x, int i, int w, int j);
		@Override
		public void clipRect(int i, int j, int width, int height) {
			// TODO Auto-generated method stub
		}
		@Override
		public abstract void drawString(String text, int x, int y);
		@Override
		public Shape getClip() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public void setClip(Shape clip) {
			// TODO Auto-generated method stub
		}
		@Override
		public void drawChars(char[] data, int offset, int length, int x, int y) {
			drawString(new String(data, offset, length), x, y);
		}
	}
	public static class GridLayout implements LayoutManager {
		public GridLayout(int i, int j, int k, int l) {
			// TODO Auto-generated constructor stub
		}

		public GridLayout(int rows, int cols) {
			// TODO Auto-generated constructor stub
		}
	}
	public static abstract class Image {
		public abstract int getWidth(Void observer);
		public abstract int getHeight(Void observer);
	}
	public static interface LayoutManager {}
	public static class Point extends geom.Point2D {
		public int x, y;

		public Point(int x, int y) { this.x = x; this.y = y; }

		public Point() {}

		@Override
		public double getX() {
			return x;
		}

		@Override
		public double getY() {
			return y;
		}

		public void setLocation(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public void setLocation(double x, double y) {
			setLocation((int) x, (int) y);
		}
	}
	public static class RenderingHints {
		public static final Void KEY_TEXT_ANTIALIASING = null;
		public static final Object VALUE_TEXT_ANTIALIAS_ON = null;
	}
	public static interface Shape {}
	public static class Toolkit {
		public static Toolkit getDefaultToolkit() {
			return new Toolkit();
		}

		public Dimension getScreenSize() {
			var window = Shim.window();
			return new Dimension(window.getInnerWidth(), window.getInnerHeight());
		}
	}
	public static class Window extends Container {
		public void setBounds(int x, int y, int width, int height) { /* TODO */ }
		public void setAlwaysOnTop(boolean alwaysOnTop) { /* TODO */ }
		public void setLocationRelativeTo(Component component) { /* TODO */ }
		public void setVisible(boolean visible) { /* TODO */ }
		public void pack() { /* TODO */ }
		public void dispose() { /* TODO */ }
		public void setLocation(int x, int y) {
			// TODO Auto-generated method stub
		}
		public void setSize(int width, int height) {
			// TODO Auto-generated method stub
		}
		public void addWindowListener(WindowListener windowListener) {
			// TODO Auto-generated method stub
		}
		public void setMinimumSize(Dimension dimension) {
			// TODO Auto-generated method stub
		}
	}

	public static class event {
		public static class ActionEvent extends AWTEvent {
			public ActionEvent(Object source) {
				super(source);
				// TODO Auto-generated constructor stub
			}

			public String getActionCommand() {
				// TODO Auto-generated method stub
				return null;
			}
		}
		public static interface ActionListener {
			void actionPerformed(ActionEvent e);
		}
		public static class ComponentEvent extends AWTEvent {
			public ComponentEvent(Component source) {
				super(source);
				// TODO Auto-generated constructor stub
			}
		}
		public static interface ComponentListener {
			void componentHidden(ComponentEvent arg0);
			void componentMoved(ComponentEvent arg0);
			void componentResized(ComponentEvent event);
			void componentShown(ComponentEvent arg0);
		}
		public static class InputEvent extends ComponentEvent {
			public InputEvent(Component source) {
				super(source);
				// TODO Auto-generated constructor stub
			}
			public boolean isControlDown() { /* TODO */ return false; }
			public boolean isShiftDown() { /* TODO */ return false; }
		}
		public static class KeyEvent extends InputEvent {
			private final char keyChar;

			public KeyEvent(Component source, char keyChar) {
				super(source);
				this.keyChar = keyChar;
			}

			public char getKeyChar() {
				return keyChar;
			}
		}
		public static interface KeyListener {
			void keyPressed(KeyEvent arg0);
			void keyReleased(KeyEvent arg0);
			void keyTyped(KeyEvent arg0);
		}
		public static class MouseAdapter implements MouseListener {

			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}
		}
		public static class MouseEvent extends InputEvent {
			private final org.teavm.jso.dom.events.MouseEvent event;

			public MouseEvent(Component source, org.teavm.jso.dom.events.MouseEvent event) {
				super(source);
				this.event = event;
			}

			public static final int BUTTON1 = 1;
			public static final int BUTTON2 = 2;
			public static final int BUTTON3 = 3;

			public int getButton() {
				return event.getButton() + 1;
			}

			public Point getPoint() {
				return new Point(event.getClientX(), event.getClientY());
			}
		}
		public static interface MouseListener {
			void mouseClicked(MouseEvent arg0);
			void mouseEntered(MouseEvent arg0);
			void mouseExited(MouseEvent arg0);
			void mousePressed(MouseEvent arg0);
			void mouseReleased(MouseEvent arg0);
		}
		public static interface MouseMotionListener {
			public void mouseDragged(MouseEvent arg0);
			public void mouseMoved(MouseEvent arg0);
		}
		public static class WindowEvent {}
		public static interface WindowListener {
			void windowOpened(WindowEvent e);
			void windowClosing(WindowEvent e);
			void windowClosed(WindowEvent e);
			void windowIconified(WindowEvent e);
			void windowDeiconified(WindowEvent e);
			void windowActivated(WindowEvent e);
			void windowDeactivated(WindowEvent e);
		}
	}

	public static class geom {
		public static abstract class Dimension2D {}
		public static abstract class Point2D {
			public abstract double getX();
			public abstract double getY();
			public abstract void setLocation(double x, double y);
		}
	}

	private static class CanvasGraphics2D extends Graphics2D {
		private int translateX, translateY;
		private final CanvasRenderingContext2D ctx;
		private JSObject fg, bg;
		private final StyleState styleState;

		private static class StyleState {
			private JSObject ctxFillStyle, ctxStrokeStyle;
		}

		private CanvasGraphics2D(CanvasRenderingContext2D ctx, StyleState styleState, int translateX, int translateY) {
			this.ctx = ctx;
			this.styleState = styleState;
			this.translateX = translateX;
			this.translateY = translateY;
		}

		private CanvasGraphics2D(CanvasRenderingContext2D ctx) {
			this(ctx, new StyleState(), 0, 0);
		}

		@Override
		void shimDebug(int x, int y, String text) {
			ctx.fillText(text, translateX + x, translateY + y);
		}

		@Override
		void shimDrawImage(Image img_ , int x, int y) {
			if (!(img_ instanceof image.BufferedImage))
				throw new UnsupportedOperationException("Unknown image: " + img_);
			var img = (image.BufferedImage) img_;
			var source = img.source;
			if (source instanceof image.ImgBufferedImage) {
				var imgSource = (image.ImgBufferedImage) source;
				ctx.drawImage(imgSource.element, imgSource.x, imgSource.y, imgSource.width, imgSource.height, translateX + x, translateY + y, imgSource.width, imgSource.height);
			} else
				ctx.drawImage((CanvasImageSource) img.source.shimElement(), translateX + x, translateY + y);
		}

		@JSBody(params = { "ctx", "style" }, script = "ctx.fillStyle = style;")
		private static native void shimSetFillStyle(CanvasRenderingContext2D ctx, JSObject style);

		@JSBody(params = { "ctx", "style" }, script = "ctx.strokeStyle = style;")
		private static native void shimSetStrokeStyle(CanvasRenderingContext2D ctx, JSObject style);

		private void shimSetFillStyle(JSObject color) {
			if (!Shim.equals(styleState.ctxFillStyle, color))
				shimSetFillStyle(ctx, styleState.ctxFillStyle = color);
		}

		private void shimSetStrokeStyle(JSObject color) {
			if (!Shim.equals(styleState.ctxStrokeStyle, color))
				shimSetStrokeStyle(ctx, styleState.ctxStrokeStyle = color);
		}

		@Override
		public void setColor(Color color) {
			fg = color.shimStyle;
		}

		@Override
		public void setBackground(Color color) {
			bg = color.shimStyle;
		}

		@Override
		public void fillRect(int x, int y, int width, int height) {
			shimSetFillStyle(fg);
			ctx.fillRect(translateX + x, translateY + y, width, height);
		}

		@Override
		public void clearRect(int x, int y, int width, int height) {
			shimSetFillStyle(bg);
			ctx.fillRect(translateX + x, translateY + y, width, height);
		}

		@Override
		public void drawRect(int x, int y, int width, int height) {
			shimSetStrokeStyle(fg);
			ctx.strokeRect(translateX + x, translateY + y, width, height);
		}

		@Override
		public void drawString(String text, int x, int y) {
			shimSetFillStyle(fg);
			ctx.fillText(text, translateX + x, translateY + y);
		}

		@Override
		public void translate(int x, int y) {
			translateX += x;
			translateY += y;
		}

		@Override
		public Graphics create() {
			return new CanvasGraphics2D(ctx, styleState, translateX, translateY);
		}
	}

	static class HtmlGraphics2D extends Graphics2D {
		private ArrayList<image.BufferedImage> imagesOld = new ArrayList<>();
		private ArrayList<image.BufferedImage> imagesNew = new ArrayList<>();
		private final HTMLElement element;
		boolean scheduled;

		public HtmlGraphics2D(HTMLElement element) {
			this.element = element;
		}

		@Override
		void shimDrawImage(Image img_, int x, int y) {
			if (!(img_ instanceof image.BufferedImage))
				throw new UnsupportedOperationException("Unknown image: " + img_);
			var img = (image.BufferedImage) img_;
			// TODO: probably need more logic to copy <img> elements that are reused, and set width/height
			imagesNew.add(img);
		}

		void shimBeginDraw() {}

		void shimEndDraw() {
			for (var img : imagesOld)
				if (imagesNew.indexOf(img) < 0)
					element.removeChild(img.source.shimElement());
			for (var img : imagesNew) {
				if (imagesOld.indexOf(img) < 0)
					element.appendChild(img.source.shimElement());
				var source = img.source;
				if (source instanceof CanvasBufferedImage) {
					var canvasElement = ((CanvasBufferedImage) source).element;
					var rect = canvasElement.getBoundingClientRect();
					int oldWidth = canvasElement.getWidth();
					int oldHeight = canvasElement.getHeight();
					int newWidth = rect.getWidth();
					int newHeight = rect.getHeight();
					if (oldWidth != newWidth || oldHeight != newHeight) {
						canvasElement.setWidth(newWidth);
						canvasElement.setHeight(newHeight);
					}
				}
			}
			imagesOld.clear();
			var tmp = imagesNew;
			imagesNew = imagesOld;
			imagesOld = tmp;
		}

		@Override
		public void translate(int x, int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Graphics create() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBackground(Color color) {
			// TODO("HtmlGraphics2D.setBackground");
		}

		@Override
		public void setColor(Color color) {
			// TODO("HtmlGraphics2D.setColor");
		}

		@Override
		public void fillRect(int i, int j, int width, int height) {
			// TODO("HtmlGraphics2D.fillRect");
		}

		@Override
		public void clearRect(int i, int j, int width, int height) {
			TODO("HtmlGraphics2D.clearRect");
		}

		@Override
		public void drawRect(int x, int i, int w, int j) {
			TODO("HtmlGraphics2D.drawRect");
		}

		@Override
		public void drawString(String text, int x, int y) {
			TODO("HtmlGraphics2D.drawString");
		}
	}

	public static class image {
		public static class AffineTransformOp {
			public AffineTransformOp(AffineTransform tx, int type) {
				// TODO Auto-generated constructor stub
			}

			public static final int TYPE_NEAREST_NEIGHBOR = 1;

			public BufferedImage filter(BufferedImage src, BufferedImage dest) {
				// TODO Auto-generated method stub
				return null;
			}
		}
		public static interface ImageObserver {}
		private static abstract class BufferedImageSource {
			abstract HTMLElement shimElement();
			public abstract int getWidth();
			public abstract int getHeight();
			public abstract Graphics2D createGraphics();
			public abstract WritableRaster getAlphaRaster();
			public abstract BufferedImage getSubimage(int x, int y, int width, int height);
		}
		public static class BufferedImage extends Image {
			private final BufferedImageSource source;

			public static final int TYPE_INT_ARGB_PRE = 3;
			public static final int TYPE_INT_ARGB = 2;

			public static BufferedImage shimBufferedImage(HTMLImageElement image) {
				return new BufferedImage(new ImgBufferedImage(image));
			}

			private BufferedImage(BufferedImageSource source) {
				this.source = source;
			}

			public BufferedImage(int width, int height, int imageType) {
				source = new CanvasBufferedImage(width, height, imageType);
			}

			@Override public int getWidth(Void observer) { return source.getWidth(); }
			@Override public int getHeight(Void observer) { return source.getHeight(); }
			public int getWidth() { return source.getWidth(); }
			public int getHeight() { return source.getHeight(); }
			public Graphics2D createGraphics() { return source.createGraphics(); }
			public WritableRaster getAlphaRaster() { return source.getAlphaRaster(); }
			public BufferedImage getSubimage(int x, int y, int width, int height) { return source.getSubimage(x, y, width, height); }

			public Raster getData() {
				throw new UnsupportedOperationException();
			}
			public WritableRaster getRaster() {
				throw new UnsupportedOperationException();
			}
		}
		public static class CanvasBufferedImage extends BufferedImageSource {
			private static final JSObject CONTEXT_OPTIONS = shimContextOptions();
			private final HTMLCanvasElement element;
			private WritableRaster alphaRaster;

			public static final int TYPE_INT_ARGB_PRE = 3;
			public static final int TYPE_INT_ARGB = 2;

			private CanvasBufferedImage(int width, int height, int imageType) {
				element = (HTMLCanvasElement) document().createElement("canvas");
				if (imageType == BufferedImage.TYPE_INT_ARGB_PRE) {
					// used by `IsoCanvas`
					// NOTE: ignoring width and height, since the initial value is big and unnecessary; should be resized after it's drawn somewhere
					element.setClassName("resizable");
				} else {
					element.setWidth(width);
					element.setHeight(height);
				}
			}

			@JSBody(script = "return { alpha: false };")
			private static native JSObject shimContextOptions();

			@Override
			HTMLElement shimElement() {
				return element;
			}

			@Override
			public int getWidth() {
				return element.getWidth();
			}

			@Override
			public int getHeight() {
				return element.getHeight();
			}

			@Override
			public Graphics2D createGraphics() {
				return new CanvasGraphics2D((CanvasRenderingContext2D) element.getContext("2d", CONTEXT_OPTIONS));
			}

			@Override
			public WritableRaster getAlphaRaster() {
				if (alphaRaster != null)
					return alphaRaster;
				var ctx = (CanvasRenderingContext2D) element.getContext("2d");
				return alphaRaster = new AlphaRaster(ctx.getImageData(0,  0, getWidth(), getHeight()));
			}

			@Override
			public BufferedImage getSubimage(int x, int y, int width, int height) {
				throw new UnsupportedOperationException();
			}
		}
		private static class ImgBufferedImage extends BufferedImageSource {
			private final HTMLImageElement element;
			private final int x, y, width, height;
			private WritableRaster alphaRaster;

			private ImgBufferedImage(HTMLImageElement element) {
				this(element, 0, 0, element.getWidth(), element.getHeight());
			}

			private ImgBufferedImage(HTMLImageElement element, int x, int y, int width, int height) {
				this.element = element;
				this.x = x;
				this.y = y;
				this.width = width;
				this.height = height;
			}

			@Override
			HTMLElement shimElement() {
				return element;
			}

			@Override
			public int getWidth() {
				return width;
			}

			@Override
			public int getHeight() {
				return height;
			}

			@Override
			public Graphics2D createGraphics() {
				throw new UnsupportedOperationException();
			}

			@Override
			public WritableRaster getAlphaRaster() {
				if (alphaRaster != null)
					return alphaRaster;
				var canvasElement = (HTMLCanvasElement) document().createElement("canvas");
				canvasElement.setWidth(getWidth());
				canvasElement.setHeight(getHeight());
				var ctx = (CanvasRenderingContext2D) canvasElement.getContext("2d");
				ctx.drawImage(element, -x, -y);
				return alphaRaster = new AlphaRaster(ctx.getImageData(0,  0, getWidth(), getHeight()));
			}

			@Override
			public BufferedImage getSubimage(int x, int y, int width, int height) {
				return new BufferedImage(new ImgBufferedImage(element, this.x + x, this.y + y, width, height));
			}
		}
		public abstract static class Raster {
			public abstract int[] getPixel(int x, int y, int[] iArray);
			public abstract int getWidth();
			public abstract int getHeight();
		}
		public abstract static class WritableRaster extends Raster {
			public abstract void setPixel(int x, int y, int[] iArray);
		}
		private static class AlphaRaster extends WritableRaster {
			private final ImageData data;

			private AlphaRaster(ImageData data) {
				this.data = data;
			}

			@Override
			public void setPixel(int x, int y, int[] iArray) {
				throw new UnsupportedOperationException();
			}

			@Override
			public int[] getPixel(int x, int y, int[] iArray) {
				iArray[0] = data.getData().get((y*getWidth() + x)*4 + 3);
				return iArray;
			}

			@Override
			public int getWidth() {
				return data.getWidth();
			}

			@Override
			public int getHeight() {
				return data.getHeight();
			}
		}
	}

	public static class dnd {
		public static class DragSourceDragEvent {}
		public static interface DragSourceMotionListener {
			void dragMouseMoved(DragSourceDragEvent dsde);
		}
		public static class DropTarget {
			public DropTarget(Component c, DropTargetListener dt) {
				// TODO
			}
		}
		public static class DropTargetContext {
			public Component getComponent() {
				// TODO Auto-generated method stub
				return null;
			}
		}
		public static class DropTargetDragEvent {}
		public static class DropTargetDropEvent {

			public Transferable getTransferable() {
				// TODO Auto-generated method stub
				return null;
			}

			public DropTargetContext getDropTargetContext() {
				// TODO Auto-generated method stub
				return null;
			}

			public Point getLocation() {
				// TODO Auto-generated method stub
				return null;
			}

			public int getDropAction() {
				// TODO Auto-generated method stub
				return 0;
			}

			public void acceptDrop(int dropAction) {
				// TODO Auto-generated method stub
			}

			public void dropComplete(boolean b) {
				// TODO Auto-generated method stub
			}

			public void rejectDrop() {
				// TODO Auto-generated method stub
			}
		}
		public static class DropTargetEvent {}
		public static interface DropTargetListener {

			void dragEnter(DropTargetDragEvent arg0);

			void dragExit(DropTargetEvent arg0);

			void dragOver(DropTargetDragEvent arg0);

			void dropActionChanged(DropTargetDragEvent arg0);

			void drop(DropTargetDropEvent drag);}
	}

	public static class datatransfer {
		public static class DataFlavor {
			public DataFlavor(String string) throws ClassNotFoundException {
				// TODO Auto-generated constructor stub
			}

			public static final String javaJVMLocalObjectMimeType = "application/x-java-jvm-local-objectref";
		}
		public static interface Transferable {
			Object getTransferData(DataFlavor flavor) throws IOException;

			DataFlavor[] getTransferDataFlavors();

			boolean isDataFlavorSupported(DataFlavor flavor);
		}
		public static class UnsupportedFlavorException extends RuntimeException {}
	}
}
