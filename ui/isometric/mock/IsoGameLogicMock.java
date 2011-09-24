package ui.isometric.mock;

import client.model.GameLogic;
import client.model.GameThing;

/**
 * 
 * An implementation of GameLogic that just prints out any calls to it, used for testing of ui
 * 
 * @author melby
 *
 */
public class IsoGameLogicMock implements GameLogic {

	@Override
	public void performActionOn(String action, GameThing object) {
		System.out.println("Perform " + action + " on " + object);
	}

}
