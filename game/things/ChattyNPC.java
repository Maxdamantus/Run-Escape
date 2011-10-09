package game.things;

import game.GameThing;
import game.GameWorld;
import game.Level;
import game.Location;
import game.LocationS;

import java.util.LinkedList;
import java.util.List;

import serialization.ParseException;
import serialization.Serializer;
import serialization.SerializerUnion;
import serialization.Serializers;
import serialization.Tree;
import util.Direction;

public class ChattyNPC extends Character {

	public static void makeSerializer(final SerializerUnion<GameThing> union, final GameWorld world){
		union.addIdentifier(new SerializerUnion.Identifier<GameThing>(){
			public String type(GameThing g){
				return g instanceof ChattyNPC? "ChattyNPC" : null;
			}
		});

		union.addSerializer("ChattyNPC", new Serializer<GameThing>(){
			public Tree write(GameThing o){
				ChattyNPC in = (ChattyNPC)o;
				Tree out = new Tree();
				out.add(new Tree.Entry("type", new Tree(in.type())));
				out.add(new Tree.Entry("name", new Tree(in.name)));
				out.add(new Tree.Entry("response", new Tree(in.response)));
				return out;
			}

			public GameThing read(Tree in) throws ParseException {
				return new ChattyNPC(world,
					in.find("type").value(),
					in.find("name").value(),
					in.find("response").value());
			}
		});
	}

	private final String name;
	private final String response;

	public ChattyNPC(GameWorld world, String t, String n, String resp){
		super(world, t);
		name = n;
		response = resp;
		health(1000);
		setStats(1,1,1,1);
		update();

	}

	public String name(){
		return name;
	}

	public List<String> interactions(){
		List<String> out = new LinkedList<String>(super.interactions());
		out.add("talk");
		out.add("examine");
		return out;
	}

	public void interact(String name, Player who){
		if(name.equals("talk"))
			world().emitSay(this, who, name()+": "+response);
		else super.interact(name, who);
	}
}