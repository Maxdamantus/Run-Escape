package ui.isometric.mock;

import game.PlayerMessage;
import clientinterface.GameLogic;
import clientinterface.GameThing;

/**
 * 
 * An implementation of GameLogic that just prints out any calls to it, used for testing of ui
 * 
 * @author melby
 *
 */
public class IsoGameLogicMock implements GameLogic {

	@Override
	public void performActionOn(String action, GameThing object, PlayerMessage who) {
		System.out.println("Perform " + action + " on " + object + " by " + who);
	}

}
