package shim;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLSelectElement;
import org.teavm.jso.dom.xml.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import shim.awt.Component;
import shim.awt.Container;
import shim.awt.Dimension;
import shim.awt.Graphics;
import shim.awt.HtmlGraphics2D;
import shim.awt.datatransfer.Transferable;
import shim.awt.event.ActionListener;
import shim.awt.image.BufferedImage;
import shim.javax.swing.filechooser.FileFilter;
import shim.javax.xml.transform.dom.DOMSource;
import shim.javax.xml.transform.stream.StreamResult;
import static shim.Shim.document;

public class javax {
	private static <T> T TODO(String msg) {
		System.out.println("TODO: " + msg);
		return null;
	}

	public static class swing {
		public static abstract class AbstractButton extends JComponent {
			final ArrayList<ActionListener> actionListeners = new ArrayList<>();

			@Override
			HTMLElement shimNewElement() {
				var element = document().createElement("button");
				element.addEventListener("click", evt -> {
					evt.stopPropagation();
					evt.preventDefault();
					for (var actionListener : actionListeners)
						actionListener.actionPerformed(new awt.event.ActionEvent(this));
				});
				return element;
			}

			public void addActionListener(ActionListener actionListener) {
				actionListeners.add(actionListener);
			}

			public void setActionCommand(String string) {
				TODO("AbstractButton.setActionCommand");
			}
			public boolean isSelected() {
				TODO("AbstractButton.isSelected");
				return true;
			}
			public void setSelected(boolean selected) {
				TODO("AbstractButton.setSelected");
			}
			public String getText() {
				return TODO("AbstractButton.getText");
			}
		}
		public static class Box extends JComponent {
			public static Component createVerticalGlue() {
				return TODO("Box.createVirtualGlue");
			}
		}
		public static class BoxLayout implements awt.LayoutManager {
			public static final int Y_AXIS = 1;
			public BoxLayout(Container target, int axis) {
				TODO("BoxLayout");
			}
		}
		public static class ButtonGroup {
			public void add(AbstractButton b) {
				TODO("ButtonGroup.add");
			}
		}
		public static class JButton extends AbstractButton {
			public JButton(String string) {
				TODO("JButton");
			}
		}
		public static class JComboBox<E> extends JComponent {
			public void addItem(E item) {
				TODO("JComboxBox.addItem");
			}

			public void setSelectedItem(E item) {
				TODO("JComboxBox.setSelectedItem");
			}

			public void addActionListener(ActionListener actionListener) {
				TODO("JComboxBox.addActionListener");
			}

			public E getSelectedItem() {
				return TODO("JComboxBox.getSelectedItem");
			}
		}
		public static abstract class JComponent extends awt.Container {
			private HtmlGraphics2D htmlGraphics;

			public void paint(Graphics jgraphics) {
				TODO("JComponent.paint");
			}

			@Override
			public void repaint() {
				var g_ = htmlGraphics;
				if (g_ == null)
					g_ = htmlGraphics = new HtmlGraphics2D(element);
				var g = g_;
				if (g.scheduled)
					return;
				g.scheduled = true;
				Shim.requestAnimationFrame(time -> {
					g.scheduled = false;
					g.shimBeginDraw();
					try {
						paint(g);
					} finally {
						g.shimEndDraw();
					}
				});
			}

			public void setToolTipText(String description) {
				TODO("JComponent.setToolTipText");
			}

			public TransferHandler getTransferHandler() {
				return TODO("JComponent.getTransferHandler");
			}

			public void setTransferHandler(TransferHandler transferHandler) {
				TODO("JComponent.setTransferHandler");
			}

			public void setPreferredSize(Dimension dimension) {
				TODO("JComponent.setPreferredSize");
			}
		}
		public static class JDialog extends awt.Dialog implements WindowConstants {
			public awt.Container getContentPane() { return TODO("JDialog.getContentPane"); }

			public JRootPane getRootPane() {
				return TODO("JDialog.getRootPane");
			}

			public void setDefaultCloseOperation(int operation) {
				TODO("JDialog.setDefaultCloseOperation");
			}

			public void setSize(Dimension size) {
				TODO("JDialog.setSize");
			}
		}
		public static class JFileChooser extends JComponent {
			public static final int APPROVE_OPTION = 0;

			public void setFileFilter(FileFilter fileFilter) {
				TODO("JFileChooser.setFileFilter");
			}

