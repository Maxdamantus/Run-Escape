package ui.isometric;
import javax.swing.JFrame;


public class UITestMain {
	public static void main(String[] args) {
		JFrame frame = new JFrame("IsoTest");
		IsoDataSource d = new IsoTestDataSource();
		IsoCanvas canvas = new IsoCanvas(d);
		canvas.setSize(300, 300);
		canvas.repaint();
		frame.add(canvas);
		frame.pack();
		frame.setVisible(true);
	}
}
