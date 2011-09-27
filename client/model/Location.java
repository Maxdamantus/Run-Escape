package client.model;

import serialization.*;

public interface Location extends common.Location<GameThing> {
	public static class ReaderS implements Reader<Location> {
		private final GameWorld world;

		public ReaderS(GameWorld w){
			world = w;
		}

		public Location read(Tree in){
			Tree tmp = null;
			Reader<? extends Location> sub = null;

			for(Tree.Entry te : in.children())
				if(te.name().equals("type")){
					String t = te.tree().value();
					if(t.equals("level"))
						sub = LevelLocation.READER;
					throw new RuntimeException("wtf");
				}
				else if(te.name().equals("just"))
					tmp = te.tree();
			return sub.read(tmp);
		}
	}

	public void put(GameThing gt);
	public void remove(GameThing gt);
}
