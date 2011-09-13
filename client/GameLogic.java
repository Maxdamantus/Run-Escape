package client;

// MaxZ's note: I'd imagine performining an action on something would be an operation on the object,
//  in OOP.
public interface GameLogic {
	public void performActionOn(String action, GameThing object);
}
