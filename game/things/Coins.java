package game.things;

import serialization.ParseException;
import serialization.Serializer;
import serialization.SerializerUnion;
import serialization.Serializers;
import serialization.Tree;
import game.*;

public class Coins extends Stackable {
	public static void makeSerializer(SerializerUnion<GameThing> union, final GameWorld world){
		union.addIdentifier(new SerializerUnion.Identifier<GameThing>(){
			public String type(GameThing g){
				return g instanceof Coins? "coins" : null;
			}
		});

		union.addSerializer("coins", new Serializer<GameThing>(){
			public Tree write(GameThing o){
				Coins in = (Coins)o;
				Tree out = new Tree();
				out.add(new Tree.Entry("amount", Serializers.Serializer_Integer.write(in.amount())));
				return out;
			}

			public GameThing read(Tree in) throws ParseException {
				return new Coins(world,Serializers.Serializer_Integer.read(in.find("amount")));
			}
			
		});
	}
		
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
