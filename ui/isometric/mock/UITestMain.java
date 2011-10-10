package ui.isometric.mock;

import ui.isometric.IsoInterface;
import ui.isometric.client.ClientInterface;
import util.*;
import game.*;

/**
 * The main class of the UI test, creates a ui without any real game logic
 * 
 * @author melby
 *
 */
public class UITestMain {
	private static GameWorld sgm = new GameWorld();

	public static void main(String[] args) {
		game.GameThing tile = new game.things.GroundTile(sgm);
		sgm.level(0).location(new Position(5, 0), Direction.NORTH).put(tile);
		sgm.level(0).location(new Position(5, 1), Direction.NORTH).put(new game.things.GroundTile(sgm, "ground_grey_water_two_sides"));
		sgm.level(0).location(new Position(5, -2), Direction.NORTH).put(new game.things.SpawnPoint(sgm));
		sgm.getPlayer("Cordi", "Cordi").login();
		IsoInterface view = new ClientInterface("IsoTest", sgm, new ClientMessageHandlerMock(), sgm.getPlayer("Cordi", "Cordi").gid());
		view.show();
	}
}
