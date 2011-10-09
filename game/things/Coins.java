package game.things;

import game.*;

public class Coins extends Stackable {
	private final static Object type = new Object();

	public Coins(GameWorld w, int amt){
		super(w, amt);
	}

	public Object type(){
		return type;
	}

	public String name(){
		return amount() != 1? "Coins" : "Coin";
	}

	public String renderer(){
		return "coins_gold";
	}

	public int renderLevel(){
		return ui.isometric.abstractions.IsoSquare.DROP;
	}
}
