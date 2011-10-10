package game.things;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.AbstractGameThing;
import game.Containable;
import game.Container;
import game.GameThing;
import game.GameWorld;
import game.Level;
import game.Location;

public class Corpse extends AbstractGameThing implements Containable {

	private final String renderer;
	private final Container contents;
	
	
	public Corpse(GameWorld world, String name, Container cont){
		super(world);
		renderer = name;
		if(cont != null){
			this.contents = cont;
		}
		else{
			this.contents = new Container(world);
		}
		update();
	}
	
	@Override
	public int renderLevel() {
		return ui.isometric.abstractions.IsoSquare.FURNATURE;
	}
		
	@Override
	public Map<String, Container> getContainers() {
		Map<String,Container> returnmap = new HashMap<String,Container>();
		if(contents != null){
			returnmap.put("contents", contents);
		}
		return returnmap;
	}

	@Override
	public String renderer() {
		return renderer;
	}
	
	public List<String> interactions(){
		ArrayList<String> interactions = new ArrayList<String>();
		interactions.add("loot body");
		interactions.add("examine");
		return interactions;
			
	}
	
	public void interact(String name, final game.things.Player who){
		if(name.equals("loot body")){
			Location l = location();
			final GameThing g = this;
			if(l instanceof Level.Location){
				who.moveTo((Level.Location)l, 1, new Runnable(){
					public void run(){
						update();
						who.showContainer(contents, renderer);
					}
				});
			
			}
		}
	}
	

	@Override
	public String name() {
		return "Corpse";
	}

}
