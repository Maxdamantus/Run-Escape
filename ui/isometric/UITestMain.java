package ui.isometric;

import util.*;
import clientinterface.*;

public class UITestMain {
	private static game.GameModel sgm = new game.GameModel();

	public static void main(String[] args) {
		game.GameThing tile = new game.things.GroundTile(sgm);
		sgm.level(0).put(new Position(5, 4), tile);
		IsoInterface view = new IsoInterface("IsoTest", Conversions.fromServerGameModel(sgm));
		view.show();
	}
}
