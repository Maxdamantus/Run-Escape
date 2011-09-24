package game.things;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ui.isometric.IsoRendererLibrary;
import util.Area;
import util.Direction;
import util.Position;

import game.*;

public class Player extends AbstractGameThing {
	private String renderer;

	public Player(GameModel model, Location loc, Direction dir, String renderer){
		super(model);
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put(IsoRendererLibrary.RENDERER_ISOMETRIC_LEVEL, 1);
		this.userArguments().put(IsoRendererLibrary.RENDERER_ISOMETRIC, map);
		this.renderer = renderer;
	}
	public String renderer(){
		return this.renderer;
	}
}