			public int showOpenDialog(awt.Component parent) {
				TODO("JFileChooser.showOpenDialog");
				return 0;
			}

			public int showSaveDialog(awt.Component parent) {
				TODO("JFileChooser.showSaveDialog");
				return 0;
			}

			public File getSelectedFile() {
				return TODO("JFileChooser.getSelectedFile");
			}
		}
		public static class JFrame extends awt.Frame {
			public static final int EXIT_ON_CLOSE = 3;

			public JFrame(String name) {
				TODO("JFrame");
			}

			public JFrame() {
				TODO("JFrame");
			}

			public void setJMenuBar(JMenuBar bar) {
				TODO("JFrame.setJMenuBar");
			}

			public Container getContentPane() {
				return TODO("JFrame.getContentPane");
			}

			public void setDefaultCloseOperation(int closeOperation) {
				TODO("JFrame.setDefaultCloseOperation");
			}

			@Override
			public void setVisible(boolean visible) {
				var parent = element.getParentNode();
				if (parent == null && visible)
					document().getBody().appendChild(element);
				else if (parent != null && !visible)
					parent.removeChild(element);
			}
		}
		public static class JLabel extends JComponent {
			public JLabel(String string) {
				TODO("JLabel");
			}

			public void setText(String string) {
				TODO("JLabel.setText " + string);
			}
		}
		public static class JMenu extends JMenuItem {
			public JMenu(String s) {
				super(s);
				TODO("JMenu");
			}
		}
		public static class JMenuBar extends JComponent {}
		public static class JMenuItem extends AbstractButton {
			public JMenuItem(String text) {
				element.setTextContent(text);
				element.setAttribute("value", text);
			}

			@Override
			public String getText() {
				return element.getTextContent();
			}


			public void setEnabled(boolean enabled) {
				if (enabled)
					element.removeAttribute("disabled");
				else
					element.setAttribute("disabled", "");
			}
		}
		public static class JOptionPane extends JComponent {
			public static final int PLAIN_MESSAGE = -1;
			public static final int OK_CANCEL_OPTION = 2;
			public static final int CANCEL_OPTION = 2;
			public static final int OK_OPTION = 0;

			public static String showInputDialog(String string) {
				TODO("JOptionPane.showInputDialog " + string);
				return "";
			}

			public static void showMessageDialog(awt.Component component, Object message) {
				TODO("JOptionPane.showMessageDialog " + message);
			}

			public static String showInputDialog(awt.Component parentComponent, Object message, String title, int messageType, Void icon, Object[] selectionValues, Object initialSelectionValue) {
				TODO("JOptionPane.showInputDialog " + message);
				return null;
			}

			public static int showConfirmDialog(awt.Component parentComponent, Object message, String title, int optionType, int messageType) {
				TODO("JOptionPane.showConfirmDialog " + message);
				return 0;
			}

			public static int showConfirmDialog(awt.Component parentComponent, String message) {
				TODO("JOptionPane.showConfirmDialog " + message);
				return 0;
			}
		}
		public static class JPanel extends JComponent {}
		public static class JPopupMenu extends JComponent {
			private final ArrayList<JMenuItem> children = new ArrayList<>();

			@Override
			HTMLElement shimNewElement() {
				var element = document().createElement("div");
				element.setClassName("popup-menu");
				return element;
			}

			private void remove() {
				var e = element;
				for (int x = 0; x < 2; x++) {
					var p = (HTMLElement) e.getParentNode();
					if (p == null)
						break;
					p.removeChild(e);
					e = p;
				}
			}

			public void show(Container invoker, int x, int y) {
				var panel = document().createElement("div");
				panel.setClassName("modal-panel");
				panel.appendChild(element);
				panel.addEventListener("mousedown", evt -> {
					if (evt.getTarget() == evt.getCurrentTarget())
						remove();
				});
				document().getBody().appendChild(panel);
				element.focus();
				// TODO: make it relative to component?
				element.getStyle().setProperty("left", x + "px");
				element.getStyle().setProperty("top", y + "px");
			}

