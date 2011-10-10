package game.things;

import game.Container;
import game.GameThing;
import game.GameWorld;
import game.Location;

import java.util.LinkedList;
import java.util.List;

import serialization.ParseException;
import serialization.Serializer;
import serialization.SerializerUnion;
import serialization.Serializers;
import serialization.Tree;

public class Key extends PickupGameThing {
	public static void makeSerializer(SerializerUnion<GameThing> union, final GameWorld world){
		union.addIdentifier(new SerializerUnion.Identifier<GameThing>(){
			public String type(GameThing g){
				return g instanceof Key? "Key" : null;
			}
		});

		union.addSerializer("Key", new Serializer<GameThing>(){
			public Tree write(GameThing o){
				Key in = (Key)o;
				Tree out = new Tree();
				out.add(new Tree.Entry("renderer", new Tree(in.renderer)));
				out.add(new Tree.Entry("doorcode", new Tree(in.renderer)));
				return out;
			}

			public GameThing read(Tree in) throws ParseException {
				return new Key(world,
					in.find("renderer").value(),
					in.find("doorcode").value());
			}
		});
	}
	
	private String name, renderer, doorcode;
	
	public Key(GameWorld w, String ren, String drcd){
		super(w);
		name = "Key";
		this.renderer = ren;
		doorcode = drcd;
	}
	
	@Override
	public String renderer() {
		return renderer;
	}

	@Override
	public String name() {
		return name;
	}
	
	public String doorcode(){
		return doorcode;
	}

}
