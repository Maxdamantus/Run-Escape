package game.things;

import game.GameThing;
import game.GameWorld;
import game.Level;
import game.Location;
import game.LocationS;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import serialization.ParseException;
import serialization.Serializer;
import serialization.SerializerUnion;
import serialization.Serializers;
import serialization.Tree;
import util.Direction;

/**
 * 
 * @author wheelemaxw
 *
 *ChattyNPC is a type of character that the user is able to talk to, with a set of interactions which
 *are randomized (aside from 1 initial interaction and 1 secret.
 *Unattackable
 */
public class ChattyNPC extends Character {

	/**
	 * Custom serializer for ChattyNPC's
	 * @param union
	 * @param world
	 */
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
				out.add(new Tree.Entry("response", Serializers.list(Serializers.Serializer_String).write(response)));
				return out;
			}

			public GameThing read(Tree in) throws ParseException {
				return new ChattyNPC(world,
					in.find("type").value(),
					in.find("name").value(),
					Serializers.list(Serializers.Serializer_String).read(in.find("response")));
			}
		});
	}

	private final String name;
	private static List<String> response;
	private boolean talked;
	private int i;

	public ChattyNPC(GameWorld world, String t, String n, List<String> resp){
		super(world, t);
		name = n;
		response = resp;
		health(1000);
		setStats(1,1,1,1);
		update();
		i = 0;

	}

	/**
	 * Returns a string
	 * @return Name of the Thing
	 */
	public String name(){
		return name;
	}

	/**
	 * @return Returns the list of available interactions
	 */
	public List<String> interactions(){
		List<String> out = new LinkedList<String>(super.interactions());
		out.add("talk");
		out.add("examine");
		return out;
	}
	
	/**
	 * Requires the player to walk to the NPC before emitting the say message
	 * @param p - player interacting with
	 * @param say - string that NPC will say.
	 */
	public void walkAndTalk(final Player p, final String say){
		Location l = location();
		final GameThing g = this;
		if(l instanceof Level.Location){
			p.moveTo((Level.Location)l, 1, new Runnable(){
				public void run(){
					update();
					world().emitSay(g, p, say);
				}
			});
		p.face(l);
		}
	}

	/**
	 * interaction instructions for talking, 
	 * Gets either the initial interaction, a randomized one from size-2 or the secret one(after 20 talks)
	 */
	public void interact(String name, Player who){
		if(name.equals("talk")){
			if(talked == false){
				walkAndTalk(who, name()+": "+response.get(response.size()-2));
				talked = true;
				i++;
			}
			else if(i < 20){
				walkAndTalk( who, name()+": "+response.get((int)(Math.random()*(response.size()-2))));
				i++;
			}
			else{
				walkAndTalk(who, name()+": "+response.get(response.size()-1));
				i=0;
			}
			
		}
		else super.interact(name, who);
	}
}