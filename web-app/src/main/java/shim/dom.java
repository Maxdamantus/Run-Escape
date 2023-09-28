package shim;

import java.util.ArrayList;

import org.teavm.jso.JSBody;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;

class dom {
	static class ShimDocument extends ShimNode<org.teavm.jso.dom.xml.Document> implements Document {
		ShimDocument(org.teavm.jso.dom.xml.Document node) {
			super(node);
		}

		@Override public Node adoptNode(Node arg0) throws DOMException { throw new UnsupportedOperationException(); }
		@Override public Attr createAttribute(String arg0) throws DOMException { throw new UnsupportedOperationException(); }
		@Override public Attr createAttributeNS(String arg0, String arg1) throws DOMException { throw new UnsupportedOperationException(); }
		@Override public CDATASection createCDATASection(String arg0) throws DOMException { throw new UnsupportedOperationException(); }
		@Override public Comment createComment(String arg0) { throw new UnsupportedOperationException(); }
		@Override public DocumentFragment createDocumentFragment() { throw new UnsupportedOperationException(); }
		@Override public Element createElement(String arg0) throws DOMException { throw new UnsupportedOperationException(); }
		@Override public Element createElementNS(String arg0, String arg1) throws DOMException { throw new UnsupportedOperationException(); }
		@Override public EntityReference createEntityReference(String arg0) throws DOMException { throw new UnsupportedOperationException(); }
		@Override public ProcessingInstruction createProcessingInstruction(String arg0, String arg1) throws DOMException { throw new UnsupportedOperationException(); }
		@Override public Text createTextNode(String arg0) { throw new UnsupportedOperationException(); }
		@Override public DocumentType getDoctype() { throw new UnsupportedOperationException(); }
		@Override public Element getDocumentElement() { throw new UnsupportedOperationException(); }
		@Override public String getDocumentURI() { throw new UnsupportedOperationException(); }
		@Override public DOMConfiguration getDomConfig() { throw new UnsupportedOperationException(); }
		@Override public Element getElementById(String arg0) { throw new UnsupportedOperationException(); }
		@Override public NodeList getElementsByTagName(String arg0) { throw new UnsupportedOperationException(); }
		@Override public NodeList getElementsByTagNameNS(String arg0, String arg1) { throw new UnsupportedOperationException(); }
		@Override public DOMImplementation getImplementation() { throw new UnsupportedOperationException(); }
		@Override public String getInputEncoding() { throw new UnsupportedOperationException(); }
		@Override public boolean getStrictErrorChecking() { throw new UnsupportedOperationException(); }
		@Override public String getXmlEncoding() { throw new UnsupportedOperationException(); }
		@Override public boolean getXmlStandalone() { throw new UnsupportedOperationException(); }
		@Override public String getXmlVersion() { throw new UnsupportedOperationException(); }
		@Override public Node importNode(Node arg0, boolean arg1) throws DOMException { throw new UnsupportedOperationException(); }
		@Override public void normalizeDocument() { throw new UnsupportedOperationException(); }
		@Override public Node renameNode(Node arg0, String arg1, String arg2) throws DOMException { throw new UnsupportedOperationException(); }
		@Override public void setDocumentURI(String arg0) { throw new UnsupportedOperationException(); }
		@Override public void setStrictErrorChecking(boolean arg0) { throw new UnsupportedOperationException(); }
		@Override public void setXmlStandalone(boolean arg0) throws DOMException { throw new UnsupportedOperationException(); }
		@Override public void setXmlVersion(String arg0) throws DOMException { throw new UnsupportedOperationException(); }
	}

	static class ShimNodeList implements NodeList {
		private final ArrayList<org.teavm.jso.dom.xml.Node> nodes = new ArrayList<>();

		@JSBody(params = { "e" }, script = "return e.textContent.trim() === \"\";")
		private native static boolean isTextContentEmpty(org.teavm.jso.dom.xml.Text e);

		private ShimNodeList(org.teavm.jso.dom.xml.NodeList<org.teavm.jso.dom.xml.Node> nodeList) {
			int len = nodeList.getLength();
			for (int x = 0; x < len; x++) {
				var node = nodeList.item(x);
				if (node.getNodeType() == org.teavm.jso.dom.xml.Node.TEXT_NODE) {
					var text = (org.teavm.jso.dom.xml.Text) node;
					// HACK: filter out empty text nodes to simulate the XPath filter that is stubbed out in the web shim
					if (isTextContentEmpty(text))
						continue;
				}
				nodes.add(node);
			}
		}

		@Override public int getLength() { return nodes.size(); }
		@Override public Node item(int arg0) { return newShimNode(nodes.get(arg0)); }
	}

	private static Node newShimNode(org.teavm.jso.dom.xml.Node node) {
		switch (node.getNodeType()) {
			case org.teavm.jso.dom.xml.Node.TEXT_NODE: return new ShimText((org.teavm.jso.dom.xml.Text) node);
			default: return new ShimNode<>(node);
		}
	}

