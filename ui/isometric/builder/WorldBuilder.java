package ui.isometric.builder;

import game.GameWorld;
import ui.isometric.mock.ClientMessageHandlerMock;

/**
 * Main class for the world builder
 * 
 * @author melby
 *
 */
public class WorldBuilder {
	private static GameWorld sgm = new GameWorld();

	public static void main(String[] args) {
		BuilderInterface view = new BuilderInterface("World Builder", sgm, new ClientMessageHandlerMock());
		view.show();
	}
}
