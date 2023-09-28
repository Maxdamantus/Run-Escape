package game.things;

import game.*;

import java.util.*;

import serialization.*;

public class Pouch extends PickupGameThing implements Containable {
	public static void makeSerializer(final SerializerUnion<GameThing> union, final GameWorld world){
		union.addIdentifier(new SerializerUnion.Identifier<GameThing>(){
			public String type(GameThing g){
				return g instanceof Pouch? "pouch" : null;
			}
		});

		union.addSerializer("pouch", new Serializer<GameThing>(){
			public Tree write(GameThing o){
				Pouch in = (Pouch)o;
				Tree out = new Tree();
				out.add(new Tree.Entry("contents", Container.serializer(union.serializer(), world).write(in.container)));
				out.add(new Tree.Entry("name", new Tree(in.pname)));
				return out;
			}

			public GameThing read(Tree in) throws ParseException {
				return new Pouch(world,
					Container.serializer(union.serializer(), world).read(in.find("contents")),
					in.find("name").value());
			}
		});
	}

	private final Container container;
	private String pname;

	public Pouch(GameWorld w){
		this(w, new Container(w), "");
	}

	private Pouch(GameWorld w, Container c, String n){
		super(w);
		container = c;
		pname = n;
		update();
	}

	public String name(){
		return "Pouch" + (pname.equals("")? "" : " (" + pname + ")");
	}

	public Location location(Location s){
		Location old = location();
		if(s instanceof Container && old instanceof Container && ((Container)old).hasParent((Container)s))
			return super.location(old);
		return super.location(s);
	}

	public List<String> interactions(){
		List<String> out = new LinkedList<String>();
		if(location() instanceof game.Container && ((Container)location()).owner() != null){
			out.add("receive");
			out.add("view contents");
			out.add("set name");
		}
		out.add("get name");
		out.addAll(super.interactions());
		return out;
	}

	public void interact(String name, Player who){
		if(name.equals("view contents"))
			who.showContainer(container, "Pouch");
		else if(name.equals("receive") && ((Container)location()).owner() == who){
			for(GameThing got : who.buffer().snapshot())
				container.put(got);
		}
		else if(name.equals("set name") && ((Container)location()).owner() == who){
			for(GameThing got : who.buffer().snapshot())
				if(got instanceof Letters.Sequence){
					pname = ((Letters.Sequence)got).value();
					update();
				}
		}
		else if(name.equals("get name"))
			who.buffer().put(new Letters.Sequence(world(), pname));
		else
			super.interact(name, who);
	}

	public Map<String, Container> getContainers(){
		Map<String, Container> out = new HashMap<String, Container>();
		out.put("contents", container);
		return out;
	}

	public Map<String, String> info(){
		if(pname.equals(""))
			return super.info();
		Map<String, String> out = new HashMap<String, String>();
		out.put("stackcount", pname);
		return out;
	}

	public String renderer(){
		return "pouch";
	}
}