	static class ShimText extends ShimNode<org.teavm.jso.dom.xml.Text> implements Text {
		private ShimText(org.teavm.jso.dom.xml.Text node) {
			super(node);
		}

		@Override public String getWholeText() { return shimNode().getTextContent(); }

		// NOTE: other methods are hopefully unused
		@Override public void appendData(String arg0) throws DOMException { throw new UnsupportedOperationException(); }
		@Override public void deleteData(int arg0, int arg1) throws DOMException { throw new UnsupportedOperationException(); }
		@Override public String getData() throws DOMException { throw new UnsupportedOperationException(); }
		@Override public int getLength() { throw new UnsupportedOperationException(); }
		@Override public void insertData(int arg0, String arg1) throws DOMException { throw new UnsupportedOperationException(); }
		@Override public void replaceData(int arg0, int arg1, String arg2) throws DOMException { throw new UnsupportedOperationException(); }
		@Override public void setData(String arg0) throws DOMException { throw new UnsupportedOperationException(); }
		@Override public String substringData(int arg0, int arg1) throws DOMException { throw new UnsupportedOperationException(); }
		@Override public boolean isElementContentWhitespace() { throw new UnsupportedOperationException(); }
		@Override public Text replaceWholeText(String arg0) throws DOMException { throw new UnsupportedOperationException(); }
		@Override public Text splitText(int arg0) throws DOMException { throw new UnsupportedOperationException(); }
	}

	static class ShimNode<T extends org.teavm.jso.dom.xml.Node> implements Node {
		private final T node;

		private ShimNode(T node) {
			this.node = node;
		}

		T shimNode() {
			return node;
		}

		@Override public NodeList getChildNodes() { return new ShimNodeList(node.getChildNodes()); }
		@Override public String getNodeName() { return node.getNodeName(); }

		// NOTE: other methods are hopefully unused
		@Override public Node appendChild(Node arg0) throws DOMException { throw new UnsupportedOperationException(); }
		@Override public Node cloneNode(boolean arg0) { throw new UnsupportedOperationException(); }
		@Override public short compareDocumentPosition(Node arg0) throws DOMException { throw new UnsupportedOperationException(); }
		@Override public NamedNodeMap getAttributes() { throw new UnsupportedOperationException(); }
		@Override public String getBaseURI() { throw new UnsupportedOperationException(); }
		@Override public Object getFeature(String arg0, String arg1) { throw new UnsupportedOperationException(); }
		@Override public Node getFirstChild() { throw new UnsupportedOperationException(); }
		@Override public Node getLastChild() { throw new UnsupportedOperationException(); }
		@Override public String getLocalName() { throw new UnsupportedOperationException(); }
		@Override public String getNamespaceURI() { throw new UnsupportedOperationException(); }
		@Override public Node getNextSibling() { throw new UnsupportedOperationException(); }
		@Override public short getNodeType() { throw new UnsupportedOperationException(); }
		@Override public String getNodeValue() throws DOMException { throw new UnsupportedOperationException(); }
		@Override public Document getOwnerDocument() { throw new UnsupportedOperationException(); }
		@Override public Node getParentNode() { throw new UnsupportedOperationException(); }
		@Override public String getPrefix() { throw new UnsupportedOperationException(); }
		@Override public Node getPreviousSibling() { throw new UnsupportedOperationException(); }
		@Override public String getTextContent() throws DOMException { throw new UnsupportedOperationException(); }
		@Override public Object getUserData(String arg0) { throw new UnsupportedOperationException(); }
		@Override public boolean hasAttributes() { throw new UnsupportedOperationException(); }
		@Override public boolean hasChildNodes() { throw new UnsupportedOperationException(); }
		@Override public Node insertBefore(Node arg0, Node arg1) throws DOMException { throw new UnsupportedOperationException(); }
		@Override public boolean isDefaultNamespace(String arg0) { throw new UnsupportedOperationException(); }
		@Override public boolean isEqualNode(Node arg0) { throw new UnsupportedOperationException(); }
		@Override public boolean isSameNode(Node arg0) { throw new UnsupportedOperationException(); }
		@Override public boolean isSupported(String arg0, String arg1) { throw new UnsupportedOperationException(); }
		@Override public String lookupNamespaceURI(String arg0) { throw new UnsupportedOperationException(); }
		@Override public String lookupPrefix(String arg0) { throw new UnsupportedOperationException(); }
		@Override public void normalize() { throw new UnsupportedOperationException(); }
		@Override public Node removeChild(Node arg0) throws DOMException { throw new UnsupportedOperationException(); }
		@Override public Node replaceChild(Node arg0, Node arg1) throws DOMException { throw new UnsupportedOperationException(); }
		@Override public void setNodeValue(String arg0) throws DOMException { throw new UnsupportedOperationException(); }
		@Override public void setPrefix(String arg0) throws DOMException { throw new UnsupportedOperationException(); }
		@Override public void setTextContent(String arg0) throws DOMException { throw new UnsupportedOperationException(); }
		@Override public Object setUserData(String arg0, Object arg1, UserDataHandler arg2) { throw new UnsupportedOperationException(); }
	}
}
