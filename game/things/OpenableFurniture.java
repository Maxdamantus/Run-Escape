package game.things;

import java.util.*;

import serialization.*;

import util.Direction;
import game.*;

public class OpenableFurniture extends AbstractGameThing implements Togglable, Containable {
	public static void makeSerializer(final SerializerUnion<GameThing> union, final GameWorld world){
		union.addIdentifier(new SerializerUnion.Identifier<GameThing>(){
			public String type(GameThing g){
				return g instanceof OpenableFurniture? "OpenableFurniture" : null;
			}
		});
		
		union.addSerializer("OpenableFurniture", new Serializer<GameThing>(){
			public Tree write(GameThing o){
				OpenableFurniture in = (OpenableFurniture)o;
				Tree out = new Tree();
				out.add(new Tree.Entry("type", new Tree(in.renderer)));
				out.add(new Tree.Entry("state", new Tree(Boolean.toString(in.open))));
				out.add(new Tree.Entry("contents", Container.serializer(union.serializer(), world).write(in.contents)));
				
				return out;
			}

			public GameThing read(Tree in) throws ParseException {
				return new OpenableFurniture(world, in.find("type").value(), Boolean.parseBoolean(in.find("open").value()), 
						Container.serializer(union.serializer(), world).read(in.find("contents")));
			}
		});
	}
		
	private final String renderer;
	private boolean open;
	private final Container contents;

	
	//For reading in (serializer to be completed)
	public OpenableFurniture(GameWorld world, String name, boolean open, Container cont){
		super(world);
		renderer = name;
		if(cont != null){
			this.contents = cont;
		}
		else{
			this.contents = new Container(world);
		}
		this.open = open;
		update();
	}
	
	//For empty chest
	public OpenableFurniture(GameWorld world, String name){
		super(world);
		renderer = name;
		open = false;
		contents = new Container(world);
		update();
	}


	//need the default renderer for a chest, at the moment looks like a wall...
	public OpenableFurniture(GameWorld world){
		this(world, "chest_1");
	}

	public String renderer(){
		return renderer + "_" + renderState();
	}

	private String renderState() {
		if(open){
			return "open";
		}
		else{
			return "closed";
		}
	}
	
	public List<String> interactions(){
		ArrayList<String> interactions = new ArrayList<String>();
		if(open){
			interactions.add("view contents");
			interactions.add("close");
			}	
		else{
			interactions.add("open");
			}
		interactions.add("examine");
		return interactions;
			
	}

	public String name(){
		return renderer;
	}
	
	public Map<String,Container> getContainers(){
		Map<String,Container> returnmap = new HashMap<String,Container>();
		if(contents != null){
			returnmap.put("contents", contents);
		}
		return returnmap;
	}
	
	public void walkAndSet(final boolean s, Player p){
		Location l = location();
		if(l instanceof Level.Location)
			p.moveTo((Level.Location)l, 1, new Runnable(){
				public void run(){
					open = s;
					update();
				}
			});
	}
	
	public void interact(String name, game.things.Player who){
		if(name.equals("close"))
			walkAndSet(false, who);
		else if(name.equals("open"))
			walkAndSet(true, who);
		else if(name.equals("view contents")){
			walkAndSet(true,who);
			who.showContainer(contents, renderer);
		}
		else super.interact(name, who);
	}
	
	@Override
	public boolean canWalkInto(Direction d, Character p) {
		return false;
	}
	
	@Override
	public int renderLevel() {
		return ui.isometric.abstractions.IsoSquare.FURNATURE;
	}

	public void toggle(){
		open ^= true;
		update();
	}
}
