package game.things;

import game.*;

import java.util.*;

import serialization.*;

public class Letters {
	public static void makeSerializer(SerializerUnion<GameThing> union, GameWorld world){
		Letter.makeSerializer(union, world);
		Sequence.makeSerializer(union, world);
	}

	public static class Letter extends PickupGameThing {
		public static void makeSerializer(SerializerUnion<GameThing> union, final GameWorld world){
			union.addIdentifier(new SerializerUnion.Identifier<GameThing>(){
				public String type(GameThing g){
					return g instanceof Letter? "letter" : null;
				}
			});

			union.addSerializer("letter", new Serializer<GameThing>(){
				public Tree write(GameThing o){
					Letter in = (Letter)o;
					return new Tree(String.valueOf(in.which));
				}

				public GameThing read(Tree in){
					return new Letter(world, in.value().charAt(0));
				}
			});
		}

		private final char which;

		public Letter(GameWorld w, char c){
			super(w);
			which = c;
			update();
		}
		
		public String name(){
			return "Letter (" + which + ")";
		}

		public List<String> interactions(){
			List<String> out = new LinkedList<String>();
			out.add("after");
			out.add("before");
			out.add("destroy");
			return out;
		}

		public void interact(String name, Player who){
			if(name.equals("after")){
				location().put(new Letter(world(), (char)(which + 1)));
				destroy();
			}
			else if(name.equals("before")){
				location().put(new Letter(world(), (char)(which - 1)));
				destroy();
			}
			else if(name.equals("destroy"))
				destroy();
			else if(name.equals("send"))
				who.buffer().put(this);
		}

		public String renderer(){
			return "armour_tunic";
		}

		public Map<String, String> info(){
			Map<String, String> out = new HashMap<String, String>();
			out.put("stackcount", String.valueOf(which));
			return out;
		}

		public void destroy(){
			LocationS.NOWHERE.put(this);
			world().forget(this);
		}

		public char value(){
			return which;
		}

		public int renderLevel(){
			return -42000-9000;
		}
	}

	public static class Sequence extends PickupGameThing {
		public static void makeSerializer(SerializerUnion<GameThing> union, final GameWorld world){
			union.addIdentifier(new SerializerUnion.Identifier<GameThing>(){
				public String type(GameThing g){
					return g instanceof Sequence? "string" : null;
				}
			});

			union.addSerializer("string", new Serializer<GameThing>(){
				public Tree write(GameThing o){
					Sequence in = (Sequence)o;
					return new Tree(String.valueOf(in.current));
				}

				public GameThing read(Tree in){
					return new Sequence(world, in.value());
				}
			});
		}

		private String current;

		public Sequence(GameWorld w, String c){
			super(w);
			current = c;
			update();
		}

		public String name(){
			return "String (" + current + ")";
		}

		public List<String> interactions(){
			List<String> out = new LinkedList<String>();
			out.add("push");
			out.add("create");
			out.add("pop");
			out.add("duplicate");
			out.addAll(super.interactions());
			return out;
		}
		
		public void interact(String name, Player who){
			if(name.equals("push")){
				for(GameThing g : who.buffer().snapshot())
					if(g instanceof Letter)
						current += ((Letter)g).value();
				update();
			}
			else if(name.equals("create"))
				who.buffer().put(new Letter(world(), 'M'));
			else if(name.equals("pop")){
				who.buffer().put(new Letter(world(), current.charAt(current.length())));
				current = current.substring(0, current.length() - 1);
				update();
			}
			else if(name.equals("duplicate"))
				who.buffer().put(new Sequence(world(), current));
			else
				super.interact(name, who);
		}

		public Map<String, String> info(){
			Map<String, String> out = new HashMap<String, String>();
			out.put("stackcount", current);
			return out;
		}

		public String renderer(){
			return "ruby";
		}

		public String value(){
			return current;
		}

		public int renderLevel(){
			return -42000-9000;
		}
	}
}
