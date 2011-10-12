package game.things;

import game.*;

import java.util.*;

public class Letters {
	public static class Letter extends PickupGameThing {
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
