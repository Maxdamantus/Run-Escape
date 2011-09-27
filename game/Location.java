package game;

import serialization.*;

public interface Location {
	public static final Writer<Location> WRITER = new Writer<Location>(){
		public Tree write(Location in){
			if(in instanceof LevelLocation){
				Tree out = new Tree();
				out.add(new Tree.Entry("type", new Tree("level")));
				out.add(new Tree.Entry("just", LevelLocation.WRITER.write((LevelLocation)in)));
				return out;
			}
			throw new RuntimeException("wtf");
		}
	};
	public void put(GameThing gt);
	public void remove(GameThing gt);
}
