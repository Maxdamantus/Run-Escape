package game.things;

import java.util.ArrayList;
import java.util.List;

import game.*;

public class Door extends AbstractGameThing {
	private final String openRenderer;
	private final String closedRenderer;
	private boolean open;

	public Door(GameWorld world, String closedRenderer, String openRenderer, boolean open){
		super(world);
		this.openRenderer = openRenderer;
		this.closedRenderer = closedRenderer;
		this.open = open;
		update();
	}

	@Override
	public String renderer(){
		return open?openRenderer:closedRenderer;
	}
	
	@Override
	public List<String> interactions(){
		return new ArrayList<String>(){private static final long serialVersionUID = 1L;{this.add(defaultInteraction());}};
	}
	
	@Override
	public String defaultInteraction() {
		if(open) {
			return "close";
		}
		else {
			return "open";
		}
	}
	
	@Override
	public void interact(String inter, Player who) {
		if(inter.equals("close") && open) {
			open = false;
			update();
		}
		else if(inter.equals("open") && !open) {
			open = true;
			update();
		}
	}

	@Override
	public String name(){
		return "Door";
	}
}
