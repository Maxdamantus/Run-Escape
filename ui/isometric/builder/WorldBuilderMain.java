package ui.isometric.builder;

import game.GameWorld;
import ui.isometric.mock.ClientMessageHandlerMock;

public class WorldBuilderMain {
	private static GameWorld sgm = new GameWorld();

	public static void main(String[] args) {
		IsoInterfaceWorldBuilder view = new IsoInterfaceWorldBuilder("World Builder", sgm, new ClientMessageHandlerMock());
		view.show();
	}
}
