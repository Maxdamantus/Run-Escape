package game;

import util.*;
import java.util.*;

public class Level extends Level<GameThing> {
	public boolean contains(GameThing gt){
		Location l = gt.location();
		if(l instanceof LevelLocation){
			for(GameThing g : portion(((LevelLocation)l).position(), ((LevelLocation)l).position()))
				if(gt == g)
					return true;
			return false;
		}
		throw new RuntimeException("wtf");
	}
}
