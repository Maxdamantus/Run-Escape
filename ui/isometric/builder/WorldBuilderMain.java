package ui.isometric.builder;

import game.GameWorld;
import ui.isometric.mock.IsoGameLogicMock;
import util.*;

public class WorldBuilderMain {
	private static GameWorld sgm = new GameWorld();

	public static void main(String[] args) {
		game.GameThing tile = new game.things.GroundTile(sgm);
		sgm.level(0).put(new Position(5, 0), tile);
		sgm.level(0).put(new Position(5, 1), new game.things.GroundTile(sgm, "ground_grey_water_two_sides"));
		IsoInterfaceWorldBuilder view = new IsoInterfaceWorldBuilder("IsoTest", sgm, new IsoGameLogicMock());
		view.show();
	}
}