			@Override
			public void add(Component component, Object constraints) {
				super.add(component, constraints);
				if (component instanceof JMenuItem)
					((JMenuItem) component).addActionListener(e -> {
						remove();
					});
			}
		}
		public static class JRadioButton extends JToggleButton {
			public JRadioButton(String string) {
				TODO("JRadioButton " + string);
			}
		}
		public static class JRootPane extends JComponent {
			public void setDefaultButton(JButton okButton) {
				TODO("JRootPane.setDefaultButton");
			}
		}
		public static class JScrollPane extends JComponent {
			public void setViewportView(awt.Component component) {
				TODO("JScrollPane.setViewportView");
			}
		}
		public static class JTextArea extends JTextComponent {}
		public static class JTextComponent extends JComponent {
			public void setEditable(boolean editable) { /* TODO */ }
			public void setText(String text) { /* TODO */ }
			public String getText() {
				return TODO("JTextComponent.getText");
			}
		}
		public static class JTextField extends JTextComponent {
			public void addActionListener(ActionListener actionListener) {
				TODO("JTextField.addActionListener");
			}
		}
		public static class JToggleButton extends AbstractButton {}
		public static class TransferHandler {
			public static final int COPY = 1;
			public static final int NONE = 0;

			public void exportAsDrag(JComponent c, awt.event.InputEvent e, int copy2) {
				TODO("TransferHandler.exportAsDrag");
			}

			protected Transferable createTransferable(JComponent c) {
				return TODO("TransferHandler.createTransferable");
			}

			public int getSourceActions(JComponent c) {
				TODO("TransferHandler.getSourceActions");
				return 0;
			}
		}
		public static interface WindowConstants {
			public static final int DO_NOTHING_ON_CLOSE = 0;
		}

		public static class filechooser {
			public static interface FileFilter {
				public boolean accept(File arg0);
				public String getDescription();
			}
		}
	}

	public static class imageio {
		public static class ImageIO {
			public static BufferedImage read(URL url) {
				return TODO("ImageIO.read " + url);
			}
		}
	}

	public static class xml {
		public static class parsers {
			public static class DocumentBuilder {
				public Document newDocument() {
					return TODO("Document.newDocument");
				}

				public Document parse(InputSource is) throws SAXException, IOException {
					String xml = Shim.readAllCharsToString(is.getCharacterStream());
					return new dom.ShimDocument(DOMParser.create().parseFromString(xml, "text/xml"));
				}
			}
			public static class DocumentBuilderFactory {
				public static DocumentBuilderFactory newInstance() {
					return new DocumentBuilderFactory();
				}

				public DocumentBuilder newDocumentBuilder() {
					return new DocumentBuilder();
				}
			}
			public static class ParserConfigurationException extends RuntimeException {}
		}

		public static class transform {
			public static class OutputKeys {
				public static final String INDENT = "indent";
			}
			public static class Transformer {
				public void setOutputProperty(String indent, String value) {
					TODO("Transformer.setOutputProperty");
				}

				public void transform(DOMSource domSource, StreamResult streamResult) {
					TODO("Transformer.transform");
				}
			}
			public static class TransformerConfigurationException extends RuntimeException {}
			public static class TransformerException extends RuntimeException {}
			public static class TransformerFactory {
				public static TransformerFactory newInstance() {
					return TODO("TransformerFactory.newInstance");
				}

				public void setAttribute(String name, Object value) {
					TODO("TransformerFactory.setAttribute");
				}

				public Transformer newTransformer() {
					return TODO("TransformerFactory.newTransformer");
				}
			}
			public static class TransformerFactoryConfigurationError extends RuntimeException {}

			public static class dom {
				public static class DOMSource {
					public DOMSource(Node doc) {
						TODO("DOMSource");
					}
				}
			}

			public static class stream {
				public static class StreamResult {
					public StreamResult(StringWriter sw) {
						TODO("StreamResult");
					}
				}
			}
		}

		public static class xpath {
			public static class XPathConstants {
				public static final Void NODESET = null;
			}
			public static class XPath {
				public XPathExpression compile(String string) {
					return new XPathExpression();
				}
			}
			public static class XPathExpression {
				public NodeList evaluate(Node node, Void nodeset) {
					return new NodeList() {
						@Override public int getLength() { return 0; }
						@Override public Node item(int arg0) { throw new IndexOutOfBoundsException(); }
					};
				}
			}
			public static class XPathExpressionException extends RuntimeException {}
			public static class XPathFactory {
				public static XPathFactory newInstance() {
					return new XPathFactory();
				}

				public XPath newXPath() {
					return new XPath();
				}
			}
		}
	}
}
